package com.topjohnwu.magisk.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.topjohnwu.magisk.core.Config
import com.topjohnwu.magisk.ui.main.NovaBackground
import com.topjohnwu.magisk.ui.main.NovaPrimary
import com.topjohnwu.magisk.ui.main.NovaSurface
import com.topjohnwu.magisk.ui.main.coreFeatures
import com.topjohnwu.magisk.ui.main.hideFeatures
import com.topjohnwu.magisk.ui.main.novaToolsFeatures

@Composable
fun SettingsScreen() {
    val all88Features = remember { coreFeatures + hideFeatures + novaToolsFeatures }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovaBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "NOVA ENGINE AYARLARI",
            color = NovaPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "88 Özelliğin Kontrol Merkezi (Aktif Native Tetikleyici)",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(all88Features) { feature ->
                SettingCheckboxCard(featureId = feature.first, title = feature.first, description = feature.second)
            }
        }
    }
}

@Composable
fun SettingCheckboxCard(featureId: String, title: String, description: String) {
    // Özelliğin durumunu doğrudan sistem konfigürasyonundan oku ve kaydet
    var isChecked by remember { mutableStateOf(Config.get<Boolean>(featureId, true)) }

    Card(
        colors = CardDefaults.cardColors(containerColor = NovaSurface),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                isChecked = !isChecked 
                // Sistem ve C++ daemon katmanına değişikliği bildir
                Config.set(featureId, isChecked)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Text(
                    text = description,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
            Text(
                text = if (isChecked) "✅" else "⬛",
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
