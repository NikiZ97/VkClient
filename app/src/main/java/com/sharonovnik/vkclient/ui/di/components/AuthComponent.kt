package com.sharonovnik.vkclient.ui.di.components

import android.content.Context
import com.sharonovnik.vkclient.data.local.dao.PostsDao
import com.sharonovnik.vkclient.data.mapper.CommentMapper
import com.sharonovnik.vkclient.data.network.ApiService
import com.sharonovnik.vkclient.ui.di.modules.*
import com.sharonovnik.vkclient.ui.di.scopes.AuthScope
import com.sharonovnik.vkclient.ui.main.MainActivity
import com.sharonovnik.vkclient.ui.posts.favorite.FavoritesFragment
import com.sharonovnik.vkclient.ui.posts.news.PostsFragment
import com.sharonovnik.vkclient.ui.profile.ProfileFragment
import dagger.Component

@Component(dependencies = [AppComponent::class],
    modules = [DatabaseModule::class,
        NetworkModule::class,
        MapperModule::class,
        RepositoryModule::class,
        ViewModelFactoryModule::class,
        PostsViewModelModule::class,
        MainViewModelModule::class,
        ProfileViewModelModule::class]
)
@AuthScope
interface AuthComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(postsFragment: PostsFragment)
    fun inject(favoritesFragment: FavoritesFragment)
    fun inject(profileFragment: ProfileFragment)
    fun postDao(): PostsDao
    fun apiService(): ApiService
    fun commentMapper(): CommentMapper
    fun context(): Context
}