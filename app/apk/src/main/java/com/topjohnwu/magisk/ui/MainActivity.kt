package com.topjohnwu.magisk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.topjohnwu.magisk.ui.main.MainScreen
import com.topjohnwu.magisk.ui.theme.MagiskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagiskTheme {
                MainScreen()
            }
        }
    }
}
