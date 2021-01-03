package com.sharonovnik.vkclient.data.network.response

import com.sharonovnik.vkclient.domain.model.Group
import com.sharonovnik.vkclient.domain.model.Post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsFeedResponse (
    @SerialName("items") val posts: List<Post> = listOf(),
    @SerialName("groups") val groups: List<Group> = listOf(),
)