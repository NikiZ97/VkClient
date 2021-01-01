package com.sharonovnik.homework_2.ui.di.components

import com.sharonovnik.homework_2.ui.di.modules.PostPreviewViewModelModule
import com.sharonovnik.homework_2.ui.di.modules.RepositoryModule
import com.sharonovnik.homework_2.ui.di.modules.ViewModelFactoryModule
import com.sharonovnik.homework_2.ui.di.scopes.PostPreviewScope
import com.sharonovnik.homework_2.ui.posts.preview.PostPreviewActivity
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