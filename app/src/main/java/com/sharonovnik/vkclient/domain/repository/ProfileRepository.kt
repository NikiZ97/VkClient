package com.sharonovnik.vkclient.domain.repository

import com.sharonovnik.vkclient.data.local.entity.PostEntity
import com.sharonovnik.vkclient.ui.profile.ProfileAction
import io.reactivex.Observable

interface ProfileRepository {
    fun getCurrentUser(): Observable<ProfileAction>
    fun getUserWall(): Observable<ProfileAction>
    fun composePost(postEntity: PostEntity): Observable<ProfileAction>
}