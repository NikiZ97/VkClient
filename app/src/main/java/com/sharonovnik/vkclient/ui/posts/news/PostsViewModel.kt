package com.sharonovnik.vkclient.ui.posts.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
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
import javax.inject.Provider

class PostsViewModel @Inject constructor(
    private val postsRepositoryProvider: Provider<PostsRepository>
) : BaseViewModel() {
    private val mutableState = MutableLiveData<PostsState>()
    private val _input: Relay<PostsAction> = PublishRelay.create()
    val input: Consumer<PostsAction> = _input

    val state: Observable<PostsState> = _input.reduxStore(
            initialState = PostsState(),
            sideEffects = listOf(::getPostsSideEffect,
                ::changePostLikeStatusSideEffect,
                ::getPostsOnPullSideEffect),
            reducer = PostsState::reduce
        )

    init {
        getPosts()
    }

    private fun getPosts() {
        disposables.add(
            state.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableState.value = it
                }, {})
        )
    }

    fun getState() = mutableState

    private fun getPostsSideEffect(
        actions: Observable<PostsAction>, state: StateAccessor<PostsState>
    ): Observable<PostsAction> {
        return actions.ofType(PostsAction.LoadPosts::class.java)
            .switchMap {
                postsRepositoryProvider.get().getPosts()
            }
    }

    private fun changePostLikeStatusSideEffect(
        actions: Observable<PostsAction>, state: StateAccessor<PostsState>
    ): Observable<PostsAction> {
        return actions.ofType(PostsAction.LikePost::class.java)
            .switchMap {
                postsRepositoryProvider.get().changePostLikeStatus(it.methodName, it.post)
            }
            .onErrorReturn { PostsAction.ErrorLikePost(it) }
    }

    private fun getPostsOnPullSideEffect(
        actions: Observable<PostsAction>, state: StateAccessor<PostsState>
    ): Observable<PostsAction> {
        return actions.ofType(PostsAction.LoadPostsWithPullToRefresh::class.java)
            .switchMap {
                postsRepositoryProvider.get().getPosts().onErrorReturn {
                    return@onErrorReturn PostsAction.ErrorLoadPosts(it)
                }
            }
    }

    override fun init(savedStateHandle: SavedStateHandle) {}
}