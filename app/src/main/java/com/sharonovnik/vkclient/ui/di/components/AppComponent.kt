package com.sharonovnik.vkclient.ui.di.components

import com.sharonovnik.vkclient.ui.VKLoginActivity
import com.sharonovnik.vkclient.ui.di.modules.AppModule
import com.sharonovnik.vkclient.ui.di.modules.DatabaseModule
import com.sharonovnik.vkclient.ui.di.modules.MapperModule
import com.sharonovnik.vkclient.ui.di.modules.NetworkModule
import com.sharonovnik.vkclient.ui.di.scopes.AppScope
import dagger.Component

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent {
    fun inject(loginActivity: VKLoginActivity)
    fun plus(databaseModule: DatabaseModule,
        networkModule: NetworkModule,
        mapperModule: MapperModule): AuthComponent
}