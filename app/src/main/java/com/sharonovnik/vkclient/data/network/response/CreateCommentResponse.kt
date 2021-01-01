package com.sharonovnik.vkclient.data.network.response

import com.google.gson.annotations.SerializedName

data class CreateCommentResponse(
    @SerializedName("comment_id")
    val commentId: Int
)