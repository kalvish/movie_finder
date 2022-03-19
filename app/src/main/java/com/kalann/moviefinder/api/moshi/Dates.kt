package com.kalann.moviefinder.api.moshi


import com.squareup.moshi.Json

data class Dates (
    @Json(name = "maximum")
    var maximum: String? = null,

    @Json(name = "minimum")
    var minimum: String? = null
)
