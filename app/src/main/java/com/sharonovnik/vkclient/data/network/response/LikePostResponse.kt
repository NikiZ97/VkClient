package com.sharonovnik.vkclient.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikesResponse (
    @SerialName("likes")
    val likes: Int? = null
)