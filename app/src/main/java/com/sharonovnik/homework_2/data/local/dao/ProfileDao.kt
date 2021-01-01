package com.sharonovnik.homework_2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sharonovnik.homework_2.data.local.entity.ProfileEntity
import io.reactivex.Single

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(profile: ProfileEntity)

    @Query("SELECT * FROM ${ProfileEntity.TABLE_NAME}")
    fun getCurrentUser(): Single<ProfileEntity>
}