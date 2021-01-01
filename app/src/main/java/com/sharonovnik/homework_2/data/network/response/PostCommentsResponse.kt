package com.sharonovnik.homework_2.data.network.response

import com.google.gson.annotations.SerializedName


data class CommentsResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("items")
    val comments: List<PostComment>,
    @SerializedName("profiles")
    val profiles: List<PostProfile>,
//    @SerializedName("groups")
//    val groups: List<PostCommunity>
)

data class PostComment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("from_id")
    val fromId: Int,
    @SerializedName("date")
    val date: Long,
    @SerializedName("text")
    val text: String,
)

data class PostProfile(
    @SerializedName("id")
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("photo_50")
    val photo50: String
)

data class Comment(
    val comment: PostComment,
    val profile: PostProfile
)