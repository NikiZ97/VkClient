package com.sharonovnik.homework_2.ui.di.modules

import androidx.lifecycle.ViewModelProvider
import com.sharonovnik.homework_2.ui.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}