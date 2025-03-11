package com.task.feature_gifs.domain.model.gifs

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class GifItem(
    @PrimaryKey val id: String,
    val slug: String,
    val title: String,
    @SerialName("alt_text") val description: String,
    val images: GifImages
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GifItem

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}