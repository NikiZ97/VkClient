package com.sharonovnik.homework_2.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Post(
    @SerializedName("source_id") val sourceId: Long,
    @SerializedName("post_id") val postId: Int,
    @SerializedName("date") val date: Int,
    @SerializedName("attachments") val attachments: List<Attachment?>?,
    @SerializedName("text") val text: String,
    @SerializedName("likes") val likes: Likes?,
    @SerializedName("is_favorite") var isFavorite: Boolean,
    @SerializedName("owner_id") val ownerId: Long,
    @SerializedName("comments") val comments: Comments?,
    var publicName: String?,
    var avatar: String?,
    var displayDate: String,
) : Serializable

data class Attachment(
    @SerializedName("photo") val photo: Photo?
) : Serializable

data class Comments(
    @SerializedName("count") val count: Long,
    @SerializedName("can_post") val canPost: Int,
) : Serializable