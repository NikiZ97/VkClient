package com.sharonovnik.homework_2.data.network.response

import com.google.gson.annotations.SerializedName

data class LikesResponse (
    @SerializedName("likes")
    val likes: Int?
)