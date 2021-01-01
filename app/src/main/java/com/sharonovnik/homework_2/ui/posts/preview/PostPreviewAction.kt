package com.sharonovnik.homework_2.ui.posts.preview

import com.sharonovnik.homework_2.data.local.PostWithGroup
import com.sharonovnik.homework_2.data.network.response.Comment

sealed class PostPreviewAction {
    data class GetLocalPostById(val id: Int) : PostPreviewAction()
    data class PostPreviewLoaded(val post: PostWithGroup) : PostPreviewAction()
    data class ErrorLoadPost(val throwable: Throwable) : PostPreviewAction()
    data class GetPostComments(
        val ownerId: Long, val postId: Int, val portion: Int = 30
    ) : PostPreviewAction()
    data class ErrorLoadPostComments(val throwable: Throwable) : PostPreviewAction()
    data class PostCommentsLoaded(val comments: List<Comment>): PostPreviewAction()
    data class CreateComment(val ownerId: Long, val postId: Int, val message: String): PostPreviewAction()
    data class CommentCreated(val comment: Comment): PostPreviewAction()
    data class ErrorCreateComment(val throwable: Throwable): PostPreviewAction()
}