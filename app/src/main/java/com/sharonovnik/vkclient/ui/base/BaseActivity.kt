package com.sharonovnik.vkclient.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.sharonovnik.vkclient.NewsFeedApplication
import com.sharonovnik.vkclient.ui.di.modules.PresentationModule

open class BaseActivity : AppCompatActivity() {
    private val authComponent get() = (application as NewsFeedApplication).authComponent

    internal val activityComponent by lazy {
        authComponent?.plus()
    }

    private val presentationComponent by lazy {
        activityComponent?.plus(PresentationModule())
    }

    protected val injector get() = presentationComponent
}