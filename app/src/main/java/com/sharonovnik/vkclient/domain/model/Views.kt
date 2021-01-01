package com.sharonovnik.vkclient.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Views (
    @SerializedName("count") val count : Int
) : Serializable