package com.sharonovnik.vkclient.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.sharonovnik.vkclient.domain.model.Attachment
import com.sharonovnik.vkclient.domain.model.Comments
import com.sharonovnik.vkclient.domain.model.Likes

@Entity(tableName = PostEntity.TABLE_NAME)
data class PostEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "source_id") val source: Long,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "attachments") val attachments: List<Attachment?>?,
    @ColumnInfo(name = "text") val text: String?,
    @ColumnInfo(name = "likes") val likes: Likes?,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "owner_id") val ownerId: Long,
    @ColumnInfo(name = "comments") val comments: Comments?,
    @ColumnInfo(name = "is_user_wall_post") val isUserWallPost: Boolean = false,
    @ColumnInfo(name = "user_like_timestamp") val userLikeTimestamp: Long? = null
) {
    companion object {
        const val TABLE_NAME = "Post"
    }

    @Ignore var displayDate: String = ""
    @Ignore var publicName: String? = null
    @Ignore var avatar: String? = null
}