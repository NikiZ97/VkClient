package com.sharonovnik.vkclient.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.sharonovnik.vkclient.data.network.response.Career
import com.sharonovnik.vkclient.data.network.response.City
import com.sharonovnik.vkclient.data.network.response.Country
import com.sharonovnik.vkclient.data.network.response.LastSeen
import com.sharonovnik.vkclient.ui.profile.UserInfoItem

@Entity(tableName = ProfileEntity.TABLE_NAME)
class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name="first_name")
    val firstName: String,

    @ColumnInfo(name="domain")
    val domain: String,

    @ColumnInfo(name="last_name")
    val lastName: String,

    @ColumnInfo(name="photo")
    val photo: String,

    @ColumnInfo(name="bdate")
    val birthdate: String?,

    @ColumnInfo(name="country")
    val country: Country?,

    @ColumnInfo(name = "city")
    val city: City?,

    @ColumnInfo(name="about")
    val about: String,

    @ColumnInfo(name="last_seen")
    val lastSeen: LastSeen,

    @ColumnInfo(name="followers_count")
    val followersCount: Long,

    @ColumnInfo(name="career")
    val career: List<Career>,

    @ColumnInfo(name="university")
    val universityId: Int,

    @ColumnInfo(name="university_name")
    val universityName: String,

    @ColumnInfo(name="faculty")
    val faculty: Int,

    @ColumnInfo(name="faculty_name")
    val facultyName: String,

    @ColumnInfo(name="graduation")
    val graduationYear: Int,
) {
    companion object {
        const val TABLE_NAME = "Profile"
    }

    @Ignore
    var infoItems: List<UserInfoItem>? = null
}