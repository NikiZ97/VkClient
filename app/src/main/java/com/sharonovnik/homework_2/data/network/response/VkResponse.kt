package com.sharonovnik.homework_2.data.network.response

import com.google.gson.annotations.SerializedName

data class VkResponse<T>(
    @SerializedName("response")
    val response: T
)