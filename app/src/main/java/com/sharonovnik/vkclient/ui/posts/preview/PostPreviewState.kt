package com.sharonovnik.vkclient.ui.posts.preview

import com.sharonovnik.vkclient.data.local.entity.PostEntity
import com.sharonovnik.vkclient.data.network.response.Comment

data class PostPreviewState(
    val postPreview: PostEntity? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isEmptyState: Boolean = false,
    val postComments: List<Comment>? = null,
)

internal fun PostPreviewState.reduce(action: PostPreviewAction): PostPreviewState {
    return when (action) {
        is PostPreviewAction.PostPreviewLoaded -> copy(
            postPreview = action.post.post
        )
        is PostPreviewAction.GetLocalPostById -> {
            this
        }
        is PostPreviewAction.ErrorLoadPost -> copy(
            error = action.throwable, postPreview = null, isLoading = false
        )
        is PostPreviewAction.ErrorLoadPostComments -> copy(
            error = action.throwable, postComments = null, isLoading = false
        )
        is PostPreviewAction.GetPostComments -> copy(
            postPreview = null, error = null, isLoading = true,
            isEmptyState = false, postComments = null
        )
        is PostPreviewAction.PostCommentsLoaded -> copy(
            error = null, isLoading = false,
            isEmptyState = false, postComments = action.comments
        )
        is PostPreviewAction.CreateComment -> copy(
            isLoading = true
        )
        is PostPreviewAction.CommentCreated -> copy(
            postComments = postComments?.plus(action.comment)?.sortedByDescending { it.comment.date },
            isLoading = false
        )
        is PostPreviewAction.ErrorCreateComment -> copy(
            error = action.throwable, postComments = null, isLoading = false
        )
    }
}