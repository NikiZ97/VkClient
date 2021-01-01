package com.sharonovnik.homework_2.domain.model

import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("id") val id : Long,
    @SerializedName("name") val name : String,
    @SerializedName("screen_name") val screenName : String,
    @SerializedName("photo_50") val photo50 : String
)