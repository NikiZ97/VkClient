package com.sharonovnik.homework_2.ui.di.modules

import com.sharonovnik.homework_2.data.mapper.CommentMapper
import com.sharonovnik.homework_2.data.mapper.PostsDtoToEntityMapper
import com.sharonovnik.homework_2.data.mapper.ProfileInfoMapper
import com.sharonovnik.homework_2.data.mapper.UserWallMapper
import com.sharonovnik.homework_2.ui.di.scopes.AuthScope
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