package com.sharonovnik.homework_2.ui.di.modules

import androidx.lifecycle.ViewModel
import com.sharonovnik.homework_2.ui.main.MainViewModel
import com.sharonovnik.homework_2.ui.viewmodel.ViewModelKey
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