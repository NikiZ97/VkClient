package com.sharonovnik.vkclient.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Photo (
    @SerializedName("album_id") val albumId : Int,
    @SerializedName("date") val date : Int,
    @SerializedName("id") val id : Int,
    @SerializedName("owner_id") val ownerId : Int,
    @SerializedName("has_tags") val hasTags : Boolean,
    @SerializedName("access_key") val accesskey : String,
    @SerializedName("sizes") val sizes: List<Size>,
    @SerializedName("post_id") val postId : Int,
    @SerializedName("text") val text : String,
    @SerializedName("user_id") val userId : Int,
    @SerializedName("width") val width : Int
): Serializable

data class Size(
    @SerializedName("height") val height: Int,
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("type") val type: String,
): Serializable