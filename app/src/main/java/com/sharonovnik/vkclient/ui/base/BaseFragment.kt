package com.sharonovnik.vkclient.ui.base

import androidx.fragment.app.Fragment
import com.sharonovnik.vkclient.ui.di.modules.PresentationModule

open class BaseFragment: Fragment() {

    private val presentationComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent?.plus(PresentationModule())
    }

    protected val injector get() = presentationComponent
}