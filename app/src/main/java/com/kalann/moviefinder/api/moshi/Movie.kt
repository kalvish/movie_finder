package com.kalann.moviefinder.api.moshi

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Movie (
    @Json(name = "adult")
    var adult: Boolean? = null,

    @Json(name = "backdrop_path")
    var backdropPath: String? = null,

    @Json(name = "belongs_to_collection")
    var belongsToCollection: BelongsToCollection? = null,

    @Json(name = "budget")
    var budget: Int? = null,

    @Json(name = "genres")
    var genres: List<Genre>? = null,

    @Json(name = "homepage")
    var homepage: String? = null,

    @PrimaryKey(autoGenerate = false)
    @Json(name = "id")
    var id: Int,

    @Json(name = "imdb_id")
    var imdbId: String? = null,

    @Json(name = "original_language")
    var originalLanguage: String? = null,

    @Json(name = "original_title")
    var originalTitle: String? = null,

    @Json(name = "overview")
    var overview: String? = null,

    @Json(name = "popularity")
    var popularity: Double? = null,

    @Json(name = "poster_path")
    var posterPath: String? = null,

    @Json(name = "production_companies")
    var productionCompanies: List<ProductionCompany>? = null,

    @Json(name = "production_countries")
    var productionCountries: List<ProductionCountry>? = null,

    @Json(name = "release_date")
    var releaseDate: String? = null,

    @Json(name = "revenue")
    var revenue: Int? = null,

    @Json(name = "runtime")
    var runtime: Int? = null,

    @Json(name = "spoken_languages")
    var spokenLanguages: List<SpokenLanguage>? = null,

    @Json(name = "status")
    var status: String? = null,

    @Json(name = "tagline")
    var tagline: String? = null,

    @Json(name = "title")
    var title: String? = null,

    @Json(name = "video")
    var video: Boolean? = null,

    @Json(name = "vote_average")
    var voteAverage: Double? = null,

    @Json(name = "vote_count")
    var voteCount: Int? = null,

    @Ignore
    @Json(name = "genre_ids")
    val genre_ids: List<Int>? = null
) {
    constructor(adult: Boolean?,backdropPath: String?,belongsToCollection: BelongsToCollection?
                ,budget: Int?,genres: List<Genre>?,homepage: String?
                ,id: Int,imdbId: String?,originalLanguage: String?
                ,originalTitle: String?,overview: String?,popularity: Double?
                ,posterPath: String?,productionCompanies: List<ProductionCompany>?
                ,productionCountries: List<ProductionCountry>?,releaseDate: String?
                ,revenue: Int?,runtime: Int?,spokenLanguages: List<SpokenLanguage>?
                ,status: String?,tagline: String?,title: String?
                ,video: Boolean?,voteAverage: Double?,voteCount: Int?
                ) : this(adult, backdropPath, belongsToCollection, budget, genres, homepage, id, imdbId, originalLanguage, originalTitle, overview, popularity, posterPath, productionCompanies, productionCountries, releaseDate, revenue, runtime, spokenLanguages, status, tagline, title, video, voteAverage, voteCount, emptyList<Int>())
}
