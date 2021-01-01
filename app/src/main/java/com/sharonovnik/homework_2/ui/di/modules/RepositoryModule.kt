package com.sharonovnik.homework_2.ui.di.modules

import com.sharonovnik.homework_2.data.repository.PostPreviewRepositoryImpl
import com.sharonovnik.homework_2.data.repository.PostsRepositoryImpl
import com.sharonovnik.homework_2.data.repository.ProfileRepositoryImpl
import com.sharonovnik.homework_2.domain.repository.PostPreviewRepository
import com.sharonovnik.homework_2.domain.repository.PostsRepository
import com.sharonovnik.homework_2.domain.repository.ProfileRepository
import com.sharonovnik.homework_2.ui.di.scopes.AuthScope
import com.sharonovnik.homework_2.ui.di.scopes.PostPreviewScope
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    @AuthScope
    abstract fun providePostsRepository(postsRepository: PostsRepositoryImpl): PostsRepository

    @Binds
    @PostPreviewScope
    abstract fun providePostsPreviewRepository(postPreviewRepository: PostPreviewRepositoryImpl)
            : PostPreviewRepository

    @Binds
    @AuthScope
    abstract fun provideProfileRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository
}