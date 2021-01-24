package com.sharonovnik.vkclient.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.sharonovnik.vkclient.NewsFeedApplication
import com.sharonovnik.vkclient.ui.di.modules.PresentationModule

open class BaseActivity : AppCompatActivity() {
    private val authComponent get() = (application as NewsFeedApplication).authComponent

    internal val activityComponent by lazy {
        authComponent?.builder()
            ?.activity(this as FragmentActivity)
            ?.build()
    }

    private val presentationComponent by lazy {
        activityComponent?.plus(PresentationModule(this))
    }

    protected val injector get() = presentationComponent
}