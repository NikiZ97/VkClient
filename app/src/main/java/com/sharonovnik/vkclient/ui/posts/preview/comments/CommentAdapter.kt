package com.sharonovnik.vkclient.ui.posts.preview.comments

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.sharonovnik.vkclient.R
import com.sharonovnik.vkclient.data.network.response.Comment
import com.sharonovnik.vkclient.ui.DateTimeConverter
import com.sharonovnik.vkclient.ui.base.RecyclerBindableAdapter
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter(
    comments: List<Comment>,
    diffCallback: DiffUtil.ItemCallback<Comment>
) : RecyclerBindableAdapter<Comment, RecyclerView.ViewHolder>(comments, diffCallback) {

    override fun layoutId(type: Int): Int {
        return R.layout.item_comment
    }

    override fun viewHolder(view: View, type: Int): RecyclerView.ViewHolder {
        return CommentHolder(view)
    }

    override fun onBindItemViewHolder(
        viewHolder: RecyclerView.ViewHolder, position: Int, type: Int
    ) {
        (viewHolder as CommentHolder).bind(getItem(position))
    }

    private inner class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(comment: Comment) {
            Glide.with(itemView.context)
                .load(comment.profile?.photo50)
                .transform(CircleCrop())
                .into(itemView.avatarImage)
            with(itemView.ownerName) {
                text = comment.profile?.firstName
                append(" ")
                append(comment.profile?.lastName)
            }
            itemView.commentText.text = comment.comment?.text
            itemView.commentTime.text =
                DateTimeConverter.getDateFromUnixTime(comment.comment?.date)
        }
    }
}