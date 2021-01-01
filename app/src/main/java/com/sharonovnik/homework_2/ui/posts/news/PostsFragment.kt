package com.sharonovnik.homework_2.ui.posts.news

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharonovnik.homework_2.*
import com.sharonovnik.homework_2.data.local.entity.PostEntity
import com.sharonovnik.homework_2.data.local.storage.ImagesManager
import com.sharonovnik.homework_2.data.network.NetworkState
import com.sharonovnik.homework_2.ui.HeaderSimpleDividerDecoration
import com.sharonovnik.homework_2.ui.PermissionResolver
import com.sharonovnik.homework_2.ui.PostsDateDecorator
import com.sharonovnik.homework_2.ui.SwipeTouchCallback
import com.sharonovnik.homework_2.ui.dialog.PostsErrorDialogFragment
import com.sharonovnik.homework_2.ui.posts.PostAdapter
import com.sharonovnik.homework_2.ui.posts.PostRow
import com.sharonovnik.homework_2.ui.posts.PostsAction
import com.sharonovnik.homework_2.ui.posts.PostsState
import com.sharonovnik.homework_2.ui.posts.preview.PostPreviewActivity
import kotlinx.android.synthetic.main.fragment_posts.*
import retrofit2.HttpException
import javax.inject.Inject

class PostsFragment : Fragment() {
    private lateinit var adapter: PostAdapter
    private var onPostLikeStateChangedListener: OnPostLikeStateChangedListener? = null
    private var onDataRefreshedListener: OnDataRefreshedListener? = null
    @Inject
    lateinit var imagesManager: ImagesManager
    private lateinit var postsViewModel: PostsViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var permissionResolver: PermissionResolver

    companion object {
        private const val ADD_LIKE_NAME = "add"
        private const val REMOVE_LIKE_NAME = "delete"

        @JvmStatic
        fun newInstance() = PostsFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as NewsFeedApplication).authComponent?.inject(this)
        if (context is OnPostLikeStateChangedListener) {
            onPostLikeStateChangedListener = context
        }
        if (context is OnDataRefreshedListener) {
            onDataRefreshedListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        permissionResolver = PermissionResolver(this)
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postsViewModel = ViewModelProvider(this, viewModelFactory)[PostsViewModel::class.java]
        postsViewModel.input.accept(PostsAction.LoadPosts)
        initRecyclerView()
        initSwipeRefreshLayout()
        subscribeUi()
    }

    private fun subscribeUi() {
        postsViewModel.getState()
            .observe(viewLifecycleOwner, {
                render(it!!)
            })
    }

    fun scrollUp() {
        posts.smoothScrollToPosition(0)
    }

    private fun render(state: PostsState) {
        toggleShimmerVisibility(state.isLoading && adapter.posts.isEmpty())
        posts.isVisible = state.items.isNotEmpty()
        adapter.posts = state.items
        if (state.isLikeStateChanged) {
            onPostLikeStateChangedListener?.onPostLikeStateChanged()
        }
        swipeRefresh.isRefreshing = state.isRefreshing
        state.error?.let {
            if (it is HttpException) {
                handleHttpException(it, state.isRefreshing)
                return@let
            }
            handleNetworkResult(
                NetworkState.NetworkException(
                    it.message ?: getString(R.string.unknown_error)
                )
            )
        }
    }

    private fun handleHttpException(it: HttpException, isRefreshing: Boolean) {
        when (it.code()) {
            403 -> handleNetworkResult(NetworkState.HttpErrors.ResourceForbidden(it.message()), isRefreshing)
            404 -> handleNetworkResult(NetworkState.HttpErrors.ResourceNotFound(it.message()), isRefreshing)
            500 -> handleNetworkResult(NetworkState.HttpErrors.InternalServerError(it.message()), isRefreshing)
        }
    }

    private fun handleNetworkResult(
        networkState: NetworkState, isRefreshing: Boolean = false,
        onSuccess: ((networkState: NetworkState.Success) -> Unit)? = null
    ) {
        when (networkState) {
            is NetworkState.Success -> {
                onSuccess?.invoke(networkState)
            }
            is NetworkState.Error -> {
                showLoadingError(isRefreshing, !isRefreshing)
            }
            is NetworkState.HttpErrors.ResourceForbidden -> showLoadingError(errorMessage = getString(R.string.resource_forbidden))
            is NetworkState.HttpErrors.ResourceNotFound -> showLoadingError(errorMessage = getString(R.string.resource_not_found))
            is NetworkState.HttpErrors.InternalServerError -> showLoadingError(errorMessage = getString(R.string.internal_server_error))
            is NetworkState.NetworkException -> {
                showLoadingError(showPlaceholder = false, errorMessage = getString(R.string.error_occurred))
            }
        }
    }

    private fun initRecyclerView(resultData: List<PostRow> = listOf()) {
        posts.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter(
            onLikeChangedListener = { likedPost, liked ->
                if (likedPost != null) {
                    changePostLikeState(likedPost, liked)
                }
            }, onClickListener = ::openPreviewActivity, onOptionsClickListener = ::showPopupMenu)
        adapter.posts = resultData
        posts.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeTouchCallback(adapter))
        itemTouchHelper.attachToRecyclerView(posts)
        posts.addItemDecoration(
            HeaderSimpleDividerDecoration(requireContext(), ContextCompat.getDrawable(
            requireContext(), R.drawable.post_divider_drawable
        )))
        posts.addItemDecoration(PostsDateDecorator(posts, true))
    }

    private fun openPreviewActivity(postRow: PostRow) {
        requireContext().openActivity(PostPreviewActivity::class.java) {
            if (postRow.post != null) {
                putInt(PostPreviewActivity.EXTRA_POST_PREVIEW_ID_KEY, postRow.post!!.id)
                putLong(PostPreviewActivity.EXTRA_OWNER_ID_KEY, -postRow.post!!.source)
            }
        }
    }

    private fun changePostLikeState(post: PostEntity, liked: Boolean) {
        val methodName = if (liked) ADD_LIKE_NAME else REMOVE_LIKE_NAME
        postsViewModel.input.accept(PostsAction.LikePost(methodName, post))
    }

    private fun showPopupMenu(view: View, drawable: Drawable) {
        view.showOptionsPopupWindow(doOnSave = {
            permissionResolver.obtainPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                granted = {
                    imagesManager.saveImageToGallery(drawable) {
                        swipeRefresh.showSnackbar(getString(R.string.image_downloaded_into_pictures_folder))
                    }
                },
                denied = {
                    it.invoke()
                })
        }, doOnShare = {
            imagesManager.shareImage(drawable) { sendIntent ->
                requireContext().startActivity(Intent.createChooser(sendIntent, getString(R.string.share_to_title)))
            }
        })
    }

    private fun initSwipeRefreshLayout() {
        swipeRefresh.setOnRefreshListener {
            postsViewModel.input.accept(PostsAction.LoadPostsWithPullToRefresh)
        }
    }

    private fun showLoadingError(
        showPlaceholder: Boolean = true, showDialog: Boolean = true, errorMessage: String? = null
    ) {
        if (showPlaceholder) {
            errorPlaceholder.visibility = View.VISIBLE
        }
        if (showDialog) {
            PostsErrorDialogFragment.newInstance(errorMessage)
                .show(childFragmentManager, PostsErrorDialogFragment.javaClass.name)
        }
    }

    private fun toggleShimmerVisibility(show: Boolean) {
        with(shimmer) {
            if (show) {
                isVisible = true
                startShimmer()
            } else {
                stopShimmer()
                isVisible = false
            }
        }
    }

    interface OnPostLikeStateChangedListener {
        fun onPostLikeStateChanged()
    }

    interface OnDataRefreshedListener {
        fun onDataRefreshed()
    }
}