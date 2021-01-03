package com.sharonovnik.vkclient.data.network.response

import com.google.gson.annotations.SerializedName
import com.sharonovnik.vkclient.domain.model.Group
import com.sharonovnik.vkclient.domain.model.Post

data class NewsFeedResponse (
    @SerializedName("items") val posts: List<Post>,
    @SerializedName("groups") val groups: List<Group>,
)