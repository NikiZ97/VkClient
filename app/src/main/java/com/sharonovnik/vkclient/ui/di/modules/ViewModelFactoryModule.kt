package com.sharonovnik.vkclient.ui.di.modules

import androidx.lifecycle.ViewModelProvider
import com.sharonovnik.vkclient.ui.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}