package com.sharonovnik.vkclient.data.network.response

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("domain")
    val domain: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("photo_100")
    val photo: String,

    @SerializedName("bdate")
    val birthdate: String?,

    @SerializedName("country")
    val country: Country?,

    @SerializedName("city")
    val city: City?,

    @SerializedName("about")
    val about: String,

    @SerializedName("last_seen")
    val lastSeen: LastSeen,

    @SerializedName("followers_count")
    val followersCount: Long,

    @SerializedName("career")
    val career: List<Career>,

    @SerializedName("university")
    val universityId: Int,

    @SerializedName("university_name")
    val universityName: String,

    @SerializedName("faculty")
    val faculty: Int,

    @SerializedName("faculty_name")
    val facultyName: String,

    @SerializedName("graduation")
    val graduationYear: Int,
)

data class Country(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,
)

data class City(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,
)

data class LastSeen(
    @SerializedName("platform")
    val platform: Int,

    @SerializedName("time")
    val time: Long,
)

data class Career(
    @SerializedName("group_id")
    val communityId: Int,

    @SerializedName("company")
    val companyName: String,

    @SerializedName("country_id")
    val countryId: Int,

    @SerializedName("city_id")
    val cityId: Int,

    @SerializedName("city_name")
    val cityName: String,

    @SerializedName("from")
    val careerBegYear: Int,

    @SerializedName("until")
    val careerEndYear: Int,

    @SerializedName("position")
    val position: String,
)