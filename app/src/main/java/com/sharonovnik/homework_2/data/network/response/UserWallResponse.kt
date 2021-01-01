package com.sharonovnik.homework_2.data.network.response

import com.google.gson.annotations.SerializedName
import com.sharonovnik.homework_2.domain.model.Comments
import com.sharonovnik.homework_2.domain.model.Likes
import com.sharonovnik.homework_2.domain.model.Photo
import com.sharonovnik.homework_2.domain.model.Views

data class UserWallResponse(
    @SerializedName("count")
    val count: Int,

    @SerializedName("items")
    val wallItem: List<UserWallItem>,

    @SerializedName("profiles")
    val profiles: List<UserWallProfile>
)

data class UserWallItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("from_id")
    val fromId: Long,

    @SerializedName("owner_id")
    val ownerId: Long,

    @SerializedName("date")
    val date: Long,

    @SerializedName("post_type")
    val postType: String,

    @SerializedName("text")
    val text: String,

    @SerializedName("can_delete")
    val canDelete: Int,

    @SerializedName("attachments")
    val attachments: List<UserWallAttachment>?,

    @SerializedName("post_source")
    val postSource: PostSource,

    @SerializedName("comments")
    val comments: Comments,

    @SerializedName("likes")
    val likes: Likes,

    @SerializedName("views")
    val views: Views,

    @SerializedName("is_favorite")
    val isFavorite: Boolean,
)

data class UserWallProfile(
    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val last_name: String,

    @SerializedName("id")
    val id: Long,

    @SerializedName("photo_100")
    val photo: String,
)

data class UserWallAttachment(
    @SerializedName("type")
    val type: String,

    @SerializedName("photo")
    val photo: Photo?,
)

data class PostSource(
    @SerializedName("type")
    val type: String,

    @SerializedName("platform")
    val platform: String,
)