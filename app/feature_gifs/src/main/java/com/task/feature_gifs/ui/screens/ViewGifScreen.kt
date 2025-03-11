package com.task.feature_gifs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.task.feature_gifs.R
import com.task.feature_gifs.domain.model.gifs.GifItem
import com.task.feature_gifs.domain.model.imageLoader.getCachingImageLoader
import com.task.feature_gifs.domain.model.orientation.Orientation
import com.task.feature_gifs.domain.model.orientation.getOrientation
import com.task.feature_gifs.domain.stateStores.viewGif.ViewGifUiAction

@Composable
fun ViewGifScreen(
    gifItems: List<GifItem>,
    startIndex: Int,
    onUiAction: (ViewGifUiAction) -> Unit
) {
    val context = LocalContext.current
    val imageLoader = remember { getCachingImageLoader(context) }

    val configuration = LocalConfiguration.current
    val orientation = remember { getOrientation(configuration.orientation) }

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { gifItems.size }
    )

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable {
                        onUiAction(ViewGifUiAction.NavigateBack)
                    }
                    .padding(6.dp),
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back Button",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF111111)),
            state = pagerState
        ) { index ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (orientation) {
                    Orientation.HORIZONTAL -> {
                        AsyncImage(
                            modifier = Modifier.fillMaxHeight(),
                            model = gifItems[index].images.fixedHeight.url,
                            imageLoader = imageLoader,
                            placeholder = painterResource(R.drawable.gif_placeholder),
                            contentScale = ContentScale.FillHeight,
                            contentDescription = null
                        )
                    }
                    Orientation.VERTICAL -> {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = gifItems[index].images.fixedWidth.url,
                            imageLoader = imageLoader,
                            placeholder = painterResource(R.drawable.gif_placeholder),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}