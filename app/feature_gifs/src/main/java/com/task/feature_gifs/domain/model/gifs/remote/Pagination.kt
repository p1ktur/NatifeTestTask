package com.task.feature_gifs.domain.model.gifs.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val offset: Int,
    @SerialName("total_count") val totalCount: Int,
    val count: Int
)
