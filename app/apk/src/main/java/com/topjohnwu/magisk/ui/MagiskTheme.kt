package com.topjohnwu.magisk.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val NovaBackground = Color(0xFF05050A)
val NovaSurface = Color(0xFF0E0E17)
val NovaPrimary = Color(0xFF00FFC8)
val NovaSecondary = Color(0xFF9D00FF)

private val NovaColorScheme = darkColorScheme(
    background = NovaBackground,
    surface = NovaSurface,
    primary = NovaPrimary,
    secondary = NovaSecondary
)

@Composable
fun MagiskTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NovaColorScheme,
        content = content
    )
}
