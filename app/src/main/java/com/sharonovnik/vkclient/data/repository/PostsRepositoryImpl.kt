package com.sharonovnik.vkclient.data.repository

import android.content.Context
import com.sharonovnik.vkclient.R
import com.sharonovnik.vkclient.convertToPostRow
import com.sharonovnik.vkclient.data.local.dao.PostsDao
import com.sharonovnik.vkclient.data.local.entity.PostEntity
import com.sharonovnik.vkclient.data.mapper.PostsDtoToEntityMapper
import com.sharonovnik.vkclient.data.network.ApiService
import com.sharonovnik.vkclient.domain.repository.PostsRepository
import com.sharonovnik.vkclient.groupByDate
import com.sharonovnik.vkclient.ui.posts.PostsAction
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val api: ApiService, private val postsDao: PostsDao,
    private val postsMapper: PostsDtoToEntityMapper, private val context: Context
) : PostsRepository {

    override fun getPosts(): Observable<PostsAction> {
        return Observable.mergeDelayError(
            getLocalPosts(onlyFavorite = false),
            getRemotePosts()
                .onErrorReturn {
                    return@onErrorReturn PostsAction.ErrorLoadPosts(it)
                }
        )
    }

    override fun changePostLikeStatus(
        methodName: String, post: PostEntity
    ): Observable<PostsAction> {
        return api.addOrRemovePostLike(methodName, post.id, -post.source)
            .subscribeOn(Schedulers.io())
            .flatMap { postsDao.updatePost(post.copy(userLikeTimestamp = Calendar.getInstance().time.time)) }
            .toObservable()
            .map { PostsAction.PostLiked }
    }

    private fun getRemotePosts(): Observable<PostsAction> {
        return api.getNewsFeed(R.integer.com_vk_sdk_AppId.toString())
            .subscribeOn(Schedulers.io())
            .map { response -> postsMapper.map(response.response) }
            .map { pair ->
                postsDao.insertPostsAndGroups(pair.first, pair.second)
                return@map pair.first.groupByDate(context)
            }
            .toObservable()
            .map { PostsAction.PostsLoaded(itemsList = it) }
    }

    override fun getLocalPosts(onlyFavorite: Boolean): Observable<PostsAction> {
        return postsDao.getPosts()
            .subscribeOn(Schedulers.io())
            .flatMap { list ->
                Observable.fromIterable(list)
                    .filter {
                        if (it.post.likes == null) {
                            return@filter false
                        }
                        if (onlyFavorite) {
                            it.post.likes.userLikes > 0
                        } else {
                            true
                        }
                    }
                    .map {
                        it.post.publicName = it.group?.name
                        it.post.avatar = it.group?.photo50
                        return@map it.post
                    }
                    .toList()
                    .map {
                        if (onlyFavorite) {
                            it.convertToPostRow()
                                .sortedByDescending { row -> row.post?.userLikeTimestamp }
                        } else {
                            it.groupByDate(context)
                        }
                    }
            }
            .toObservable()
            .map { PostsAction.PostsLoaded(it) }
    }
}