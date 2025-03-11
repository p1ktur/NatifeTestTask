package com.task.feature_gifs.domain.stateStores.search

sealed interface SearchUiAction {
    data object NavigateBack : SearchUiAction
    data class NavigateToGif(val index: Int) : SearchUiAction

    data object LoadNextPage : SearchUiAction

    data class BanGif(val index: Int, val id: String) : SearchUiAction
}