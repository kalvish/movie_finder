package com.kalann.moviefinder.ui.search

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.kalann.moviefinder.MFinApplication
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.dagger.MoviesComponent
import com.kalann.moviefinder.databinding.ActivitySearchResultsBinding
import com.kalann.moviefinder.movies.MovieAdapter
import com.kalann.moviefinder.movies.MovieDataRepository
import com.kalann.moviefinder.ui.now_playing.NwPlayingMoviesAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchResultsActivity : AppCompatActivity() {
    lateinit var moviesComponent: MoviesComponent
    lateinit var activitySearchResultsBinding: ActivitySearchResultsBinding

    @Inject
    lateinit var moviesDataRepository: MovieDataRepository
    lateinit var viewModel: SearchResultsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        moviesComponent = (applicationContext as MFinApplication)
            .daggerAppComponent.moviesComponent().create()
        moviesComponent.inject(this)
        super.onCreate(savedInstanceState)
        activitySearchResultsBinding = ActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(activitySearchResultsBinding.root)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        activitySearchResultsBinding.list.addItemDecoration(decoration)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            var query = intent.getStringExtra(SearchManager.QUERY)
            if(query==null) query = "Kids"
            viewModel = ViewModelProvider(this, SearchResultsViewModelFactory(query, owner = this, moviesDataRepository))
                .get(SearchResultsViewModel::class.java)
            activitySearchResultsBinding.bindState(
                uiState = viewModel.state,
                pagingData = viewModel.pagingDataFlow,
                uiActions = viewModel.accept
            )
        }
    }

    private fun ActivitySearchResultsBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<Movie>>,
        uiActions: (UiAction) -> Unit
    ) {
        val movieAdapter = MovieAdapter(object : MovieAdapter.OnMovieClickListener {
            override fun onClickListItem(movie: Movie) {
            }
        })
        list.adapter = movieAdapter

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            movieAdapter = movieAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun ActivitySearchResultsBinding.bindSearch(
        uiState: StateFlow<UiState>,
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        searchMovie.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateMovieListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        searchMovie.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateMovieListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(searchMovie::setText)
        }
    }

    private fun ActivitySearchResultsBinding.updateMovieListFromInput(onQueryChanged: (UiAction.Search) -> Unit) {
        searchMovie.text.trim().let {
            if (it.isNotEmpty()) {
                list.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun ActivitySearchResultsBinding.bindList(
        movieAdapter: MovieAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<Movie>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })
        val notLoading = movieAdapter.loadStateFlow
            .distinctUntilChangedBy { it.source.refresh }
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(movieAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) list.scrollToPosition(0)
            }
        }
    }
}