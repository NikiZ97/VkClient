package com.sharonovnik.vkclient.ui.posts.preview

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.sharonovnik.vkclient.*
import com.sharonovnik.vkclient.data.local.storage.ImagesManager
import com.sharonovnik.vkclient.data.network.response.Comment
import com.sharonovnik.vkclient.ui.PermissionResolver
import com.sharonovnik.vkclient.ui.base.BaseActivity
import com.sharonovnik.vkclient.ui.custom.PostLayout
import com.sharonovnik.vkclient.ui.posts.ImageFullscreenActivity
import com.sharonovnik.vkclient.ui.posts.preview.comments.CommentAdapter
import kotlinx.android.synthetic.main.activity_post_preview.*
import kotlinx.android.synthetic.main.layout_comment_input_field.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.view_post.*
import kotlinx.android.synthetic.main.view_post.view.*
import javax.inject.Inject

class PostPreviewActivity : BaseActivity() {
    private var postId: Int = 0
    private var ownerId: Long = 0
    private lateinit var viewModel: PostPreviewViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var imagesManager: ImagesManager
    private lateinit var app: NewsFeedApplication
    private lateinit var postLayout: PostLayout
    private lateinit var commentAdapter: CommentAdapter
    @Inject
    lateinit var permissionResolver: PermissionResolver

    companion object {
        const val EXTRA_POST_PREVIEW_ID_KEY = "post_preview_id_key"
        const val EXTRA_OWNER_ID_KEY = "owner_id_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_preview)
        injector?.inject(this)
        app = (applicationContext as NewsFeedApplication)
        postId = intent.getIntExtra(EXTRA_POST_PREVIEW_ID_KEY, 0)
        ownerId = intent.getLongExtra(EXTRA_OWNER_ID_KEY, 0)
        viewModel = ViewModelProvider(this, viewModelFactory)[PostPreviewViewModel::class.java]
        viewModel.getPostPreviewById(postId, ownerId)
        setOnClickListeners()
        createHeaderLayout()
        initViews()
        subscribeUi()
    }

    private fun createHeaderLayout() {
        postLayout = PostLayout(this)
        postLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        postLayout.options.setOnClickListener { view ->
            view.showOptionsPopupWindow(doOnSave = {
                permissionResolver.obtainPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    granted = {
                        imagesManager.saveImageToGallery(contentImage.drawable) {
                            refresh.showSnackbar(getString(R.string.image_downloaded_into_pictures_folder))
                        }
                    },
                    denied = {
                        it.invoke()
                    })
            }, doOnShare = {
                imagesManager.shareImage(contentImage.drawable) { sendIntent ->
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.share_to_title)))
                }
            })
        }
    }

    private fun setOnClickListeners() {
        arrowBack.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }
        refresh.setOnRefreshListener {
            viewModel.getPostPreviewById(postId, ownerId)
        }
        sendButton.setOnClickListener {
            if (messageEditText.text.isNotEmpty()) {
                viewModel.createComment(postId, ownerId, messageEditText.text.toString())
                    .also {
                        messageEditText.setText("")
                        hideSoftKeyboard()
                    }
            }
        }
    }

    private fun initViews() {
        arrowBack.isVisible = true
        scrollUp.isVisible = false
        commentAdapter = CommentAdapter(listOf(), object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.comment?.id == newItem.comment?.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }).apply {
            addHeader(postLayout)
        }
        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@PostPreviewActivity)
            adapter = commentAdapter
            setHasFixedSize(true)
        }
    }

    private fun subscribeUi() {
        viewModel.getState()
            .observe(this, {
                render(it)
            })
    }

    private fun render(state: PostPreviewState) {
        if (state.postPreview == null) {
            return
        }
        refresh.isRefreshing = state.isLoading
        with(state.postPreview) {
            toolbarTitle.text = state.postPreview.publicName
            postLayout.setPostName(publicName ?: getString(R.string.post_preview_title))
            postLayout.setPostPublicationDate(com.sharonovnik.vkclient.ui.DateTimeConverter.getDateFromUnixTime(date.toLong()))

            val photo = attachments?.get(0)?.photo
            if (photo != null) {
                attachments!![0]?.photo?.let { postLayout.setImage(it) }
                Glide.with(this@PostPreviewActivity)
                    .load(photo.sizes[photo.sizes.size - 1].url)
                    .into(contentImage)
            } else {
                postLayout.options.isVisible = false
                postLayout.setHasContentImage(false)
            }
            Glide.with(this@PostPreviewActivity)
                .load(avatar)
                .transform(CircleCrop())
                .placeholder(R.drawable.no_image_placeholder)
                .into(this@PostPreviewActivity.avatar)
            postLayout.setPostContentText(text)
            postLayout.setLikeCount(likes?.count?.toLong())
            postLayout.setCommentCount(comments?.count)
            inputField.isVisible = comments?.canPost == 1
            if (likes != null) {
                if (likes.userLikes > 0) {
                    postLayout.like.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@PostPreviewActivity, R.drawable.ic_like_pressed
                        )
                    )
                }
            }
            postLayout.contentImage.setOnClickListener {
                val intent =
                    Intent(this@PostPreviewActivity, ImageFullscreenActivity::class.java).apply {
                        putExtra(ImageFullscreenActivity.EXTRA_IMAGE_URL, photo?.sizes!![photo.sizes.size - 1].url)
                    }
                startActivity(intent)
            }
        }
        if (state.postComments != null) {
            commentAdapter.setItems(state.postComments)

        } else if (state.error != null) {
            showShortToast(getString(R.string.error_loading_comments))
        }
    }
}