package com.sharonovnik.homework_2.ui.di.modules

import androidx.lifecycle.ViewModel
import com.sharonovnik.homework_2.ui.posts.news.PostsViewModel
import com.sharonovnik.homework_2.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PostsViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PostsViewModel::class)
    internal abstract fun postsViewModel(viewModel: PostsViewModel): ViewModel
}
