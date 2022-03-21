package com.kalann.moviefinder.db.entities.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kalann.moviefinder.api.moshi.Genre
import com.kalann.moviefinder.api.moshi.ProductionCountry


class ProductionCountryConverter {
    @TypeConverter
    fun toList(value: String?): List<ProductionCountry?>? {
        return Gson().fromJson(value, object : TypeToken<List<ProductionCountry?>?>() {}.type)
    }

    @TypeConverter
    fun toString(list: List<ProductionCountry?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}