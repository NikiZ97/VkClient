package com.sharonovnik.vkclient.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sharonovnik.vkclient.NewsFeedApplication
import com.sharonovnik.vkclient.data.CurrentUser
import com.sharonovnik.vkclient.data.network.TokenInterceptor
import com.sharonovnik.vkclient.ui.main.MainActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject

class VKLoginActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    @Inject
    lateinit var tokenInterceptor: TokenInterceptor

    private companion object {
        const val PREF_USER_ID = "pref_user_id"
    }

    private val tokenTracker: VKTokenExpiredHandler = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            VK.login(this@VKLoginActivity, arrayListOf(VKScope.WALL, VKScope.FRIENDS))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as NewsFeedApplication).appComponent.inject(this)
        VK.addTokenExpiredHandler(tokenTracker)
        if (sharedPrefs.contains(TokenInterceptor.PREF_ACCESS_TOKEN)) {
            tokenInterceptor.token = sharedPrefs.getString(TokenInterceptor.PREF_ACCESS_TOKEN, "")!!
            CurrentUser.userId = sharedPrefs.getLong(PREF_USER_ID, 0)
            startMainActivity()
        } else {
            VK.login(this, arrayListOf(VKScope.WALL, VKScope.FRIENDS))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                tokenInterceptor.token = token.accessToken
                CurrentUser.userId = token.userId.toLong()
                sharedPrefs.edit()
                    .putString(TokenInterceptor.PREF_ACCESS_TOKEN, token.accessToken)
                    .apply()
                sharedPrefs.edit()
                    .putLong(PREF_USER_ID, token.userId.toLong())
                    .apply()
                startMainActivity()
            }

            override fun onLoginFailed(errorCode: Int) {}
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this@VKLoginActivity, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        VK.removeTokenExpiredHandler(tokenTracker)
    }
}