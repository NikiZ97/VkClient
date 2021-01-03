package com.sharonovnik.vkclient.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    @SerialName("id") val id : Long = -1,
    @SerialName("name") val name : String = "",
    @SerialName("screen_name") val screenName : String = "",
    @SerialName("photo_50") val photo50 : String = ""
)