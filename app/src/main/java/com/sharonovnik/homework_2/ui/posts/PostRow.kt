package com.sharonovnik.homework_2.ui.posts

import com.sharonovnik.homework_2.data.local.entity.PostEntity

sealed class PostRow(val postType: PostType) {
    abstract val post: PostEntity?

    data class TextBody(override val post: PostEntity): PostRow(PostType.TEXT_BODY)
    data class Body(override val post: PostEntity): PostRow(PostType.BODY)
    data class Header(val date: String, override val post: PostEntity?): PostRow(PostType.HEADER)
}