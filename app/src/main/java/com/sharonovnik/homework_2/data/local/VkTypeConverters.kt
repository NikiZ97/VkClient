package com.sharonovnik.homework_2.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sharonovnik.homework_2.data.network.response.Career
import com.sharonovnik.homework_2.data.network.response.City
import com.sharonovnik.homework_2.data.network.response.Country
import com.sharonovnik.homework_2.data.network.response.LastSeen
import com.sharonovnik.homework_2.domain.model.*

object VkTypeConverters {

    @TypeConverter
    @JvmStatic
    fun fromAttachments(attachments: List<Attachment?>?): String {
        val type = object: TypeToken<List<Attachment>>() {}.type
        return Gson().toJson(attachments, type)
    }

    @TypeConverter
    @JvmStatic
    fun toAttachments(attachmentsJson: String?): List<Attachment?>? {
        val type = object: TypeToken<List<Attachment>>() {}.type
        return Gson().fromJson(attachmentsJson, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromPhoto(photo: Photo?): String? {
        if (photo == null) {
            return null
        }
        val type = object: TypeToken<Photo>() {}.type
        return Gson().toJson(photo, type)
    }

    @TypeConverter
    @JvmStatic
    fun toPhoto(photoJson: String?): Photo? {
        if (photoJson == null) {
            return null
        }
        val type = object: TypeToken<Photo>() {}.type
        return Gson().fromJson(photoJson, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromLikes(likes: Likes?): String {
        val type = object: TypeToken<Likes?>() {}.type
        return Gson().toJson(likes, type)
    }

    @TypeConverter
    @JvmStatic
    fun toLikes(likesJson: String): Likes? {
        val type = object: TypeToken<Likes?>() {}.type
        return Gson().fromJson(likesJson, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromViews(views: Views?): String {
        val type = object: TypeToken<Views?>() {}.type
        return Gson().toJson(views, type)
    }

    @TypeConverter
    @JvmStatic
    fun toViews(viewsJson: String): Views? {
        val type = object: TypeToken<Views?>() {}.type
        return Gson().fromJson(viewsJson, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromComments(comments: Comments?): String {
        val type = object: TypeToken<Comments?>() {}.type
        return Gson().toJson(comments, type)
    }

    @TypeConverter
    @JvmStatic
    fun toComments(commentsJson: String): Comments? {
        val type = object: TypeToken<Comments?>() {}.type
        return Gson().fromJson(commentsJson, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromSize(size: Size): String {
        val type = object: TypeToken<Size>() {}.type
        return Gson().toJson(size, type)
    }

    @TypeConverter
    @JvmStatic
    fun toSize(sizeJson: String): Size {
        val type = object: TypeToken<Size>() {}.type
        return Gson().fromJson(sizeJson, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromLastSeen(lastSeen: LastSeen): String {
        val type = object: TypeToken<LastSeen>() {}.type
        return Gson().toJson(lastSeen, type)
    }

    @TypeConverter
    @JvmStatic
    fun toLastSeen(lastSeenJson: String): LastSeen {
        val type = object: TypeToken<LastSeen>() {}.type
        return Gson().fromJson(lastSeenJson, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromCareerList(careerList: List<Career>): String {
        val type = object: TypeToken<List<Career>>() {}.type
        return Gson().toJson(careerList, type)
    }

    @TypeConverter
    @JvmStatic
    fun toCareerList(careerListJson: String): List<Career> {
        val type = object: TypeToken<List<Career>>() {}.type
        return Gson().fromJson(careerListJson, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromCountry(country: Country): String {
        val type = object: TypeToken<Country>() {}.type
        return Gson().toJson(country, type)
    }

    @TypeConverter
    @JvmStatic
    fun toCountry(countryJson: String): Country {
        val type = object: TypeToken<Country>() {}.type
        return Gson().fromJson(countryJson, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromCity(city: City): String {
        val type = object: TypeToken<City>() {}.type
        return Gson().toJson(city, type)
    }

    @TypeConverter
    @JvmStatic
    fun toCity(cityJson: String): City {
        val type = object: TypeToken<City>() {}.type
        return Gson().fromJson(cityJson, type)
    }
}