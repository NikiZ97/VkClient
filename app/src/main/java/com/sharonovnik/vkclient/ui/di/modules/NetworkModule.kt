package com.sharonovnik.vkclient.ui.di.modules

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sharonovnik.vkclient.BuildConfig
import com.sharonovnik.vkclient.data.network.ApiService
import com.sharonovnik.vkclient.data.network.TokenInterceptor
import com.sharonovnik.vkclient.ui.di.scopes.AuthScope
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @AuthScope
    fun provideOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        with(okHttpBuilder) {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(loggingInterceptor)
            }
            addInterceptor(tokenInterceptor)
        }
        return okHttpBuilder.build()
    }

    @Provides
    @AuthScope
    fun provideConverterFactory(): Converter.Factory {
        return Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
            .asConverterFactory(MediaType.parse("application/json; charset=utf-8")!!)
    }

    @Provides
    @AuthScope
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @AuthScope
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}