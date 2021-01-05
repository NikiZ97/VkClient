package com.sharonovnik.vkclient.ui.di.components

import com.sharonovnik.vkclient.ui.di.modules.PresentationModule
import com.sharonovnik.vkclient.ui.di.scopes.ActivityScope
import dagger.Subcomponent

@Subcomponent(
    // Maybe it will be useful later. E.g., for providing activity
    modules = []
)
@ActivityScope
interface ActivityComponent {
    fun plus(presentationModule: PresentationModule): PresentationComponent
}