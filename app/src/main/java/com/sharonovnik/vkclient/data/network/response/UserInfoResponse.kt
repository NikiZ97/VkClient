package com.sharonovnik.vkclient.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    @SerialName("id")
    val id: Long = -1,

    @SerialName("first_name")
    val firstName: String = "",

    @SerialName("domain")
    val domain: String = "",

    @SerialName("last_name")
    val lastName: String = "",

    @SerialName("photo_100")
    val photo: String = "",

    @SerialName("bdate")
    val birthdate: String = "",

    @SerialName("country")
    val country: Country? = null,

    @SerialName("city")
    val city: City? = null,

    @SerialName("about")
    val about: String = "",

    @SerialName("last_seen")
    val lastSeen: LastSeen,

    @SerialName("followers_count")
    val followersCount: Long = -1,

    @SerialName("career")
    val career: List<Career> = listOf(),

    @SerialName("university")
    val universityId: Int = -1,

    @SerialName("university_name")
    val universityName: String = "",

    @SerialName("faculty")
    val faculty: Int = -1,

    @SerialName("faculty_name")
    val facultyName: String = "",

    @SerialName("graduation")
    val graduationYear: Int = -1,
)

@Serializable
data class Country(
    @SerialName("id")
    val id: Int = -1,

    @SerialName("title")
    val title: String = "",
)

@Serializable
data class City(
    @SerialName("id")
    val id: Int = -1,

    @SerialName("title")
    val title: String = "",
)

@Serializable
data class LastSeen(
    @SerialName("platform")
    val platform: Int = -1,

    @SerialName("time")
    val time: Long = -1,
)

@Serializable
data class Career(
    @SerialName("group_id")
    val communityId: Int = -1,

    @SerialName("company")
    val companyName: String = "",

    @SerialName("country_id")
    val countryId: Int = -1,

    @SerialName("city_id")
    val cityId: Int = -1,

    @SerialName("city_name")
    val cityName: String = "",

    @SerialName("from")
    val careerBegYear: Int = -1,

    @SerialName("until")
    val careerEndYear: Int = -1,

    @SerialName("position")
    val position: String = "",
)