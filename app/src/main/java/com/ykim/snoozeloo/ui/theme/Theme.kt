package com.ykim.snoozeloo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SnoozelooPrimary,
    onPrimary = SnoozelooWhite,
    surface = SnoozelooBackground,
    onSurface = SnoozelooBlack,
    primaryContainer = SnoozelooPrimaryContainer,
)

@Composable
fun SnoozelooTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}