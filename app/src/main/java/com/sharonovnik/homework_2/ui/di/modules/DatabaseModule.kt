package com.sharonovnik.homework_2.ui.di.modules

import android.content.Context
import androidx.room.Room
import com.sharonovnik.homework_2.data.local.AppDatabase
import com.sharonovnik.homework_2.data.local.dao.PostsDao
import com.sharonovnik.homework_2.data.local.dao.ProfileDao
import com.sharonovnik.homework_2.ui.di.scopes.AuthScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    @AuthScope
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "news_feed_database")
            .build()
    }

    @Provides
    @AuthScope
    fun providePostsDao(database: AppDatabase): PostsDao {
        return database.postsDao()
    }

    @Provides
    @AuthScope
    fun provideProfileDao(database: AppDatabase): ProfileDao {
        return database.profileDao()
    }
}