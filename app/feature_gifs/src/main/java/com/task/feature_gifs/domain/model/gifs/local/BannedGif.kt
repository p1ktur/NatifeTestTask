package com.task.feature_gifs.domain.model.gifs.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BannedGif(
    @PrimaryKey val id: String
)
