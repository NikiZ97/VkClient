package com.sharonovnik.vkclient.domain.model

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Photo (
    @SerialName("album_id") val albumId : Int = -1,
    @SerialName("date") val date : Int = -1,
    @SerialName("id") val id : Int = -1,
    @SerialName("owner_id") val ownerId : Int = -1,
    @SerialName("has_tags") val hasTags : Boolean = false,
    @SerialName("access_key") val accesskey : String = "",
    @SerialName("sizes") val sizes: List<Size> = listOf(),
    @SerialName("post_id") val postId : Int = -1,
    @SerialName("text") val text : String = "",
    @SerialName("user_id") val userId : Int = -1,
    @SerialName("width") val width : Int = -1
): Serializable

@kotlinx.serialization.Serializable
data class Size(
    @SerialName("height") val height: Int = -1,
    @SerialName("url") val url: String = "",
    @SerialName("width") val width: Int = -1,
    @SerialName("type") val type: String = "",
): Serializable