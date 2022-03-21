package com.kalann.moviefinder.db.entities.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kalann.moviefinder.api.moshi.Genre
import com.kalann.moviefinder.api.moshi.ProductionCompany


class ProductionCompanyConverter {
    @TypeConverter
    fun toList(value: String?): List<ProductionCompany?>? {
        return Gson().fromJson(value, object : TypeToken<List<ProductionCompany?>?>() {}.type)
    }

    @TypeConverter
    fun toString(list: List<ProductionCompany?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}