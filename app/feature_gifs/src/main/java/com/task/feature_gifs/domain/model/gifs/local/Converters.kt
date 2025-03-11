package com.task.feature_gifs.domain.model.gifs.local

import androidx.room.TypeConverter
import com.task.feature_gifs.domain.model.gifs.GifImages
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromGifImages(images: GifImages): String {
        return Json.encodeToString(images)
    }

    @TypeConverter
    fun toGifImages(json: String): GifImages {
        return Json.decodeFromString(json)
    }
}