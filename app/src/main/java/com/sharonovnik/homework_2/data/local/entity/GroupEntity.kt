package com.sharonovnik.homework_2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = GroupEntity.TABLE_NAME)
data class GroupEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id : Long,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "screen_name") val screenName : String,
    @ColumnInfo(name = "photo_50") val photo50 : String
) {
    companion object {
        const val TABLE_NAME = "GroupTable"
    }
}