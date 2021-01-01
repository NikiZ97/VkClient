package com.sharonovnik.homework_2.data.network.response

import com.google.gson.annotations.SerializedName
import com.sharonovnik.homework_2.domain.model.Group
import com.sharonovnik.homework_2.domain.model.Post

data class NewsFeedResponse (
    @SerializedName("items") val posts: List<Post>,
    @SerializedName("groups") val groups: List<Group>,
)