package com.sharonovnik.homework_2.domain.repository

import com.sharonovnik.homework_2.data.local.entity.PostEntity
import com.sharonovnik.homework_2.ui.profile.ProfileAction
import io.reactivex.Observable

interface ProfileRepository {
    fun getCurrentUser(): Observable<ProfileAction>
    fun getUserWall(): Observable<ProfileAction>
    fun composePost(postEntity: PostEntity): Observable<ProfileAction>
}