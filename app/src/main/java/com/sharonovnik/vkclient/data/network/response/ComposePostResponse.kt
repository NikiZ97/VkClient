package com.sharonovnik.vkclient.data.network.response

import com.google.gson.annotations.SerializedName

data class ComposePostResponse(
    @SerializedName("post_id")
    val postId: Int
)