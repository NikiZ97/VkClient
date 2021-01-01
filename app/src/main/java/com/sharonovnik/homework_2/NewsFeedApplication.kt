package com.sharonovnik.homework_2

import android.app.Application
import com.sharonovnik.homework_2.ui.di.components.*
import com.sharonovnik.homework_2.ui.di.modules.AppModule

class NewsFeedApplication: Application() {
    lateinit var appComponent: AppComponent
    var authComponent: AuthComponent? = null
    var postPreviewComponent: PostPreviewComponent? = null

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

    fun addPostPreviewComponent() {
        postPreviewComponent = DaggerPostPreviewComponent.builder()
            .authComponent(authComponent)
            .build()
    }

    fun clearPostPreviewComponent() {
        postPreviewComponent = null
    }

    private fun buildAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}