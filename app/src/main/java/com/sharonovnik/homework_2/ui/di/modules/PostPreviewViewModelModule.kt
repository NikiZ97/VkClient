package com.sharonovnik.homework_2.ui.di.modules

import androidx.lifecycle.ViewModel
import com.sharonovnik.homework_2.ui.di.scopes.PostPreviewScope
import com.sharonovnik.homework_2.ui.posts.preview.PostPreviewViewModel
import com.sharonovnik.homework_2.ui.viewmodel.ViewModelKey
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