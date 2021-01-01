package com.sharonovnik.vkclient.ui.profile

import androidx.lifecycle.MutableLiveData
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.sharonovnik.vkclient.R
import com.sharonovnik.vkclient.data.CurrentUser
import com.sharonovnik.vkclient.data.local.entity.PostEntity
import com.sharonovnik.vkclient.data.local.entity.ProfileEntity
import com.sharonovnik.vkclient.data.network.response.Career
import com.sharonovnik.vkclient.domain.repository.ProfileRepository
import com.sharonovnik.vkclient.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import java.util.*
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseViewModel() {
    private val mutableState = MutableLiveData<ProfileState>()
    private val _input: Relay<ProfileAction> = PublishRelay.create()
    val input: Consumer<ProfileAction> = _input

    val state: Observable<ProfileState> = _input
        .reduxStore(
        initialState = ProfileState(), sideEffects = listOf(
            ::getUserInfoSideEffect,
            ::getUserWallSideEffect,
            ::composePostSideEffect,
        ), reducer = ProfileState::reduce
    )

    init {
        disposables.add(
            state.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.profileInfo != null) {
                        it.profileInfo.infoItems = createInfoSections(it.profileInfo)
                    }
                    mutableState.value = it
                })
    }

    fun getState() = mutableState

    private fun getUserInfoSideEffect(
        actions: Observable<ProfileAction>, state: StateAccessor<ProfileState>
    ): Observable<ProfileAction> {
        return actions.ofType(ProfileAction.LoadUserInfo::class.java)
            .switchMap {
                profileRepository.getCurrentUser()
            }
    }

    private fun getUserWallSideEffect(
        actions: Observable<ProfileAction>, state: StateAccessor<ProfileState>
    ): Observable<ProfileAction> {
        return actions.ofType(ProfileAction.LoadUserWall::class.java)
            .switchMap {
                profileRepository.getUserWall()
            }
    }

    private fun composePostSideEffect(
        actions: Observable<ProfileAction>, state: StateAccessor<ProfileState>
    ): Observable<ProfileAction> {
        return actions.ofType(ProfileAction.ComposePost::class.java)
            .switchMap {
                profileRepository.composePost(createPostEntity(it.text))
            }
            .onErrorReturn { ProfileAction.ErrorComposePost(it) }
    }

    private fun createPostEntity(text: String) = PostEntity(
        -1, CurrentUser.userId, Calendar.getInstance().time.time / 1000,
        null, text, null, false, CurrentUser.userId,
        null, true
    )

    fun getAllUserInfo() {
        input.accept(ProfileAction.LoadUserWall)
        input.accept(ProfileAction.LoadUserInfo)
    }

    private fun createInfoSections(profileInfo: ProfileEntity): List<UserInfoItem> {
        val items = mutableListOf<UserInfoItem>()
        items += createMainSection(profileInfo)
        items += createEducationSection(profileInfo)
        items += createCareerSection(profileInfo.career)
        return items
    }

    private fun createMainSection(profileInfo: ProfileEntity): List<UserInfoItem> {
        val items = mutableListOf<UserInfoItem>()
        val mainInfoSection = UserInfoSection(R.string.user_main_info_section)
        if (profileInfo.city != null) {
            items += UserInfoItem(
                0, StringHolder(resId = R.string.city_title),
                profileInfo.city.title,
                R.drawable.ic_city
            ).attachToSection(mainInfoSection)
        }
        if (profileInfo.country != null) {
            items += UserInfoItem(
                1, StringHolder(resId = R.string.country_title),
                profileInfo.country.title,
                R.drawable.ic_country
            ).attachToSection(mainInfoSection)
        }
        if (profileInfo.birthdate != null) {
            items += UserInfoItem(
                2, StringHolder(resId = R.string.birthday_title),
                profileInfo.birthdate,
                R.drawable.ic_birthday
            ).attachToSection(mainInfoSection)
        }
        if (profileInfo.followersCount != 0L) {
            items += UserInfoItem(
                3, StringHolder(resId = R.string.followers_title),
                profileInfo.followersCount.toString(),
                R.drawable.ic_followers
            ).attachToSection(mainInfoSection)
        }
        return items
    }

    private fun createEducationSection(profileInfo: ProfileEntity): List<UserInfoItem> {
        val items = mutableListOf<UserInfoItem>()
        val educationInfoSection = UserInfoSection(R.string.user_education_section)
        if (profileInfo.universityName.isNotEmpty()) {
            items += UserInfoItem(
                4, StringHolder(string = profileInfo.universityName),
                profileInfo.facultyName,
                R.drawable.ic_education
            ).attachToSection(educationInfoSection)
        }
        return items
    }

    private fun createCareerSection(career: List<Career>): List<UserInfoItem> {
        val items = mutableListOf<UserInfoItem>()
        val careerInfoSection = UserInfoSection(R.string.user_career_section)
        career.forEach { careerItem ->
            items += UserInfoItem(
                5, StringHolder(string = careerItem.companyName),
                careerItem.position,
                R.drawable.ic_career
            ).attachToSection(careerInfoSection)
        }
        return items
    }
}