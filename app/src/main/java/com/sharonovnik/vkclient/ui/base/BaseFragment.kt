package com.sharonovnik.vkclient.ui.base

import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {

    private val presentationComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent?.plus()
    }

    protected val injector get() = presentationComponent
}