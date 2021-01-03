package com.sharonovnik.vkclient.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentsResponse(
    @SerialName("count")
    val count: Int = -1,
    @SerialName("items")
    val comments: List<PostComment> = listOf(),
    @SerialName("profiles")
    val profiles: List<PostProfile> = listOf(),
//    @SerialName("groups")
//    val groups: List<PostCommunity>
)

@Serializable
data class PostComment(
    @SerialName("id")
    val id: Int = -1,
    @SerialName("from_id")
    val fromId: Int = -1,
    @SerialName("date")
    val date: Long = -1,
    @SerialName("text")
    val text: String = "",
)

@Serializable
data class PostProfile(
    @SerialName("id")
    val id: Int = -1,
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("last_name")
    val lastName: String = "",
    @SerialName("photo_50")
    val photo50: String = ""
)

@Serializable
data class Comment(
    val comment: PostComment? = null,
    val profile: PostProfile? = null,
)