package com.sharonovnik.vkclient.ui

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.github.florent37.runtimepermission.kotlin.askPermission
import java.lang.ref.Reference
import java.lang.ref.WeakReference

class PermissionResolver {

    private val activityReference: Reference<FragmentActivity>?
    private val fragmentReference: Reference<Fragment>?

    constructor(activity: FragmentActivity) {
        activityReference = WeakReference(activity)
        fragmentReference = null
    }

    constructor(fragment: Fragment) {
        activityReference = null
        fragmentReference = WeakReference(fragment)
    }

    private fun checkPermission(vararg permissions: String): Boolean {
        for (permission in permissions) {
            val permissionState = checkSelfPermission(getFragmentActivity(), permission)
            if (permissionState == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    fun obtainPermission(
        permission: String, granted: () -> Unit, denied: (askPermission: () -> Unit) -> Unit
    ) {
        obtainPermission(listOf(permission), granted, denied)
    }

    private fun obtainPermission(
        permissions: List<String>, granted: () -> Unit, denied: (askPermission: () -> Unit) -> Unit
    ) {
        val typedArray = permissions.toTypedArray()
        if (checkPermission(*typedArray)) {
            granted.invoke()
        } else {
            denied.invoke {
                askPermission(granted, *typedArray)
            }
        }
    }

    private fun askPermission(granted: () -> Unit, vararg permissions: String) {
        getFragmentActivity().askPermission(*permissions) {
            granted.invoke()
        }
            .onDeclined {
                if (it.hasForeverDenied()) {
                    it.goToSettings()
                }
            }
    }

    private fun getFragmentActivity(): FragmentActivity {
        val activity = activityReference?.get() ?: fragmentReference?.get()?.activity
        return requireNotNull(activity) {
            "Permission Resolver not attached to activity."
        }
    }
}