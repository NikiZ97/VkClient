package com.sharonovnik.homework_2.ui.posts.favorite

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharonovnik.homework_2.*
import com.sharonovnik.homework_2.data.local.storage.ImagesManager
import com.sharonovnik.homework_2.ui.EqualSpaceItemDecoration
import com.sharonovnik.homework_2.ui.HeaderSimpleDividerDecoration
import com.sharonovnik.homework_2.ui.PermissionResolver
import com.sharonovnik.homework_2.ui.ScrollToTopListener
import com.sharonovnik.homework_2.ui.main.MainActivity
import com.sharonovnik.homework_2.ui.posts.PostAdapter
import com.sharonovnik.homework_2.ui.posts.PostRow
import com.sharonovnik.homework_2.ui.posts.PostType
import com.sharonovnik.homework_2.ui.posts.preview.PostPreviewActivity
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_posts.posts
import javax.inject.Inject


class FavoritesFragment : Fragment(), MainActivity.OnFavoritePostAddedListener {
    private var adapter: PostAdapter? = null
    @Inject
    lateinit var imagesManager: ImagesManager
    private lateinit var permissionResolver: PermissionResolver

    companion object {

        @JvmStatic
        fun newInstance() = FavoritesFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as NewsFeedApplication).authComponent?.inject(this)
        (context as MainActivity).scrollToTopListener = object : ScrollToTopListener {
            override fun onScrollToTopClicked() {
                posts.smoothScrollToPosition(0)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        permissionResolver = PermissionResolver(this)
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }

    override fun onFavoritePostAdded(posts: List<PostRow>) {
        val postsWithoutHeaders = posts.filter { it.postType != PostType.HEADER }
        adapter?.posts = postsWithoutHeaders
    }

    fun scrollUp() {
        posts.smoothScrollToPosition(0)
    }

    private fun initRecyclerView() {
        posts.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter({ _, _ -> }, onClickListener = { postRow ->
            requireContext().openActivity(PostPreviewActivity::class.java) {
                if (postRow.post != null) {
                    putInt(PostPreviewActivity.EXTRA_POST_PREVIEW_ID_KEY, postRow.post!!.id)
                    putLong(PostPreviewActivity.EXTRA_OWNER_ID_KEY, -postRow.post!!.source)
                }
            }
        }, onOptionsClickListener = ::showPopupMenu)
        posts.adapter = adapter
        posts.addItemDecoration(
            HeaderSimpleDividerDecoration(requireContext(), ContextCompat.getDrawable(
                requireContext(), R.drawable.post_divider_drawable))
        )
        posts.addItemDecoration(EqualSpaceItemDecoration(requireContext().dpToPx(10)))
    }

    private fun showPopupMenu(view: View, drawable: Drawable) {
        view.showOptionsPopupWindow(doOnSave = {
            permissionResolver.obtainPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                granted = {
                    imagesManager.saveImageToGallery(drawable) {
                        root.showSnackbar(getString(R.string.image_downloaded_into_pictures_folder))
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
}