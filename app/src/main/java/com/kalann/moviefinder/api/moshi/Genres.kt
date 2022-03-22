package com.kalann.moviefinder.api.moshi

import com.squareup.moshi.Json

data class Genres (
    @Json(name = "genres")
    var genres: List<Genre>? = null
)