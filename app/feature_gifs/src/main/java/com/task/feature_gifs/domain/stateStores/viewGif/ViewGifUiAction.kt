package com.task.feature_gifs.domain.stateStores.viewGif

sealed interface ViewGifUiAction {
    data object NavigateBack : ViewGifUiAction
}