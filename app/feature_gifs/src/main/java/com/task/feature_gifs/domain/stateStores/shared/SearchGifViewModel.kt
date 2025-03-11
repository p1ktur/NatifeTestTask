package com.task.feature_gifs.domain.stateStores.shared

import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.feature_gifs.data.repositories.SearchRepository
import com.task.feature_gifs.domain.model.connection.ConnectionManager
import com.task.feature_gifs.domain.stateStores.search.SearchUiAction
import com.task.feature_gifs.domain.stateStores.search.SearchUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchGifViewModel(
    private val connectionManager: ConnectionManager,
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    // Gifs Search
    private var lastSearchPage = 1
    private var allSearchPagesReached = false
    private var lastSearchText = ""

    init {
        viewModelScope.launch {
            snapshotFlow { uiState.value.searchText.text }
                .debounce(500)
                .collectLatest { text ->
                    searchGifs(text.toString())
                }
        }

        connectionManager.registerCallback(
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    allSearchPagesReached = false
                }
            }
        )
    }

    fun onUiAction(action: SearchUiAction) {
        when (action) {
            SearchUiAction.NavigateBack -> Unit
            is SearchUiAction.NavigateToGif -> Unit

            SearchUiAction.LoadNextPage -> viewModelScope.launch { searchGifs() }

            is SearchUiAction.BanGif -> banGif(action.index, action.id)
        }
    }

    // Actions

    private suspend fun searchGifs(searchText: String? = null) {
        val text = searchText ?: uiState.value.searchText.text.toString()
        if (text.isEmpty()) {
            if (lastSearchPage == 1) {
                return
            } else {
                lastSearchText = ""
                lastSearchPage = 1
                allSearchPagesReached = false

                _uiState.update {
                    it.copy(
                        loadingGifs = false,
                        resultsAmount = 0,
                        searchedGifs = emptyList()
                    )
                }
            }
        }

        _uiState.update {
            it.copy(
                loadingGifs = true
            )
        }

        val isFilteringOn = if (text != lastSearchText) {
            lastSearchText = text
            lastSearchPage = 1
            allSearchPagesReached = false
            true
        } else {
            if (allSearchPagesReached) {
                _uiState.update {
                    it.copy(
                        loadingGifs = false
                    )
                }
                return
            }
            false
        }

        val perPage = 10
        var gifs = searchRepository.searchGifs(
            page = lastSearchPage++,
            perPage = perPage,
            searchText = text
        )

        if (gifs.size < perPage) allSearchPagesReached = true

        gifs = searchRepository.filterBannedGifs(gifs)

        if (gifs.isEmpty()) {
            _uiState.update {
                it.copy(
                    searchedGifs = emptyList(),
                    resultsAmount = 0,
                    loadingGifs = false
                )
            }
        } else {
            val newRecipes = if (isFilteringOn) {
                uiState.value.searchedGifs.filterOn(gifs)
            } else {
                (uiState.value.searchedGifs + gifs).distinctBy { it.id }
            }

            _uiState.update {
                it.copy(
                    searchedGifs = newRecipes,
                    resultsAmount = newRecipes.size,
                    loadingGifs = false
                )
            }
        }
    }

    private fun banGif(index: Int, id: String) {
        viewModelScope.launch {
            val removedItem = uiState.value.searchedGifs[index]

            _uiState.update {
                it.copy(
                    searchedGifs = it.searchedGifs.minus(removedItem)
                )
            }

            searchRepository.banGif(id)
        }
    }

    // Not Actions

    private fun <T> List<T>.filterOn(other: List<T>): List<T> {
        val filtered = filter { other.contains(it) }
        return filtered + other.filterNot { contains(it) }
    }

    override fun onCleared() {
        super.onCleared()

        connectionManager.unregisterCallbacks()
    }
}