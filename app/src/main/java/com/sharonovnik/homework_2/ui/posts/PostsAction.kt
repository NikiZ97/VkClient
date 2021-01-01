package com.sharonovnik.homework_2.ui.posts

import com.sharonovnik.homework_2.data.local.entity.PostEntity

sealed class PostsAction {
    object LoadPosts : PostsAction()
    object LoadPostsWithPullToRefresh : PostsAction()
    data class PostsLoaded(val itemsList: List<PostRow>) : PostsAction()
    data class LikePost(val methodName: String, val post: PostEntity) : PostsAction()
    data class ErrorLoadPosts(val throwable: Throwable) : PostsAction()
    data class ErrorLikePost(val throwable: Throwable) : PostsAction()
    object PostLiked : PostsAction()
    object LoadLocalPosts: PostsAction()
}