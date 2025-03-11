package com.task.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val colorScheme = lightColorScheme(
    primary = DarkerBlue,
    secondary = Blue,
    tertiary = LighterBlue
)

@Composable
fun NatifeTestTaskTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = colorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}