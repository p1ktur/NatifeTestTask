package com.task.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val colorScheme = lightColorScheme(
    primary = DarkerBlue,
    onPrimary = Color.White,
    secondary = Blue,
    onSecondary = Color.White,
    tertiary = LighterBlue,
    onTertiary = Color.White,
    errorContainer = DarkerRed,
    onErrorContainer = Color.White
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