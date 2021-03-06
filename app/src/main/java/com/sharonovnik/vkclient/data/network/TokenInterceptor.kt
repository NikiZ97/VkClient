package com.sharonovnik.vkclient.data.network

import com.sharonovnik.vkclient.ui.di.scopes.AppScope
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@AppScope
class TokenInterceptor @Inject constructor(): Interceptor {
    var token: String = ""

    companion object {
        const val PREF_ACCESS_TOKEN = "pref_access_token"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}