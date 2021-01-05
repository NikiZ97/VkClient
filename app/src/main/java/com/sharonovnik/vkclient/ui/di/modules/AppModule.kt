package com.sharonovnik.vkclient.ui.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.sharonovnik.vkclient.ui.di.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {
    @Provides
    @AppScope
    fun provideContext() = context

    companion object {

        @Provides
        @AppScope
        fun providePreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        }
    }
}