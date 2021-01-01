package com.sharonovnik.homework_2.ui.di.modules

import androidx.lifecycle.ViewModel
import com.sharonovnik.homework_2.ui.profile.ProfileViewModel
import com.sharonovnik.homework_2.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel
}