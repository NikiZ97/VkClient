package com.sharonovnik.vkclient.ui.di.modules

import androidx.lifecycle.ViewModel
import com.sharonovnik.vkclient.ui.di.scopes.PostPreviewScope
import com.sharonovnik.vkclient.ui.posts.preview.PostPreviewViewModel
import com.sharonovnik.vkclient.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PostPreviewViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PostPreviewViewModel::class)
    @PostPreviewScope
    internal abstract fun postPreviewViewModel(viewModel: PostPreviewViewModel): ViewModel
}