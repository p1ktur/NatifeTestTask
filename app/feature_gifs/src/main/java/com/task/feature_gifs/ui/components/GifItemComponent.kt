package com.task.feature_gifs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.task.feature_gifs.R
import com.task.feature_gifs.domain.model.gifs.GifItem
import com.task.feature_gifs.domain.model.orientation.Orientation

@Composable
fun GifItem(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    orientation: Orientation,
    gifItem: GifItem,
    index: Int,
    lastIndex: Int,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    var isDismissed by remember { mutableStateOf(false) }
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isDismissed = true
                true
            } else {
                false
            }
        }
    )
    val isInSwipe by remember {
        derivedStateOf {
            state.dismissDirection == SwipeToDismissBoxValue.EndToStart
        }
    }
    LaunchedEffect(isDismissed) {
        if (isDismissed) {
            onDismiss()
        }
    }

    SwipeToDismissBox(
        state = state,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val backgroundColor = if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                Color.Transparent
            }

            val iconTint = if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                MaterialTheme.colorScheme.onErrorContainer
            } else {
                Color.Transparent
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(48.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon",
                    tint = iconTint
                )
            }
        },
        content = {
            when (orientation) {
                Orientation.HORIZONTAL -> {
                    HorizontalGifItem(
                        modifier = modifier,
                        imageLoader = imageLoader,
                        isInSwipe = isInSwipe,
                        gifItem = gifItem,
                        index = index,
                        lastIndex = lastIndex,
                        onClick = onClick
                    )
                }
                Orientation.VERTICAL -> {
                    VerticalGifItem(
                        modifier = modifier,
                        imageLoader = imageLoader,
                        isInSwipe = isInSwipe,
                        gifItem = gifItem,
                        index = index,
                        lastIndex = lastIndex,
                        onClick = onClick
                    )
                }
            }
        }
    )
}

@Composable
fun VerticalGifItem(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    isInSwipe: Boolean,
    gifItem: GifItem,
    index: Int,
    lastIndex: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(0.dp))
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .clip(RoundedCornerShape(16.dp)),
            model = gifItem.images.preview.url,
            imageLoader = imageLoader,
            placeholder = painterResource(R.drawable.gif_placeholder),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )
        if (gifItem.title.isNotBlank()) {
            Text(
                text = gifItem.title,
                style = MaterialTheme.typography.titleSmall,
                color = if (isInSwipe) {
                    MaterialTheme.colorScheme.onErrorContainer
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        }
        if (gifItem.description.isNotBlank()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = gifItem.description,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isInSwipe) {
                    MaterialTheme.colorScheme.onErrorContainer
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        }
        if (index != lastIndex) {
            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun HorizontalGifItem(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    isInSwipe: Boolean,
    gifItem: GifItem,
    index: Int,
    lastIndex: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(0.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(144.dp)
                    .clip(RoundedCornerShape(16.dp)),
                model = gifItem.images.preview.url,
                imageLoader = imageLoader,
                placeholder = painterResource(R.drawable.gif_placeholder),
                contentScale = ContentScale.FillHeight,
                contentDescription = null
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (gifItem.title.isNotBlank()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = gifItem.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isInSwipe) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
                if (gifItem.description.isNotBlank()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = gifItem.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isInSwipe) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
            }
        }
        if (index != lastIndex) {
            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
        }
    }
}