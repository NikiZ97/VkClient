package com.sharonovnik.vkclient.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Post(
    @SerialName("source_id") val sourceId: Long = -1,
    @SerialName("post_id") val postId: Int = -1,
    @SerialName("date") val date: Int = -1,
    @SerialName("attachments") val attachments: List<Attachment?>? = null,
    @SerialName("text") val text: String = "",
    @SerialName("likes") val likes: Likes? = null,
    @SerialName("is_favorite") var isFavorite: Boolean = false,
    @SerialName("owner_id") val ownerId: Long = -1,
    @SerialName("comments") val comments: Comments? = null,
    @Transient var publicName: String? = null,
    @Transient var avatar: String? = null,
    @Transient var displayDate: String = "",
) : Serializable

@kotlinx.serialization.Serializable
data class Attachment(
    @SerialName("photo") val photo: Photo? = null
) : Serializable

@kotlinx.serialization.Serializable
data class Comments(
    @SerialName("count") val count: Long = -1,
    @SerialName("can_post") val canPost: Int = -1,
) : Serializable