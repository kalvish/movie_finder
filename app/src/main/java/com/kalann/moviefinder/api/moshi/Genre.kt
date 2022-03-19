package com.kalann.moviefinder.api.moshi

import com.squareup.moshi.Json

data class Genre (
    @Json(name = "id")
    var id: Int? = null,

    @Json(name = "name")
    var name: String? = null
)