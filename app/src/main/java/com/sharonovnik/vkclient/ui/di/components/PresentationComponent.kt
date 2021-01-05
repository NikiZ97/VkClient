package com.sharonovnik.vkclient.ui.di.components

import com.sharonovnik.vkclient.ui.di.modules.ViewModelFactoryModule
import com.sharonovnik.vkclient.ui.di.modules.ViewModelModule
import com.sharonovnik.vkclient.ui.di.scopes.PresentationScope
import com.sharonovnik.vkclient.ui.main.MainActivity
import com.sharonovnik.vkclient.ui.posts.favorite.FavoritesFragment
import com.sharonovnik.vkclient.ui.posts.news.PostsFragment
import com.sharonovnik.vkclient.ui.posts.preview.PostPreviewActivity
import com.sharonovnik.vkclient.ui.profile.ProfileFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelFactoryModule::class, ViewModelModule::class])
@PresentationScope
interface PresentationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(postsFragment: PostsFragment)
    fun inject(favoritesFragment: FavoritesFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(postPreviewActivity: PostPreviewActivity)
}