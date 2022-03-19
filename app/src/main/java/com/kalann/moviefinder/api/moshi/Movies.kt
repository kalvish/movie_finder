package com.kalann.moviefinder.api.moshi

import com.squareup.moshi.Json

data class Movies (
    @Json(name = "dates")
    var dates: Dates? = null,

    @Json(name = "page")
    var page: Int? = null,

    @Json(name = "results")
    var results: List<Movie>? = null,

    @Json(name = "total_pages")
    var totalPages: Int? = null,

    @Json(name = "total_results")
    var totalResults: Int? = null
)
