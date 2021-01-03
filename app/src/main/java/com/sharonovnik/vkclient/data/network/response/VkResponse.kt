package com.sharonovnik.vkclient.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VkResponse<T>(
    @SerialName("response")
    val response: T
)