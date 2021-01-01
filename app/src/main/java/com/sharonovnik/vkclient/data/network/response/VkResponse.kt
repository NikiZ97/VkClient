package com.sharonovnik.vkclient.data.network.response

import com.google.gson.annotations.SerializedName

data class VkResponse<T>(
    @SerializedName("response")
    val response: T
)