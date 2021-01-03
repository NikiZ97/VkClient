package com.sharonovnik.vkclient.data.network.response

import com.sharonovnik.vkclient.domain.model.Comments
import com.sharonovnik.vkclient.domain.model.Likes
import com.sharonovnik.vkclient.domain.model.Photo
import com.sharonovnik.vkclient.domain.model.Views
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserWallResponse(
    @SerialName("count")
    val count: Int = -1,

    @SerialName("items")
    val wallItem: List<UserWallItem> = listOf(),

    @SerialName("profiles")
    val profiles: List<UserWallProfile> = listOf()
)

@Serializable
data class UserWallItem(
    @SerialName("id")
    val id: Int = -1,

    @SerialName("from_id")
    val fromId: Long = -1,

    @SerialName("owner_id")
    val ownerId: Long = -1,

    @SerialName("date")
    val date: Long = -1,

    @SerialName("post_type")
    val postType: String = "",

    @SerialName("text")
    val text: String = "",

    @SerialName("can_delete")
    val canDelete: Int = -1,

    @SerialName("attachments")
    val attachments: List<UserWallAttachment>? = null,

    @SerialName("post_source")
    val postSource: PostSource? = null,

    @SerialName("comments")
    val comments: Comments? = null,

    @SerialName("likes")
    val likes: Likes? = null,

    @SerialName("views")
    val views: Views? = null,

    @SerialName("is_favorite")
    val isFavorite: Boolean = false,
)

@Serializable
data class UserWallProfile(
    @SerialName("first_name")
    val firstName: String = "",

    @SerialName("last_name")
    val last_name: String = "",

    @SerialName("id")
    val id: Long = -1,

    @SerialName("photo_100")
    val photo: String = "",
)

@Serializable
data class UserWallAttachment(
    @SerialName("type")
    val type: String = "",

    @SerialName("photo")
    val photo: Photo? = null,
)

@Serializable
data class PostSource(
    @SerialName("type")
    val type: String = "",

    @SerialName("platform")
    val platform: String = "",
)