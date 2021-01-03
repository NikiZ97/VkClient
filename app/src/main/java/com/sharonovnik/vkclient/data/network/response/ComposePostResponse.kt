package com.sharonovnik.vkclient.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComposePostResponse(
    @SerialName("post_id")
    val postId: Int = -1
)