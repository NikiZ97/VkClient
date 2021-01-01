package com.sharonovnik.homework_2.domain.repository

import com.sharonovnik.homework_2.data.local.entity.PostEntity
import com.sharonovnik.homework_2.ui.posts.PostsAction
import io.reactivex.Observable

interface PostsRepository {
    fun getPosts(): Observable<PostsAction>
    fun changePostLikeStatus(methodName: String, post: PostEntity): Observable<PostsAction>
    fun getLocalPosts(onlyFavorite: Boolean = true): Observable<PostsAction>
}