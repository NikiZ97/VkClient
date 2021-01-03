package com.sharonovnik.vkclient.ui.di.components

import com.sharonovnik.vkclient.ui.di.modules.PostPreviewViewModelModule
import com.sharonovnik.vkclient.ui.di.modules.RepositoryModule
import com.sharonovnik.vkclient.ui.di.modules.ViewModelFactoryModule
import com.sharonovnik.vkclient.ui.di.scopes.ActivityScope
import com.sharonovnik.vkclient.ui.posts.preview.PostPreviewActivity
import dagger.Component

@Component(
    dependencies = [AuthComponent::class],
    modules = [RepositoryModule::class,
        PostPreviewViewModelModule::class,
        ViewModelFactoryModule::class]
)
@ActivityScope
interface ActivityComponent {
    fun inject(postPreviewActivity: PostPreviewActivity)
}