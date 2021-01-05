package com.sharonovnik.vkclient.ui.base

import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel: SavedStateViewModel() {
    internal val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}