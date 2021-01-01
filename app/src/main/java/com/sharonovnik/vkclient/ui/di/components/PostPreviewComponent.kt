package com.sharonovnik.vkclient.ui.di.components

import com.sharonovnik.vkclient.ui.di.modules.PostPreviewViewModelModule
import com.sharonovnik.vkclient.ui.di.modules.RepositoryModule
import com.sharonovnik.vkclient.ui.di.modules.ViewModelFactoryModule
import com.sharonovnik.vkclient.ui.di.scopes.PostPreviewScope
import com.sharonovnik.vkclient.ui.posts.preview.PostPreviewActivity
import dagger.Component

@Component(
    dependencies = [AuthComponent::class],
    modules = [RepositoryModule::class,
        PostPreviewViewModelModule::class,
        ViewModelFactoryModule::class]
)
@PostPreviewScope
interface PostPreviewComponent {
    fun inject(postPreviewActivity: PostPreviewActivity)
}