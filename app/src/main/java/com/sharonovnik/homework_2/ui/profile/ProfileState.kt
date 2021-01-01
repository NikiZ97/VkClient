package com.sharonovnik.homework_2.ui.profile

import com.sharonovnik.homework_2.data.local.entity.ProfileEntity
import com.sharonovnik.homework_2.ui.posts.PostRow

data class ProfileState(
    val profileInfo: ProfileEntity? = null,
    val userWall: List<PostRow>? = null,
    val isWallEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
) {
    override fun toString(): String {
        return "ProfileState(profileInfo=$profileInfo, userWall=${userWall?.size}, isWallEmpty=$isWallEmpty, isLoading=$isLoading, error=$error)"
    }
}

internal fun ProfileState.reduce(action: ProfileAction): ProfileState {
    return when (action) {
        is ProfileAction.LoadUserInfo -> copy(
            isLoading = true, error = null,
        )
        is ProfileAction.UserInfoLoaded -> copy(
            isLoading = false, error = null, profileInfo = action.info,
        )
        is ProfileAction.UpdateProfileWithPullToRefresh -> copy(
            isLoading = true, error = null,
        )
        is ProfileAction.ErrorLoadUserProfile -> copy(
            isLoading = false, error = action.throwable,
        )
        is ProfileAction.LoadUserWall -> copy(
            isLoading = true, error = null,
        )
        is ProfileAction.UserWallLoaded -> copy(
            isLoading = false, error = null, userWall = action.wall
        )
        is ProfileAction.ComposePost -> copy(
            isLoading = true
        )
        is ProfileAction.PostComposed -> copy(
            isLoading = false, error = null, userWall = this.userWall?.plus(action.post)?.sortedByDescending { it.post?.date }
        )
        is ProfileAction.ErrorComposePost -> copy(
            isLoading = false, error = action.throwable
        )
    }
}