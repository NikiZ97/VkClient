package com.sharonovnik.vkclient.data.network.response

import com.google.gson.annotations.SerializedName

data class VkArrayResponse<T>(
    @SerializedName("response")
    val response: List<T>
)