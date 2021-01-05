package com.sharonovnik.vkclient.ui.di.components

import com.sharonovnik.vkclient.ui.di.modules.*
import com.sharonovnik.vkclient.ui.di.scopes.PresentationScope
import com.sharonovnik.vkclient.ui.main.MainActivity
import com.sharonovnik.vkclient.ui.posts.favorite.FavoritesFragment
import com.sharonovnik.vkclient.ui.posts.news.PostsFragment
import com.sharonovnik.vkclient.ui.posts.preview.PostPreviewActivity
import com.sharonovnik.vkclient.ui.profile.ProfileFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [PresentationModule::class,
        PostPreviewViewModelModule::class,
        ViewModelFactoryModule::class,
        PostsViewModelModule::class,
        MainViewModelModule::class,
        ProfileViewModelModule::class])
@PresentationScope
interface PresentationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(postsFragment: PostsFragment)
    fun inject(favoritesFragment: FavoritesFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(postPreviewActivity: PostPreviewActivity)
}