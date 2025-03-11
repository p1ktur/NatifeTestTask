package com.task.feature_gifs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.task.feature_gifs.domain.model.imageLoader.getCachingImageLoader
import com.task.feature_gifs.domain.model.orientation.getOrientation
import com.task.feature_gifs.domain.stateStores.search.SearchUiAction
import com.task.feature_gifs.domain.stateStores.search.SearchUiState
import com.task.feature_gifs.ui.components.GifItem
import com.task.feature_gifs.ui.components.LoadingIndicator
import com.task.feature_gifs.ui.components.SearchTextField
import com.task.feature_gifs.ui.components.SmallLoadingIndicator

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onUiAction: (SearchUiAction) -> Unit
) {
    val context = LocalContext.current
    val imageLoader = remember { getCachingImageLoader(context) }

    val configuration = LocalConfiguration.current
    val orientation = remember { getOrientation(configuration.orientation) }

    val lazyColumnState = rememberLazyListState()

    val isScrolledToBottom by remember {
        derivedStateOf {
            !lazyColumnState.canScrollForward && lazyColumnState.canScrollBackward
        }
    }

    LaunchedEffect(isScrolledToBottom) {
        if (isScrolledToBottom && !uiState.loadingGifs && uiState.searchText.text.isNotEmpty()) {
            onUiAction(SearchUiAction.LoadNextPage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .windowInsetsTopHeight(WindowInsets.systemBars)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchTextField(
                modifier = Modifier.fillMaxWidth(),
                state = uiState.searchText
            )
        }
        if (uiState.resultsAmount < 1) {
            if (uiState.loadingGifs) {
                LoadingIndicator()
            } else {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Type to search",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyColumnState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(uiState.searchedGifs, key = { _, it -> it.id }) { index, gifItem ->
                    GifItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        imageLoader = imageLoader,
                        orientation = orientation,
                        gifItem = gifItem,
                        index = index,
                        lastIndex = uiState.searchedGifs.lastIndex,
                        onClick = {
                            onUiAction(SearchUiAction.NavigateToGif(index))
                        },
                        onDismiss = {
                            onUiAction(SearchUiAction.BanGif(index, gifItem.id))
                        }
                    )
                }
                if (uiState.loadingGifs) {
                    item {
                        SmallLoadingIndicator()
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(144.dp))
                }
            }
        }
    }
}