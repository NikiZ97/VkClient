package com.sharonovnik.vkclient.ui.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.sharonovnik.vkclient.ui.base.SavedStateViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>,
    savedStateRegistryOwner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, null) {

    override fun <T : ViewModel?> create(
        key: String, modelClass: Class<T>, handle: SavedStateHandle
    ): T {
        val viewModelProvider: Provider<ViewModel> = viewModels[modelClass]
            ?: throw IllegalArgumentException("Class $modelClass not found")
        val viewModel = viewModelProvider.get() as T

        if (viewModel is SavedStateViewModel) {
            viewModel.init(handle)
        }
        return viewModel
    }
}