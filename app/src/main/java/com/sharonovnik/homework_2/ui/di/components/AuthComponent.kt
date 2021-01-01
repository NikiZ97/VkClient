package com.sharonovnik.homework_2.ui.di.components

import android.content.Context
import com.sharonovnik.homework_2.data.local.dao.PostsDao
import com.sharonovnik.homework_2.data.mapper.CommentMapper
import com.sharonovnik.homework_2.data.network.ApiService
import com.sharonovnik.homework_2.ui.di.modules.*
import com.sharonovnik.homework_2.ui.di.scopes.AuthScope
import com.sharonovnik.homework_2.ui.main.MainActivity
import com.sharonovnik.homework_2.ui.posts.favorite.FavoritesFragment
import com.sharonovnik.homework_2.ui.posts.news.PostsFragment
import com.sharonovnik.homework_2.ui.profile.ProfileFragment
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