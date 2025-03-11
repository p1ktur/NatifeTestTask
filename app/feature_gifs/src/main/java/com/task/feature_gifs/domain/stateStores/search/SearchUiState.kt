package com.task.feature_gifs.domain.stateStores.search

import androidx.compose.foundation.text.input.TextFieldState
import com.task.feature_gifs.domain.model.gifs.GifItem

data class SearchUiState(
    val searchText: TextFieldState = TextFieldState(),
    val loadingGifs: Boolean = false,
    val searchedGifs: List<GifItem> = emptyList(),
    val resultsAmount: Int = 0,
)
