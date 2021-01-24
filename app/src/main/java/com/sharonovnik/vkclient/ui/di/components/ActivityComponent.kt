package com.sharonovnik.vkclient.ui.di.components

import androidx.fragment.app.FragmentActivity
import com.sharonovnik.vkclient.ui.di.modules.PresentationModule
import com.sharonovnik.vkclient.ui.di.scopes.ActivityScope
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    // Maybe it will be useful later. E.g., for providing activity
    modules = []
)
@ActivityScope
interface ActivityComponent {
    fun plus(presentationModule: PresentationModule): PresentationComponent

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance fun activity(activity: FragmentActivity): Builder
        fun build(): ActivityComponent
    }
}