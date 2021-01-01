package com.sharonovnik.vkclient.ui.di.modules

import androidx.lifecycle.ViewModel
import com.sharonovnik.vkclient.ui.profile.ProfileViewModel
import com.sharonovnik.vkclient.ui.viewmodel.ViewModelKey
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