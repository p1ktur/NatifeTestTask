package com.task.feature_gifs.domain.model.gifs.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    @SerialName("msg") val message: String,
    val status: Int,
    @SerialName("response_id") val responseId: String
)
