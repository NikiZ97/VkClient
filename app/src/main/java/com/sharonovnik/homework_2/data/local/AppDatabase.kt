package com.sharonovnik.homework_2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sharonovnik.homework_2.data.local.dao.PostsDao
import com.sharonovnik.homework_2.data.local.dao.ProfileDao
import com.sharonovnik.homework_2.data.local.entity.GroupEntity
import com.sharonovnik.homework_2.data.local.entity.PostEntity
import com.sharonovnik.homework_2.data.local.entity.ProfileEntity

@Database(entities = [PostEntity::class, GroupEntity::class, ProfileEntity::class], version = 1, exportSchema = false)
@TypeConverters(VkTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postsDao(): PostsDao
    abstract fun profileDao(): ProfileDao
}