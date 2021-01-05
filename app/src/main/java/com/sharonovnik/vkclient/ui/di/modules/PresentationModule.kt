package com.sharonovnik.vkclient.ui.di.modules

import androidx.savedstate.SavedStateRegistryOwner
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val savedStateRegistryOwner: SavedStateRegistryOwner) {

    @Provides
    fun savedStateRegistryOwner() = savedStateRegistryOwner
}