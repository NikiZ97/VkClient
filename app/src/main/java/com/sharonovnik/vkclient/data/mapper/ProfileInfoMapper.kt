package com.sharonovnik.vkclient.data.mapper

import com.sharonovnik.vkclient.data.local.entity.ProfileEntity
import com.sharonovnik.vkclient.data.network.response.UserInfoResponse

class ProfileInfoMapper: Mapper<UserInfoResponse, ProfileEntity> {
    override fun map(value: UserInfoResponse): ProfileEntity {
        return ProfileEntity(
            value.id, value.firstName, value.domain, value.lastName, value.photo,
            value.birthdate, value.country, value.city, value.about, value.lastSeen, value.followersCount,
            value.career, value.universityId, value.universityName, value.faculty,
            value.facultyName, value.graduationYear
        )
    }
}