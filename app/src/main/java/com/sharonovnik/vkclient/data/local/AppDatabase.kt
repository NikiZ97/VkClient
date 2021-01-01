package com.sharonovnik.vkclient.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sharonovnik.vkclient.data.local.dao.PostsDao
import com.sharonovnik.vkclient.data.local.dao.ProfileDao
import com.sharonovnik.vkclient.data.local.entity.GroupEntity
import com.sharonovnik.vkclient.data.local.entity.PostEntity
import com.sharonovnik.vkclient.data.local.entity.ProfileEntity

@Database(entities = [PostEntity::class, GroupEntity::class, ProfileEntity::class], version = 1, exportSchema = false)
@TypeConverters(VkTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postsDao(): PostsDao
    abstract fun profileDao(): ProfileDao
}