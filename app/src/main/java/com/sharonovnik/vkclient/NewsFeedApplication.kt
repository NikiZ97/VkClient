package com.sharonovnik.vkclient

import android.app.Application
import com.sharonovnik.vkclient.ui.di.components.*
import com.sharonovnik.vkclient.ui.di.modules.AppModule

class NewsFeedApplication: Application() {
    lateinit var appComponent: AppComponent
    var authComponent: AuthComponent? = null
    var activityComponent: ActivityComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = buildAppComponent()
    }

    fun addAuthComponent() {
        authComponent = DaggerAuthComponent.builder()
            .appComponent(appComponent)
            .build()
    }

    fun clearAuthComponent() {
        authComponent = null
    }

    fun addActivityComponent() {
        activityComponent = DaggerActivityComponent.builder()
            .authComponent(authComponent)
            .build()
    }

    fun clearActivityComponent() {
        activityComponent = null
    }

    private fun buildAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}