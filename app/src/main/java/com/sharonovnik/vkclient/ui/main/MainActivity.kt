package com.sharonovnik.vkclient.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sharonovnik.vkclient.NewsFeedApplication
import com.sharonovnik.vkclient.R
import com.sharonovnik.vkclient.doIfVisible
import com.sharonovnik.vkclient.domain.repository.PostsRepository
import com.sharonovnik.vkclient.ui.ScrollToTopListener
import com.sharonovnik.vkclient.ui.base.BaseActivity
import com.sharonovnik.vkclient.ui.posts.PostRow
import com.sharonovnik.vkclient.ui.posts.PostsAction
import com.sharonovnik.vkclient.ui.posts.favorite.FavoritesFragment
import com.sharonovnik.vkclient.ui.posts.news.PostsFragment
import com.sharonovnik.vkclient.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import javax.inject.Inject

class MainActivity : BaseActivity(), PostsFragment.OnPostLikeStateChangedListener, PostsFragment.OnDataRefreshedListener {
    private var postsFragment = PostsFragment.newInstance()
    private var favoritesFragment = FavoritesFragment.newInstance()
    private var profileFragment = ProfileFragment.newInstance()
    private var currentFragment: Fragment = postsFragment
    private var onFavoritePostAddedListener = favoritesFragment as OnFavoritePostAddedListener
    private lateinit var viewModel: MainViewModel
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val handler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var postsRepository: PostsRepository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var app: NewsFeedApplication
    var scrollToTopListener: ScrollToTopListener? = null

    private companion object {
        const val FRAGMENT_TAG_POSTS = "Posts"
        const val FRAGMENT_TAG_FAVORITE = "Favorite"
        const val FRAGMENT_TAG_PROFILE = "Profile"

        private const val FAVORITE_TAB_ORDER_NUMBER = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as NewsFeedApplication
        app.addAuthComponent()
        injector?.inject(this)
        setContentView(R.layout.activity_main)
        setToolbar(currentFragment)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, favoritesFragment, FRAGMENT_TAG_FAVORITE)
                .hide(favoritesFragment)
                .commit()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, profileFragment, FRAGMENT_TAG_PROFILE)
                .hide(profileFragment)
                .commit()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, postsFragment, FRAGMENT_TAG_POSTS)
                .commit()
        }
        initBottomNavigationView()
        registerNetworkCallback()
        setClickListeners()
        subscribeUi()
    }

    private fun initBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.news -> {
                    supportFragmentManager.beginTransaction()
                        .hide(currentFragment)
                        .show(postsFragment)
                        .commit()
                    currentFragment = postsFragment
                    setToolbar(currentFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favorite -> {
                    supportFragmentManager.beginTransaction()
                        .hide(currentFragment)
                        .show(favoritesFragment)
                        .commit()
                    currentFragment = favoritesFragment
                    setToolbar(currentFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .hide(currentFragment)
                        .show(profileFragment)
                        .commit()
                    currentFragment = profileFragment
                    setToolbar(currentFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun setToolbar(currentFragment: Fragment) {
        scrollUp.isVisible = currentFragment !is ProfileFragment
        if (currentFragment is ProfileFragment) {
            toolbarTitle.text = getText(R.string.profile_title)
        } else {
            toolbarTitle.text = getText(R.string.feed_title)
        }
    }

    private fun setClickListeners() {
        scrollUp.setOnClickListener {
            val postsFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_POSTS)
            postsFragment.doIfVisible {
                (postsFragment as PostsFragment).scrollUp()
            }
            val favoritesFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_FAVORITE)
            favoritesFragment.doIfVisible {
                (favoritesFragment as FavoritesFragment).scrollUp()
            }
        }
    }

    private fun registerNetworkCallback() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onLost(network: Network) {
                handler.post {
                    with(internetConnectionLayout) {
                        scaleY = 0F
                        pivotY = 0F
                        isVisible = true
                        animate().setDuration(400)
                            .scaleY(1F)
                        handler.postDelayed({
                            animate().setDuration(400)
                                .scaleY(0F)
                                .withEndAction {
                                    isVisible = false
                                }
                        }, 4000)
                    }
                }
            }
        }
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
    }

    override fun onPostLikeStateChanged() {
        viewModel.mainInput.accept(PostsAction.LoadLocalPosts)
    }

    override fun onDataRefreshed() {
        bottomNavigationView.menu.removeItem(R.id.favorite)
    }

    private fun subscribeUi() {
        viewModel.getState()
            .observe(this, {
                val menu = bottomNavigationView.menu
                if (it.items.isNotEmpty()) {
                    val item = menu.findItem(R.id.favorite)
                    if (item == null) {
                        menu.add(Menu.NONE, R.id.favorite, FAVORITE_TAB_ORDER_NUMBER, getString(R.string.favorite))
                            .setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite))
                    }
                    onFavoritePostAddedListener.onFavoritePostAdded(it.items)
                } else {
                    menu.removeItem(R.id.favorite)
                }
            })
    }

    interface OnFavoritePostAddedListener {
        fun onFavoritePostAdded(posts: List<PostRow>)
    }

    override fun onDestroy() {
        super.onDestroy()
        app.clearAuthComponent()
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .unregisterNetworkCallback(networkCallback)
    }
}
