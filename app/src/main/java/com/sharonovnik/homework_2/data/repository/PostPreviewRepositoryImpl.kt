package com.sharonovnik.homework_2.data.repository

import com.sharonovnik.homework_2.data.local.dao.PostsDao
import com.sharonovnik.homework_2.data.mapper.CommentMapper
import com.sharonovnik.homework_2.data.network.ApiService
import com.sharonovnik.homework_2.domain.repository.PostPreviewRepository
import com.sharonovnik.homework_2.ui.posts.preview.PostPreviewAction
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostPreviewRepositoryImpl @Inject constructor(
    private val postsDao: PostsDao, private val apiService: ApiService,
    private val commentMapper: CommentMapper
) : PostPreviewRepository {

    override fun getLocalPostById(id: Int): Observable<PostPreviewAction> {
        return postsDao.getPostById(id)
            .subscribeOn(Schedulers.io())
            .map {
                it.post.publicName = it.group?.name
                it.post.avatar = it.group?.photo50
                return@map it
            }
            .toObservable()
            .map { PostPreviewAction.PostPreviewLoaded(it) }
    }

    override fun getPostComments(
        ownerId: Long, postId: Int, portion: Int
    ): Single<PostPreviewAction> {
        return apiService.getPostComments(ownerId, postId, portion)
            .subscribeOn(Schedulers.io())
            .map { comment -> commentMapper.map(comment.response) }
            .map { PostPreviewAction.PostCommentsLoaded(it) }
    }

    override fun createComment(
        ownerId: Long, postId: Int, message: String
    ): Single<PostPreviewAction> {
        return apiService.createComment(ownerId, postId, message)
            .subscribeOn(Schedulers.io())
            .flatMap { createdCommentId ->
                apiService.getComment(ownerId, createdCommentId.response.commentId)
            }
            .map { comment -> commentMapper.map(comment.response) }
            .map { t -> PostPreviewAction.CommentCreated(t[0]) }
    }
}