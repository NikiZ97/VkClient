package com.sharonovnik.homework_2.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.sharonovnik.homework_2.data.local.entity.GroupEntity
import com.sharonovnik.homework_2.data.local.entity.PostEntity

data class PostWithGroup(
    @Embedded val post: PostEntity,
    @Relation(
        parentColumn = "source_id",
        entityColumn = "id"
    )
    val group: GroupEntity?
)
