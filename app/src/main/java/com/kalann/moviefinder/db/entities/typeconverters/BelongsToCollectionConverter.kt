package com.kalann.moviefinder.db.entities.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kalann.moviefinder.api.moshi.BelongsToCollection

class BelongsToCollectionConverter {
    @TypeConverter
    fun toBelongsToCollection(value: String?): BelongsToCollection? {
        return Gson().fromJson(value, object : TypeToken<BelongsToCollection?>() {}.type)
    }

    @TypeConverter
    fun toString(list: BelongsToCollection?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}