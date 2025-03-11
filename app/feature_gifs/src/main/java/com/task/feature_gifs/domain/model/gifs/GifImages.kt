package com.task.feature_gifs.domain.model.gifs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GifImages(
    @SerialName("preview_gif") val preview: UrlWrapper,
    @SerialName("fixed_width") val fixedWidth: UrlWrapper,
    @SerialName("fixed_height") val fixedHeight: UrlWrapper
)
