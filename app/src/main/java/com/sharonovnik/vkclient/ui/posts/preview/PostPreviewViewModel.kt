package com.sharonovnik.vkclient.ui.posts.preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.sharonovnik.vkclient.domain.repository.PostPreviewRepository
import com.sharonovnik.vkclient.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Provider

class PostPreviewViewModel @Inject constructor(
    private val postPreviewRepositoryProvider: Provider<PostPreviewRepository>
): BaseViewModel() {
    private val mutableState = MutableLiveData<PostPreviewState>()
    private val postInput: Relay<PostPreviewAction> = PublishRelay.create()

    private val postState: Observable<PostPreviewState> = postInput
        .reduxStore(
            initialState = PostPreviewState(),
            sideEffects = listOf(
                ::getPostByIdSideEffect,
                ::getCommentsSideEffect,
                ::createCommentSideEffect,
            ),
            reducer = PostPreviewState::reduce
        )

    init {
        disposables.add(
            postState.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableState.value = it
                }, {}
                ))
    }

    fun getState() = mutableState

    private fun getPostByIdSideEffect(
        actions: Observable<PostPreviewAction>, state: StateAccessor<PostPreviewState>
    ): Observable<PostPreviewAction> {
        return actions.ofType(PostPreviewAction.GetLocalPostById::class.java)
            .switchMap { post ->
                postPreviewRepositoryProvider.get().getLocalPostById(post.id).onErrorReturn {
                    return@onErrorReturn PostPreviewAction.ErrorLoadPost(it)
                }
            }
    }

    private fun getCommentsSideEffect(
        actions: Observable<PostPreviewAction>, state: StateAccessor<PostPreviewState>
    ): Observable<PostPreviewAction> {
        return actions.ofType(PostPreviewAction.GetPostComments::class.java)
            .switchMap { post ->
                postPreviewRepositoryProvider.get().getPostComments(post.ownerId, post.postId)
                    .toObservable()
            }
            .onErrorReturn { PostPreviewAction.ErrorLoadPostComments(it) }
    }

    private fun createCommentSideEffect(
        actions: Observable<PostPreviewAction>, state: StateAccessor<PostPreviewState>
    ): Observable<PostPreviewAction> {
        return actions.ofType(PostPreviewAction.CreateComment::class.java)
            .switchMap { post ->
                postPreviewRepositoryProvider.get().createComment(post.ownerId, post.postId, post.message)
                    .toObservable()
            }
            .onErrorReturn { PostPreviewAction.ErrorCreateComment(it) }
    }

    fun getPostPreviewById(postId: Int, ownerId: Long) {
        postInput.accept(PostPreviewAction.GetLocalPostById(postId))
        postInput.accept(PostPreviewAction.GetPostComments(ownerId, postId))
    }

    fun createComment(postId: Int, ownerId: Long, message: String) {
        postInput.accept(PostPreviewAction.CreateComment(ownerId, postId, message))
    }

    override fun init(savedStateHandle: SavedStateHandle) {}
}