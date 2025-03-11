package com.task.feature_gifs.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.task.feature_gifs.domain.stateStores.search.SearchUiAction
import com.task.feature_gifs.domain.stateStores.shared.SearchGifViewModel
import com.task.feature_gifs.domain.stateStores.viewGif.ViewGifUiAction
import com.task.feature_gifs.ui.screens.SearchScreen
import com.task.feature_gifs.ui.screens.ViewGifScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

sealed interface GifsNavRoutes {
    @Serializable data object Search : GifsNavRoutes
    @Serializable data class ViewGif(val index: Int) : GifsNavRoutes
}

fun NavGraphBuilder.addGifsRoutes(
    navController: NavController
) {
    composable<GifsNavRoutes.Search> {
        val backStackEntry = navController.getBackStackEntry(GifsNavRoutes.Search)

        val viewModel = koinViewModel<SearchGifViewModel>(viewModelStoreOwner = backStackEntry)
        val uiState by viewModel.uiState.collectAsState()

        SearchScreen(
            uiState = uiState,
            onUiAction = { action ->
                when (action) {
                    SearchUiAction.NavigateBack -> navController.navigateUp()
                    is SearchUiAction.NavigateToGif -> navController.navigate(GifsNavRoutes.ViewGif(action.index))
                    else -> Unit
                }
                viewModel.onUiAction(action)
            }
        )
    }
    composable<GifsNavRoutes.ViewGif> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<GifsNavRoutes.ViewGif>()
        val index = route.index

        val backStackEntry = navController.getBackStackEntry(GifsNavRoutes.Search)

        val viewModel = koinViewModel<SearchGifViewModel>(viewModelStoreOwner = backStackEntry)

        ViewGifScreen(
            gifItems = viewModel.uiState.value.searchedGifs,
            startIndex = index,
            onUiAction = { action ->
                when (action) {
                    ViewGifUiAction.NavigateBack -> navController.navigateUp()
                }
            }
        )
    }
}