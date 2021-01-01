package com.sharonovnik.homework_2.domain.repository

import com.sharonovnik.homework_2.ui.posts.preview.PostPreviewAction
import io.reactivex.Observable
import io.reactivex.Single

interface PostPreviewRepository {
    fun getLocalPostById(id: Int): Observable<PostPreviewAction>
    fun getPostComments(ownerId: Long, postId: Int, portion: Int = 30): Single<PostPreviewAction>
    fun createComment(ownerId: Long, postId: Int, message: String): Single<PostPreviewAction>
}