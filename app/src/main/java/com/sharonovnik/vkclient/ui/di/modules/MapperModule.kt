package com.sharonovnik.vkclient.ui.di.modules

import com.sharonovnik.vkclient.data.mapper.CommentMapper
import com.sharonovnik.vkclient.data.mapper.PostsDtoToEntityMapper
import com.sharonovnik.vkclient.data.mapper.ProfileInfoMapper
import com.sharonovnik.vkclient.data.mapper.UserWallMapper
import com.sharonovnik.vkclient.ui.di.scopes.AuthScope
import dagger.Module
import dagger.Provides

@Module
class MapperModule {
    @Provides
    @AuthScope
    fun providePostsMapper(): PostsDtoToEntityMapper {
        return PostsDtoToEntityMapper()
    }

    @Provides
    @AuthScope
    fun provideCommentsMapper(): CommentMapper {
        return CommentMapper()
    }

    @Provides
    @AuthScope
    fun provideProfileInfoMapper(): ProfileInfoMapper {
        return ProfileInfoMapper()
    }

    @Provides
    @AuthScope
    fun provideUserWallMapper(): UserWallMapper {
        return UserWallMapper()
    }
}