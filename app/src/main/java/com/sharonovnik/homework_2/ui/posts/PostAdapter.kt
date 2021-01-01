package com.sharonovnik.homework_2.ui.posts

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.sharonovnik.homework_2.R
import com.sharonovnik.homework_2.data.local.entity.PostEntity
import com.sharonovnik.homework_2.ui.DateTimeConverter
import com.sharonovnik.homework_2.ui.DecorationTypeProvider
import com.sharonovnik.homework_2.ui.SwipeTouchCallback
import com.sharonovnik.homework_2.ui.custom.PostLayout
import kotlinx.android.synthetic.main.view_post.view.*
import kotlinx.android.synthetic.main.view_post_header.view.*

class PostAdapter(
    private val onLikeChangedListener: (PostEntity?, liked: Boolean) -> Unit,
    private val onClickListener: (PostRow) -> Unit,
    private val onOptionsClickListener: (View, Drawable) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    SwipeTouchCallback.ItemTouchHelperAdapter, DecorationTypeProvider {

    private val differCallback = object : DiffUtil.ItemCallback<PostRow>() {
        override fun areItemsTheSame(oldItem: PostRow, newItem: PostRow): Boolean {
            return when {
                oldItem is PostRow.Body && newItem is PostRow.Body -> {
                    oldItem.post.id == newItem.post.id
                }
                oldItem is PostRow.TextBody && newItem is PostRow.TextBody -> {
                    oldItem.post.id == newItem.post.id
                }
                oldItem is PostRow.Header && newItem is PostRow.Header -> {
                    oldItem.date == newItem.date
                }
                else -> false
            }
        }
        override fun areContentsTheSame(oldItem: PostRow, newItem: PostRow): Boolean {
            return when (oldItem) {
                is PostRow.TextBody -> {
                    oldItem.post == (newItem as PostRow.TextBody).post
                }
                is PostRow.Body -> {
                    oldItem.post == (newItem as PostRow.Body).post
                }
                is PostRow.Header -> {
                    oldItem == newItem
                }
            }
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var posts: List<PostRow> = emptyList()
        set(value) {
            if (value.isNotEmpty()) {
                differ.submitList(value)
                field = value
            }
        }

    override fun getItemViewType(position: Int) = posts[position].postType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (PostType.values()[viewType]) {
            PostType.TEXT_BODY -> {
                val postLayout = createPostLayout(parent, false)
                TextBodyViewHolder(postLayout)
            }
            PostType.BODY -> {
                val postLayout = createPostLayout(parent)
                BodyViewHolder(postLayout)
            }
            PostType.HEADER -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.view_post_header,
                        parent,
                        false
                    )
                )
            }
        }
    }

    private fun createPostLayout(parent: ViewGroup, hasContentImage: Boolean = true): PostLayout {
        val postLayout = PostLayout(parent.context)
        postLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        postLayout.setHasContentImage(hasContentImage)
        return postLayout
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val postRow = posts[position]) {
            is PostRow.TextBody -> (holder as TextBodyViewHolder).bind(postRow)
            is PostRow.Body -> (holder as BodyViewHolder).bind(postRow)
            is PostRow.Header -> (holder as HeaderViewHolder).bind(postRow)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        // Если вызываю notify* с payload, элемент после свайпа не возвращается обратно.
        // Если не передаю payload, перерисовывается весь айтем, хотелось бы перерисовать только иконку лайка
        if (payloads.isNotEmpty()) {
            if (payloads[0] is Boolean) {
                changeLikeIconAndTextColor(
                    holder.itemView.like,
                    holder.itemView.likeCount,
                    payloads[0] as Boolean
                )
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount() = posts.size

    override fun isHeader(position: Int): Boolean {
        if (position == RecyclerView.NO_POSITION) {
            return false
        }
        return posts[position].postType == PostType.HEADER
    }

    override fun getHeaderPositionForItem(position: Int): Int {
        var headerPosition = 0
        var itemPosition = position
        do {
            if (isHeader(position)) {
                headerPosition = itemPosition
                break
            }
            itemPosition -= 1
        } while (itemPosition >= 0)
        return headerPosition
    }

    override fun onItemLikeStateChanged(position: Int, liked: Boolean) {
        val likedPost = posts[position].post
        val likeCount = likedPost?.likes?.userLikes
        likeCount?.let { count ->
            val isAlreadyLiked = count > 0
            val likes = likedPost.likes
            when (isAlreadyLiked) {
                true -> {
                    if (!liked) {
                        if (likes.count > 0) {
                            likes.count--
                        }
                        likes.userLikes = 0
                    }
                }
                false -> {
                    if (liked) {
                        likes.userLikes = 1
                        likes.count++
                    }
                }
            }
            notifyItemChanged(position)
            onLikeChangedListener.invoke(likedPost, liked)
        }
    }

    inner class TextBodyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(textBody: PostRow.TextBody) {
            val post = textBody.post
            itemView.run {
                name.text = post.publicName
                contentText.text = post.text
                date.text = DateTimeConverter.getDateFromUnixTime(post.date)
                Glide.with(context)
                    .load(post.avatar)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.no_image_placeholder)
                    .into(avatar)
                likeCount.text = (post.likes?.count ?: 0).toString()
                commentCount.text = (post.comments?.count ?: 0).toString()
                changeLikeIconAndTextColor(like, likeCount, textBody.post.likes?.userLikes == 1)
                options.visibility = View.GONE
            }
            itemView.setOnClickListener {
                onClickListener.invoke(posts[adapterPosition])
            }
        }
    }

    inner class BodyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(body: PostRow.Body) {
            val post = body.post
            itemView.run {
                name.text = post.publicName
                contentText.text = post.text
                date.text = DateTimeConverter.getDateFromUnixTime(post.date)
                Glide.with(context)
                    .load(post.avatar)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.no_image_placeholder)
                    .into(avatar)
                likeCount.text = (post.likes?.count ?: 0).toString()
                commentCount.text = (post.comments?.count ?: 0).toString()
                changeLikeIconAndTextColor(like, likeCount, post.likes?.userLikes == 1)
                val photo = post.attachments?.get(0)?.photo
                if (photo != null) {
                    (this as PostLayout).setImage(photo)
                    Glide.with(context)
                        .load(photo.sizes[photo.sizes.size - 1].url)
                        .into(contentImage)
                }

                options.setOnClickListener {
                    onOptionsClickListener(it, contentImage.drawable)
                }
            }
            itemView.setOnClickListener {
                onClickListener.invoke(posts[adapterPosition])
            }
        }
    }

    private fun changeLikeIconAndTextColor(like: ImageView, likeText: TextView, pressed: Boolean) {
        if (pressed) {
            like.setImageDrawable(ContextCompat.getDrawable(like.context, R.drawable.ic_like_pressed))
            likeText.setTextColor(ContextCompat.getColor(likeText.context, R.color.red))
        } else {
            like.setImageDrawable(ContextCompat.getDrawable(like.context, R.drawable.ic_like))
            likeText.setTextColor(ContextCompat.getColor(likeText.context, android.R.color.darker_gray))
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(body: PostRow.Header) {
            itemView.headerText.text = body.date
        }
    }
}