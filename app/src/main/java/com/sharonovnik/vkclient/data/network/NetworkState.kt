package com.sharonovnik.vkclient.data.network

import com.sharonovnik.vkclient.ui.posts.PostRow

sealed class NetworkState {
    data class Success(val data: List<PostRow>): NetworkState()
    data class Error(val error: String = ""): NetworkState()
    data class NetworkException(val error: String): NetworkState()

    sealed class HttpErrors : NetworkState() {
        data class ResourceForbidden(val exception: String) : HttpErrors()
        data class ResourceNotFound(val exception: String) : HttpErrors()
        data class InternalServerError(val exception: String) : HttpErrors()
    }
}