package com.sharonovnik.vkclient.domain.model

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Views (
    @SerialName("count") val count : Int = -1
) : Serializable