package com.sharonovnik.vkclient.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCommentResponse(
    @SerialName("comment_id")
    val commentId: Int = -1
)