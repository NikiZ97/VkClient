package com.sharonovnik.homework_2.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Likes (
    @SerializedName("count") var count : Int,
    @SerializedName("user_likes") var userLikes : Int
) : Serializable