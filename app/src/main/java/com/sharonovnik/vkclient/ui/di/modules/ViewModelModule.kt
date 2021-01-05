package com.sharonovnik.vkclient.ui.di.modules

import androidx.lifecycle.ViewModel
import com.sharonovnik.vkclient.ui.main.MainViewModel
import com.sharonovnik.vkclient.ui.posts.news.PostsViewModel
import com.sharonovnik.vkclient.ui.posts.preview.PostPreviewViewModel
import com.sharonovnik.vkclient.ui.profile.ProfileViewModel
import com.sharonovnik.vkclient.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostsViewModel::class)
    internal abstract fun postsViewModel(viewModel: PostsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostPreviewViewModel::class)
    internal abstract fun postPreviewViewModel(viewModel: PostPreviewViewModel): ViewModel
}