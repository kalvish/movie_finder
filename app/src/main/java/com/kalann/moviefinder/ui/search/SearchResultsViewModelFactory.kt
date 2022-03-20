package com.kalann.moviefinder.ui.search

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.kalann.moviefinder.movies.MovieDataRepository

class SearchResultsViewModelFactory (
    private val initialQuery: String,
    owner: SavedStateRegistryOwner,
    private val movieDataRepository: MovieDataRepository
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(SearchResultsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchResultsViewModel(initialQuery, movieDataRepository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}