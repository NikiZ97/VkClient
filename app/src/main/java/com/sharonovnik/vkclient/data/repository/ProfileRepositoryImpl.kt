package com.sharonovnik.vkclient.data.repository

import com.sharonovnik.vkclient.convertToPostRow
import com.sharonovnik.vkclient.data.CurrentUser
import com.sharonovnik.vkclient.data.local.dao.PostsDao
import com.sharonovnik.vkclient.data.local.dao.ProfileDao
import com.sharonovnik.vkclient.data.local.entity.PostEntity
import com.sharonovnik.vkclient.data.mapper.ProfileInfoMapper
import com.sharonovnik.vkclient.data.mapper.UserWallMapper
import com.sharonovnik.vkclient.data.network.ApiService
import com.sharonovnik.vkclient.domain.repository.ProfileRepository
import com.sharonovnik.vkclient.ui.profile.ProfileAction
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: ProfileDao, private val apiService: ApiService,
    private val profileInfoMapper: ProfileInfoMapper, private val userWallMapper: UserWallMapper,
    private val postsDao: PostsDao
) : ProfileRepository {

    private companion object {
        private const val FIELDS =
            "photo_100, domain, first_name, last_name, about, bdate, "+
                    "city,country, career, education, followers_count, last_seen"
    }

    override fun getCurrentUser(): Observable<ProfileAction> {
        return Observable.mergeDelayError(
            getLocalUserInfo(), getRemoteUserInfo()
        )
            .onErrorReturn {
                return@onErrorReturn ProfileAction.ErrorLoadUserProfile(it)
            }
    }

    private fun getLocalUserInfo(): Observable<ProfileAction> {
        return profileDao.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .toObservable()
            .map { ProfileAction.UserInfoLoaded(it) }
    }

    private fun getRemoteUserInfo(): Observable<ProfileAction> {
        return apiService.getCurrentUserInfo(FIELDS)
            .subscribeOn(Schedulers.io())
            .map {
                profileInfoMapper.map(it.response[0]) }
            .toObservable()
            .map { info ->
                profileDao.insertProfile(info)
                return@map ProfileAction.UserInfoLoaded(info)
            }
    }

    override fun getUserWall(): Observable<ProfileAction> {
        return Observable.mergeDelayError(
            getLocalUserWall(),
            getRemoteUserWall().onErrorReturn {
                return@onErrorReturn ProfileAction.ErrorLoadUserProfile(it)
            },
        )
    }

    private fun getLocalUserWall(): Observable<ProfileAction> {
        return postsDao.getPostsWithoutGroups(CurrentUser.userId)
            .subscribeOn(Schedulers.io())
            .toObservable()
            .map {
                return@map ProfileAction.UserWallLoaded(it.convertToPostRow())
            }
    }

    private fun getRemoteUserWall(): Observable<ProfileAction> {
        return apiService.getCurrentUserWall()
            .subscribeOn(Schedulers.io())
            .map { userWallMapper.map(it.response) }
            .toObservable()
            .map { wall ->
                postsDao.updateUserWall(wall)
                return@map ProfileAction.UserWallLoaded(wall.convertToPostRow())
            }
    }

    override fun composePost(postEntity: PostEntity): Observable<ProfileAction> {
        return apiService.composePost(postEntity.text!!)
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                apiService.getPostById("${CurrentUser.userId}_${response.response.postId}")
            }
            .map { userWallMapper.map(it.response) }
            .flatMap {
                postsDao.insertPost(postEntity.copy(id = it[0].id))
                    .andThen(Single.just(it))
            }
            .toObservable()
            .map {
                return@map ProfileAction.PostComposed(it[0].convertToPostRow())
            }
    }
}