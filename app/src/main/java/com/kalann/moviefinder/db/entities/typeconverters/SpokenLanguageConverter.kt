package com.kalann.moviefinder.db.entities.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kalann.moviefinder.api.moshi.Genre
import com.kalann.moviefinder.api.moshi.SpokenLanguage


class SpokenLanguageConverter {
    @TypeConverter
    fun toList(value: String?): List<SpokenLanguage?>? {
        return Gson().fromJson(value, object : TypeToken<List<SpokenLanguage?>?>() {}.type)
    }

    @TypeConverter
    fun toString(list: List<SpokenLanguage?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}