package com.sharonovnik.vkclient.ui.main

import androidx.lifecycle.MutableLiveData
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.sharonovnik.vkclient.domain.repository.PostsRepository
import com.sharonovnik.vkclient.ui.base.BaseViewModel
import com.sharonovnik.vkclient.ui.posts.PostsAction
import com.sharonovnik.vkclient.ui.posts.PostsState
import com.sharonovnik.vkclient.ui.posts.reduce
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import javax.inject.Inject

class MainViewModel @Inject constructor(private val postsRepository: PostsRepository): BaseViewModel() {
    private val mutableState = MutableLiveData<PostsState>()
    private val _mainInput: Relay<PostsAction> = PublishRelay.create()
    val mainInput: Consumer<PostsAction> = _mainInput

    private val mainState: Observable<PostsState> = _mainInput.reduxStore(
        initialState = PostsState(),
        sideEffects = listOf(::getLocalPostsSideEffect),
        reducer = PostsState::reduce
    )

    init {
        disposables.add(
            mainState.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableState.value = it
                }, {})
        )
        mainInput.accept(PostsAction.LoadLocalPosts)
    }

    fun getState() = mutableState

    private fun getLocalPostsSideEffect(
        actions: Observable<PostsAction>, state: StateAccessor<PostsState>
    ): Observable<PostsAction> {
        return actions.ofType(PostsAction.LoadLocalPosts::class.java)
            .switchMap {
                postsRepository.getLocalPosts()
                    .onErrorReturn {
                    return@onErrorReturn PostsAction.ErrorLoadPosts(it)
                }
            }
    }
}