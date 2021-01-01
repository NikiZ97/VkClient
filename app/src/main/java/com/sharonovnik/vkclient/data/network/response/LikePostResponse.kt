package com.sharonovnik.vkclient.data.network.response

import com.google.gson.annotations.SerializedName

data class LikesResponse (
    @SerializedName("likes")
    val likes: Int?
)