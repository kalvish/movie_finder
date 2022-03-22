package com.kalann.moviefinder.api.moshi

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Genre (
    @PrimaryKey
    @Json(name = "id")
    var id: Int,

    @Json(name = "name")
    var name: String? = null
)