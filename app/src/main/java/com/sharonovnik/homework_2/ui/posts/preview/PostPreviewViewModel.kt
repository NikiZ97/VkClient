package com.sharonovnik.homework_2.ui.posts.preview

import androidx.lifecycle.MutableLiveData
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.sharonovnik.homework_2.domain.repository.PostPreviewRepository
import com.sharonovnik.homework_2.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class PostPreviewViewModel @Inject constructor(
    private val postPreviewRepository: PostPreviewRepository
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
                postPreviewRepository.getLocalPostById(post.id).onErrorReturn {
                    return@onErrorReturn PostPreviewAction.ErrorLoadPost(it)
                }
            }
    }

    private fun getCommentsSideEffect(
        actions: Observable<PostPreviewAction>, state: StateAccessor<PostPreviewState>
    ): Observable<PostPreviewAction> {
        return actions.ofType(PostPreviewAction.GetPostComments::class.java)
            .switchMap { post ->
                postPreviewRepository.getPostComments(post.ownerId, post.postId)
                    .toObservable()
            }
            .onErrorReturn { PostPreviewAction.ErrorLoadPostComments(it) }
    }

    private fun createCommentSideEffect(
        actions: Observable<PostPreviewAction>, state: StateAccessor<PostPreviewState>
    ): Observable<PostPreviewAction> {
        return actions.ofType(PostPreviewAction.CreateComment::class.java)
            .switchMap { post ->
                postPreviewRepository.createComment(post.ownerId, post.postId, post.message)
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
}