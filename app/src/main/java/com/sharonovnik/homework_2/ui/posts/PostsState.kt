package com.sharonovnik.homework_2.ui.posts

import com.sharonovnik.homework_2.data.local.entity.PostEntity

data class PostsState(
    val items: List<PostRow> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLikeStateChanged: Boolean = false,
    val postPreview: PostEntity? = null
) {
    override fun toString(): String {
        return "Items size = ${items.size}, " +
                "error = ${error?.message}, isLoading = $isLoading, isRefreshing = $isRefreshing" +
                "isLikeStateChanged = $isLikeStateChanged"
    }
}

internal fun PostsState.reduce(action: PostsAction): PostsState {
    return when (action) {
        is PostsAction.PostsLoaded -> copy(
            items = action.itemsList,
            isLoading = action.itemsList.isEmpty(), isRefreshing = false, error = null
        )
        is PostsAction.LoadPosts -> copy(
            isLoading = true, isRefreshing = false
        )
        is PostsAction.LikePost -> {
            this
        }
        is PostsAction.ErrorLoadPosts -> copy(
            error = action.throwable, isLoading = false, isLikeStateChanged = false,
            isRefreshing = false
        )
        is PostsAction.ErrorLikePost -> copy(
            error = action.throwable, isLoading = false, items = items
        )
        is PostsAction.PostLiked -> copy(
            isLoading = false, isLikeStateChanged = true
        )
        is PostsAction.LoadPostsWithPullToRefresh -> copy(
            isLoading = false, isRefreshing = true, isLikeStateChanged = false, error = null
        )
        is PostsAction.LoadLocalPosts -> copy(
            isLikeStateChanged = false, error = null, isLoading = true, isRefreshing = false
        )
    }
}