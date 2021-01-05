package com.sharonovnik.vkclient.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.sharonovnik.vkclient.NewsFeedApplication

open class BaseActivity : AppCompatActivity() {
    private val authComponent get() = (application as NewsFeedApplication).authComponent

    internal val activityComponent by lazy {
        authComponent?.plus()
    }

    private val presentationComponent by lazy {
        activityComponent?.plus()
    }

    protected val injector get() = presentationComponent
}