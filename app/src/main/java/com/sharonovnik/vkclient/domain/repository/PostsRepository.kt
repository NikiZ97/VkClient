package com.sharonovnik.vkclient.domain.repository

import com.sharonovnik.vkclient.data.local.entity.PostEntity
import com.sharonovnik.vkclient.ui.posts.PostsAction
import io.reactivex.Observable

interface PostsRepository {
    fun getPosts(): Observable<PostsAction>
    fun changePostLikeStatus(methodName: String, post: PostEntity): Observable<PostsAction>
    fun getLocalPosts(onlyFavorite: Boolean = true): Observable<PostsAction>
}