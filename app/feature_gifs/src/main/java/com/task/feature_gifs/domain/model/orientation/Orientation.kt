package com.task.feature_gifs.domain.model.orientation

import android.content.res.Configuration

enum class Orientation {
    HORIZONTAL,
    VERTICAL
}

fun getOrientation(orientation: Int): Orientation {
    if (orientation == Configuration.ORIENTATION_PORTRAIT) return Orientation.VERTICAL
    return Orientation.HORIZONTAL
}