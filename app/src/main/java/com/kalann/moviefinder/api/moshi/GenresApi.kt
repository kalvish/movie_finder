package com.kalann.moviefinder.api.moshi

import com.squareup.moshi.Json

data class GenresApi (
    @Json(name = "genres")
    var genres: List<Genre>? = null
)