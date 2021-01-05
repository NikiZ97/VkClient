package com.sharonovnik.vkclient.ui.di.components

import com.sharonovnik.vkclient.ui.di.modules.DatabaseModule
import com.sharonovnik.vkclient.ui.di.modules.MapperModule
import com.sharonovnik.vkclient.ui.di.modules.NetworkModule
import com.sharonovnik.vkclient.ui.di.modules.RepositoryModule
import com.sharonovnik.vkclient.ui.di.scopes.AuthScope
import dagger.Subcomponent

@Subcomponent(
    modules = [DatabaseModule::class,
        NetworkModule::class,
        MapperModule::class,
        RepositoryModule::class]
)
@AuthScope
interface AuthComponent {
    fun plus(): ActivityComponent
}