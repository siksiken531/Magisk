package com.topjohnwu.magisk.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.topjohnwu.magisk.ui.main.NovaBackground
import com.topjohnwu.magisk.ui.main.NovaPrimary
import com.topjohnwu.magisk.ui.main.NovaSecondary
import com.topjohnwu.magisk.ui.main.NovaSurface

private val DarkColorScheme = darkColorScheme(
    primary = NovaPrimary,
    secondary = NovaSecondary,
    background = NovaBackground,
    surface = NovaSurface
)

@Composable
fun MagiskTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
