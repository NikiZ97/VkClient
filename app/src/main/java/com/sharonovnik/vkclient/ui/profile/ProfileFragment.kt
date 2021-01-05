package com.sharonovnik.vkclient.ui.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.sharonovnik.vkclient.R
import com.sharonovnik.vkclient.data.local.entity.ProfileEntity
import com.sharonovnik.vkclient.data.local.storage.ImagesManager
import com.sharonovnik.vkclient.openActivity
import com.sharonovnik.vkclient.showOptionsPopupWindow
import com.sharonovnik.vkclient.showSnackbar
import com.sharonovnik.vkclient.ui.DateTimeConverter
import com.sharonovnik.vkclient.ui.HeaderSimpleDividerDecoration
import com.sharonovnik.vkclient.ui.PermissionResolver
import com.sharonovnik.vkclient.ui.SwipeTouchCallback
import com.sharonovnik.vkclient.ui.base.BaseFragment
import com.sharonovnik.vkclient.ui.posts.PostAdapter
import com.sharonovnik.vkclient.ui.posts.PostRow
import com.sharonovnik.vkclient.ui.posts.preview.PostPreviewActivity
import com.sharonovnik.vkclient.ui.profile.compose.ComposePostDialogFragment
import kotlinx.android.synthetic.main.fragment_posts.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.user_info_layout.*
import javax.inject.Inject


class ProfileFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var adapter: PostAdapter
    @Inject
    lateinit var imagesManager: ImagesManager
    private lateinit var permissionResolver: PermissionResolver

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injector?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        permissionResolver = PermissionResolver(this)
        childFragmentManager.setFragmentResultListener(
            ComposePostDialogFragment.KEY_POST_RESULT, viewLifecycleOwner
        ) { _, bundle ->
            val text = bundle.getString(ComposePostDialogFragment.EXTRA_POST_TEXT)
            if (text != null) {
                profileViewModel.input.accept(ProfileAction.ComposePost(text))
            }
        }
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        profileViewModel.getAllUserInfo()
        initPostsRecyclerView()
        subscribeUi()

        refresh.setOnRefreshListener {
            profileViewModel.getAllUserInfo()
        }

        profileFab.setOnClickListener {
            val composeDialog = ComposePostDialogFragment.newInstance()
            composeDialog.show(childFragmentManager, ComposePostDialogFragment.javaClass.name)
        }

        motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout, p1: Int) {
                refresh.isEnabled = motionLayout.progress == 0f
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }

    private fun initPostsRecyclerView() {
        userPosts.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter(onLikeChangedListener = { _, _ -> /* not implemented */ },
            onClickListener = ::openPreviewActivity, onOptionsClickListener = ::showPopupMenu)
        userPosts.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(SwipeTouchCallback(adapter))
        itemTouchHelper.attachToRecyclerView(posts)
        userPosts.addItemDecoration(
            HeaderSimpleDividerDecoration(
                requireContext(), ContextCompat.getDrawable(
                    requireContext(), R.drawable.post_divider_drawable
                )
            )
        )
        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                userPosts.scrollToPosition(0)
            }
        })
    }

    private fun openPreviewActivity(postRow: PostRow) {
        requireContext().openActivity(PostPreviewActivity::class.java) {
            if (postRow.post != null) {
                putInt(PostPreviewActivity.EXTRA_POST_PREVIEW_ID_KEY, postRow.post!!.id)
                putLong(PostPreviewActivity.EXTRA_OWNER_ID_KEY, postRow.post!!.source)
            }
        }
    }

    private fun showPopupMenu(view: View, drawable: Drawable) {
        view.showOptionsPopupWindow(doOnSave = {
            permissionResolver.obtainPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                granted = {
                    imagesManager.saveImageToGallery(drawable)
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

    private fun subscribeUi() {
        profileViewModel.getState()
            .observe(viewLifecycleOwner, {
                render(it)
            })
    }

    private fun render(state: ProfileState) {
        state.userWall?.let {
            adapter.posts = it
        }
        refresh.isRefreshing = state.isLoading
        state.profileInfo?.let {
            setProfileInfo(it)
        }
        if (state.error != null) {
            if (isVisible) {
                refresh.showSnackbar(getString(R.string.error_loading_profile))
            }
        }
    }

    private fun setProfileInfo(profileInfo: ProfileEntity) {
        Glide.with(requireContext())
            .load(profileInfo.photo)
            .transform(CircleCrop())
            .into(profileImage)
        with(name) {
            text = profileInfo.firstName
            append(" ")
            append(profileInfo.lastName)
        }
        domainName.text = getString(R.string.domain_name_prefix)
        domainName.append(profileInfo.domain)
        with(lastSeen) {
            text = getString(R.string.last_seen_prefix)
            append(DateTimeConverter.getDateFromUnixTime(profileInfo.lastSeen.time))
        }
        cityIcon.isVisible = profileInfo.city != null
        cityName.isVisible = profileInfo.city != null
        if (profileInfo.city != null) {
            cityName.text = profileInfo.city.title
        }
        followersName.text = profileInfo.followersCount.toString()
        followersName.append(getString(R.string.followers_postfix))
        val isUniversityExists = profileInfo.universityName.isNotEmpty()
        universityName.isVisible = isUniversityExists
        educationIcon.isVisible = isUniversityExists
        universityName.text = profileInfo.universityName

        showMore.setOnClickListener {
            profileInfo.infoItems?.let {
                val userInfoDialog = UserInfoBottomSheetDialog.newInstance(it)
                userInfoDialog.show(childFragmentManager, UserInfoBottomSheetDialog.javaClass.name)
            }
        }
    }
}