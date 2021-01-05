package com.sharonovnik.vkclient

import android.app.Application
import com.sharonovnik.vkclient.ui.di.components.AppComponent
import com.sharonovnik.vkclient.ui.di.components.AuthComponent
import com.sharonovnik.vkclient.ui.di.components.DaggerAppComponent
import com.sharonovnik.vkclient.ui.di.modules.AppModule
import com.sharonovnik.vkclient.ui.di.modules.DatabaseModule
import com.sharonovnik.vkclient.ui.di.modules.MapperModule
import com.sharonovnik.vkclient.ui.di.modules.NetworkModule

class NewsFeedApplication: Application() {
    lateinit var appComponent: AppComponent
    var authComponent: AuthComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = buildAppComponent()
    }

    fun addAuthComponent() {
        authComponent = appComponent.plus(DatabaseModule, NetworkModule, MapperModule)
    }

    fun clearAuthComponent() {
        authComponent = null
    }

    private fun buildAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}