package com.sharonovnik.vkclient.domain.model

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Likes (
    @SerialName("count") var count : Int = -1,
    @SerialName("user_likes") var userLikes : Int = -1
) : Serializable