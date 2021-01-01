package com.sharonovnik.vkclient.ui.profile

import com.sharonovnik.vkclient.data.local.entity.ProfileEntity
import com.sharonovnik.vkclient.ui.posts.PostRow

sealed class ProfileAction {
    object LoadUserInfo: ProfileAction()
    object UpdateProfileWithPullToRefresh: ProfileAction()
    class UserInfoLoaded(val info: ProfileEntity): ProfileAction()
    class UserWallLoaded(val wall: List<PostRow>): ProfileAction()
    class ErrorLoadUserProfile(val throwable: Throwable): ProfileAction()
    object LoadUserWall: ProfileAction()
    class ComposePost(val text: String): ProfileAction()
    class PostComposed(val post: PostRow): ProfileAction()
    class ErrorComposePost(val throwable: Throwable): ProfileAction()
}