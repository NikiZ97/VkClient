package com.sharonovnik.vkclient.ui.di.modules

import androidx.lifecycle.ViewModel
import com.sharonovnik.vkclient.ui.main.MainViewModel
import com.sharonovnik.vkclient.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel
}