package com.sharonovnik.vkclient.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.sharonovnik.vkclient.data.local.entity.GroupEntity
import com.sharonovnik.vkclient.data.local.entity.PostEntity

data class PostWithGroup(
    @Embedded val post: PostEntity,
    @Relation(
        parentColumn = "source_id",
        entityColumn = "id"
    )
    val group: GroupEntity?
)
