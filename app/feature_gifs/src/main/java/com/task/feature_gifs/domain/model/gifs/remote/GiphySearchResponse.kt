package com.task.feature_gifs.domain.model.gifs.remote

import com.task.feature_gifs.domain.model.gifs.GifItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GiphySearchResponse(
    @SerialName("data") val gifItems: List<GifItem>,
    val pagination: Pagination,
    val meta: Meta
)