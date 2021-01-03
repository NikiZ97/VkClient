package com.sharonovnik.vkclient.ui.di.modules

import com.sharonovnik.vkclient.data.repository.PostPreviewRepositoryImpl
import com.sharonovnik.vkclient.data.repository.PostsRepositoryImpl
import com.sharonovnik.vkclient.data.repository.ProfileRepositoryImpl
import com.sharonovnik.vkclient.domain.repository.PostPreviewRepository
import com.sharonovnik.vkclient.domain.repository.PostsRepository
import com.sharonovnik.vkclient.domain.repository.ProfileRepository
import com.sharonovnik.vkclient.ui.di.scopes.ActivityScope
import com.sharonovnik.vkclient.ui.di.scopes.AuthScope
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    @AuthScope
    abstract fun providePostsRepository(postsRepository: PostsRepositoryImpl): PostsRepository

    @Binds
    @ActivityScope
    abstract fun providePostsPreviewRepository(postPreviewRepository: PostPreviewRepositoryImpl)
            : PostPreviewRepository

    @Binds
    @AuthScope
    abstract fun provideProfileRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository
}