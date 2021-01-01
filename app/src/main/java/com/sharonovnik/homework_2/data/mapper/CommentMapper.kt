package com.sharonovnik.homework_2.data.mapper

import com.sharonovnik.homework_2.data.network.response.Comment
import com.sharonovnik.homework_2.data.network.response.CommentsResponse

class CommentMapper : Mapper<CommentsResponse, List<Comment>> {
    override fun map(value: CommentsResponse): List<Comment> {
        val comments = value.comments
        val profiles = value.profiles
        val result = mutableListOf<Comment>()
        comments.forEach { comment ->
            val profile = profiles.find { profile -> profile.id == comment.fromId }
            if (profile != null) {
                result.add(Comment(comment, profile))
            }
        }
        return result
    }
}