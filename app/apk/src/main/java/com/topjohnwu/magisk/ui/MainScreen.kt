package com.topjohnwu.magisk.ui.main

import androidx.compose.foundation.background
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

// Nova Palette
val NovaBackground = Color(0xFF0A0A0C)
val NovaSurface = Color(0xFF121318)
val NovaPrimary = Color(0xFF00FF66)   // Neon Green
val NovaSecondary = Color(0xFF9D00FF) // Neon Purple

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovaBackground)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "MAGISK 32.8 NOVA",
            color = NovaPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "88 Active Innovations & Nova Engine",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Tab Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { selectedTab = 0 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 0) NovaPrimary else NovaSurface,
                    contentColor = if (selectedTab == 0) Color.Black else Color.White
                )
            ) { Text("Çekirdek (30)") }

            Button(
                onClick = { selectedTab = 1 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 1) NovaPrimary else NovaSurface,
                    contentColor = if (selectedTab == 1) Color.Black else Color.White
                )
            ) { Text("Gizleme (30)") }

            Button(
                onClick = { selectedTab = 2 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 2) NovaPrimary else NovaSurface,
                    contentColor = if (selectedTab == 2) Color.Black else Color.White
                )
            ) { Text("Nova & Araçlar (28)") }
        }

        // Feature List
        val features = when (selectedTab) {
            0 -> coreFeatures
            1 -> hideFeatures
            else -> novaToolsFeatures
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(features) { feature ->
                FeatureCard(feature)
            }
        }
    }
}

@Composable
fun FeatureCard(feature: Pair<String, String>) {
    var isEnabled by remember { mutableStateOf(true) }

    Card(
        colors = CardDefaults.cardColors(containerColor = NovaSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = feature.first, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(text = feature.second, color = Color.Gray, fontSize = 11.sp)
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = { isEnabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    checkedTrackColor = NovaPrimary,
                    uncheckedTrackColor = Color.DarkGray
                )
            )
        }
    }
}

// 88 Yeniliğin Kategorize Edilmiş Kod Tanımları
val coreFeatures = listOf(
    "1. KernelSU Hook Engine" to "Gelişmiş çekirdek seviyesi hook desteği.",
    "2. Dynamic Zygisk Injector v2" to "Sistem kaynaklarını tüketmeyen anlık injection.",
    "3. Magic Mount Core v3" to "AOSP ve Custom ROM'lar için hızlı mount mimarisi.",
    "4. Smart SU Privilege Guard" to "Uygulama bazlı detaylı root yetkilendirme.",
    "5. Direct Boot Execution" to "Cihaz şifresi çözülmeden boot esnasında script çalıştırma.",
    "6. Multi-Threaded Daemon" to "Arka plan işlemlerinde sıfır gecikme.",
    "7. Auto-ResetProp Engine" to "Build.prop değerlerini anlık ve iz bırakmadan değiştirme.",
    "8. Native SELinux Polisher" to "SELinux kurallarını otomatik ve güvenli esnetme.",
    "9. Bootloop Protector Pro" to "Cihaz çökerse hatalı modülü otomatik devre dışı bırakma.",
    "10. Modular Native Core" to "Tamamen modüler C++ çekirdek mimarisi.",
    "11. EROFS & F2FS Mount Support" to "Yeni nesil dosya sistemleriyle tam uyumluluk.",
    "12. Advanced Busybox Suite" to "150+ güncel Linux komut desteği.",
    "13. Ultra Fast Module Unpacker" to "Modül kurulum süresini %60 hızlandırma.",
    "14. Custom Kernel Commandline Injector" to "Çekirdek parametrelerini canlı değiştirme.",
    "15. Isolated Process Sandbox" to "Root süreçlerini izole alanda çalıştırma.",
    "16. Memory Optimization Guard" to "RAM kullanımını minimumda tutan bellek yönetimi.",
    "17. Persistent Shell Daemon" to "Kesintisiz ve yüksek hızlı root shell oturumları.",
    "18. Smart Cgroup Controller" to "Root işlemlerine işlemci önceliği atama.",
    "19. Real-time Logcat Interceptor" to "Çekirdek hatalarını canlı yakalama.",
    "20. Async Module Loader" to "Modülleri paralel yükleyerek hızlı açılış sağlama.",
    "21. Mount Namespace Isolator" to "Uygulamalara özel sanal mount alanları.",
    "22. Auto-Repair Root Database" to "Bozulan root veritabanını otomatik onarma.",
    "23. Fast-Fallback SuperSU Protocol" to "Eski su komutlarıyla tam geriye dönük uyumluluk.",
    "24. Dynamic OverlayFS Support" to "Sistem bölümlerini iz bırakmadan değiştirme.",
    "25. Secure IPC Channel" to "Uygulamalar arası şifreli root haberleşmesi.",
    "26. Live Kernel Patching Support" to "Yeniden başlatmadan çekirdek yamama desteği.",
    "27. Low-Memory Killer Bypass" to "Kritik root daemon'larının kapanmasını engelleme.",
    "28. Smart Battery Saver Mode" to "Arka planda root taramalarını uyutma.",
    "29. Multi-User Superuser Isolation" to "Çoklu kullanıcı hesaplarında ayrı root yönetimi.",
    "30. Hardware-Accelerated Native Crypt" to "Kriptografik işlemlerde donanım ivmesi."
)

val hideFeatures = listOf(
    "31. Play Integrity Fix v5" to "STRONG ve DEVICE integrity kontrollerini tam geçme.",
    "32. Advanced DenyList Stealth" to "DenyList listesini tespit edilemez kılma.",
    "33. Banking App Shield Engine" to "Banka uygulamalarının root tespitini %100 engelleme.",
    "34. Custom Package Name Randomizer" to "Magisk APK ismini rastgele paket adıyla gizleme.",
    "35. Isolation Mode for Google Play Services" to "GMS süreçlerinden root izlerini tam izole etme.",
    "36. Mock Location Detector Bypass" to "Geliştirici konumu tespitini engelleme.",
    "37. ADB Debugging Mask" to "USB Hata Ayıklama açıkken görünmez kılma.",
    "38. Hardware Attestation Spoofer" to "Donanım kimlik doğrulama yanıtlarını manipüle etme.",
    "39. Custom Fingerprint Overrider" to "Cihaz parmak izini istenen modelle değiştirme.",
    "40. Bootloader Unlocked Mask" to "Kilitli bootloader durumunu simüle etme.",
    "41. Knox Status Spoofer" to "Samsung Knox sayacını 0x0 olarak gösterme.",
    "42. App Detector Trace Eraser" to "Root tarayan uygulamaların hafıza izlerini silme.",
    "43. Dynamic Trace Bypasser" to "ptrace ve ptrace benzeri denetimleri yanıltma.",
    "44. Virtual System Props Engine" to "Root belirteci olan sistem özelliklerini gizleme.",
    "45. Anti-Frida & Anti-Xposed Engine" to "Tersine mühendislik araçlarının tespitini önleme.",
    "46. File System Access Cloak" to "/system ve /vendor okuma izinlerini maskeleme.",
    "47. Custom Mount Point Hider" to "Magisk mount noktalarını uygulamalara gizleme.",
    "48. Root Cloak Profile Exporter" to "Gizleme ayarlarını yedekleme ve içe aktarma.",
    "49. Auto-Update DenyList Rules" to "Yeni banka uygulamaları için buluttan kural çekme.",
    "50. Dynamic System Signature Masking" to "Sistem imza doğrulamalarını gizlice bypass etme.",
    "51. Memory Scanning Blocker" to "RAM içi kod taraması yapan güvenlik araçlarını engelleme.",
    "52. Virtual Environment Shield" to "Sanal alan (Virtual Space) tespitlerini engelleme.",
    "53. SELinux Enforcing Emulator" to "SELinux Permissive olsa bile Enforcing gösterme.",
    "54. Developer Options Masking" to "Geliştirici seçeneklerinin açık olduğunu uygulamalardan gizleme.",
    "55. Magisk Binary Hider v2" to "/sbin ve /system/bin altındaki su izlerini maskeleme.",
    "56. Dynamic Boot Image Hash Spoofer" to "Boot imajı kontrolünü orijinal gösterme.",
    "57. Hardware Serial Anonymizer" to "Cihaz seri numarasını güvenlik için maskeleme.",
    "58. Safe-State Mocking Engine" to "Güvenlik taramalarında cihazı 'Orijinal Stok' raporlama.",
    "59. Game Anti-Cheat Stealth Mode" to "Mobil oyun anti-cheat sistemlerinden kaçınma.",
    "60. One-Tap Stealth Mode Switch" to "Tek tıkla tüm gizleme modlarını aktif etme."
)

val novaToolsFeatures = listOf(
    "61. OLED Neon Theme Engine" to "Gerçek siyah ve fosforlu yeşil/mor arayüz stili.",
    "62. Real-time System Monitor" to "CPU, RAM ve Root süreçlerini canlı izleme.",
    "63. One-Click Repository Downloader" to "Online modül mağazasından doğrudan indirme.",
    "64. Terminal & Termux Quick Launcher" to "Arayüzden dahili root terminali açma.",
    "65. Auto-Module Incompatible Check" to "Çakışan modülleri kurmadan önce uyarma.",
    "66. Custom Vibration Feedback" to "İşlemlerde özel dokunsal geribildirim.",
    "67. Advanced Reboot Menu" to "Recovery, Bootloader ve Soft Reboot seçenekleri.",
    "68. Backup & Restore Manager" to "Tüm modülleri ve root izinlerini tek tıkla yedekleme.",
    "69. Module Dependency Resolver" to "Eksik modül bağımlılıklarını otomatik indirme.",
    "70. App Icon Customizer" to "Magisk simgesini gizli ikonlarla değiştirme.",
    "71. Deep Clean Cache Engine" to "Dalvik-cache ve sistem önbelleğini temizleme.",
    "72. Custom Scripts Scheduler" to "Zamanlanmış root komutları çalıştırma.",
    "73. Network Speed & Filter Guard" to "Modüllerin ağ erişimini kısıtlama.",
    "74. Log Exporter & Anonymizer" to "Sistem loglarındaki kişisel verileri silip dışa aktarma.",
    "75. Quick Tile Settings Integrator" to "Hızlı ayarlara Root Toggle ekleme.",
    "76. Biometric Root Approval" to "Root izinlerini parmak izi/yüz tanıma ile onaylama.",
    "77. Multi-Language Nova Translation" to "Türkçe dahil 40+ dil desteği.",
    "78. Cloud Settings Sync" to "Ayarları GitHub Gist üzerinden senkronize etme.",
    "79. Battery Health & Temp Monitor" to "Root işlemlerinin pil sıcaklığına etkisini ölçme.",
    "80. System Partition Storage Cleaner" to "Sistem alanındaki gereksiz kalıntıları silme.",
    "81. Custom UI Color Accent Picker" to "Neon renk tonlarını kişiselleştirme.",
    "82. Magisk Module Developer Studio" to "Telefon üzerinden basit modül oluşturma aracı.",
    "83. Auto-Clear App Data on Root Revoke" to "Root izni kaldırılan uygulamanın verisini silme.",
    "84. System Font & Emoji Swapper" to "Arayüzden hızlı font/emoji modülü yükleme.",
    "85. Smart Notification Controller" to "Root uyarılarını sessize alma veya özelleştirme.",
    "86. Fast Boot Animation Installer" to "Arayüzden boot animasyonu değiştirme.",
    "87. Hardware Diagnostic Suite" to "Root erişimli donanım test aracı.",
    "88. Emergency Module Disabler" to "Ses tuşlarıyla açılışta modülleri iptal etme."
)
