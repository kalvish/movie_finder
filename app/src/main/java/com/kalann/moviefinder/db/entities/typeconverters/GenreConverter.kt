package com.kalann.moviefinder.db.entities.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kalann.moviefinder.api.moshi.Genre


class GenreConverter {
    @TypeConverter
    fun toList(value: String?): List<Genre?>? {
        return Gson().fromJson(value, object : TypeToken<List<Genre?>?>() {}.type)
    }

    @TypeConverter
    fun toString(list: List<Genre?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}