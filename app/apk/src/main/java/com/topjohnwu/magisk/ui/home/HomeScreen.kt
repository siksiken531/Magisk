package com.topjohnwu.magisk.ui.home

import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.topjohnwu.magisk.MainActivity
import com.topjohnwu.magisk.R
import com.topjohnwu.magisk.core.BuildConfig
import com.topjohnwu.magisk.core.Config
import com.topjohnwu.magisk.core.Const
import com.topjohnwu.magisk.core.Info
import com.topjohnwu.magisk.core.download.DownloadEngine
import com.topjohnwu.magisk.core.download.Subject
import com.topjohnwu.magisk.core.ktx.reboot
import com.topjohnwu.magisk.core.ktx.toast
import com.topjohnwu.magisk.core.tasks.AppMigration
import com.topjohnwu.magisk.core.tasks.MagiskInstaller
import com.topjohnwu.magisk.ui.component.MagiskDialog
import com.topjohnwu.magisk.ui.component.rememberLoadingDialog
import com.topjohnwu.magisk.ui.component.verticalScrollbar
import com.topjohnwu.magisk.ui.flash.FlashUtils
import com.topjohnwu.magisk.ui.install.InstallDialog
import com.topjohnwu.magisk.ui.install.InstallViewModel
import kotlinx.coroutines.launch
import java.io.File
import com.topjohnwu.magisk.core.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    installVm: InstallViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val loadingDialog = rememberLoadingDialog()

    var showUninstallDialog by rememberSaveable { mutableStateOf(false) }
    var showManagerDialog by rememberSaveable { mutableStateOf(false) }
    var showEnvFixDialog by rememberSaveable { mutableStateOf(false) }
    var showHideDialog by rememberSaveable { mutableStateOf(false) }
    var showRestoreDialog by rememberSaveable { mutableStateOf(false) }
    var showInstallDialog by rememberSaveable { mutableStateOf(false) }
    var envFixCode by remember { mutableIntStateOf(0) }

    LaunchedEffect(uiState.showUninstall) {
        if (uiState.showUninstall) {
            showUninstallDialog = true
            viewModel.onUninstallConsumed()
        }
    }
    LaunchedEffect(uiState.showManagerInstall) {
        if (uiState.showManagerInstall) {
            showManagerDialog = true
            viewModel.onManagerInstallConsumed()
        }
    }
    LaunchedEffect(uiState.envFixCode) {
        if (uiState.envFixCode != 0) {
            envFixCode = uiState.envFixCode
            showEnvFixDialog = true
            viewModel.onEnvFixConsumed()
        }
    }
    LaunchedEffect(uiState.showHideRestore) {
        if (uiState.showHideRestore) {
            val hidden = context.packageName != BuildConfig.APP_PACKAGE_NAME
            if (hidden) showRestoreDialog = true else showHideDialog = true
            viewModel.onHideRestoreConsumed()
        }
    }

    if (showUninstallDialog) {
        UninstallComposableDialog(
            onDismiss = { showUninstallDialog = false },
            onCompleteUninstall = {
                showUninstallDialog = false
                val intent = Intent(context, context.javaClass).apply {
                    action = FlashUtils.INTENT_FLASH
                    putExtra(FlashUtils.EXTRA_FLASH_ACTION, Const.Value.UNINSTALL)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                context.startActivity(intent)
            },
            onRestoreImage = {
                showUninstallDialog = false
                scope.launch {
                    val success = loadingDialog.withLoading {
                        MagiskInstaller.Restore().exec()
                    }
                    context.toast(
                        if (success) CoreR.string.restore_done else CoreR.string.restore_fail,
                        Toast.LENGTH_SHORT
                    )
                }
            }
        )
    }

    if (showManagerDialog) {
        ManagerInstallComposableDialog(
            cacheDir = context.cacheDir,
            onDismiss = { showManagerDialog = false },
            onInstall = {
                showManagerDialog = false
                (context as? MainActivity)?.let {
                    DownloadEngine.startWithActivity(it, Subject.App())
                }
            }
        )
    }

    if (showEnvFixDialog) {
        EnvFixComposableDialog(
            code = envFixCode,
            onDismiss = { showEnvFixDialog = false },
            onNavigateInstall = {
                showEnvFixDialog = false
                showInstallDialog = true
            },
            onFixEnv = {
                showEnvFixDialog = false
                scope.launch {
                    val success = loadingDialog.withLoading {
                        MagiskInstaller.FixEnv().exec()
                    }
                    context.toast(
                        if (success) CoreR.string.reboot_delay_toast else CoreR.string.setup_fail,
                        Toast.LENGTH_LONG
                    )
                    if (success) {
                        @Suppress("DEPRECATION")
                        Handler(Looper.getMainLooper())
                            .postDelayed({ reboot() }, 5000)
                    }
                }
            }
        )
    }

    if (showHideDialog) {
        HideAppDialog(
            onDismiss = { showHideDialog = false },
            onConfirm = { name ->
                showHideDialog = false
                scope.launch {
                    loadingDialog.withLoading {
                        AppMigration.patchAndHide(context, name)
                    }
                }
            }
        )
    }

    if (showRestoreDialog) {
        RestoreAppDialog(
            onDismiss = { showRestoreDialog = false },
            onConfirm = {
                showRestoreDialog = false
                scope.launch {
                    loadingDialog.withLoading {
                        AppMigration.restoreApp(context)
                    }
                }
            }
        )
    }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(CoreR.string.section_home)) },
                scrollBehavior = scrollBehavior,
                actions = {
                    if (Info.env.isActive) {
                        IconButton(onClick = { viewModel.onDeletePressed() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(CoreR.string.uninstall_magisk_title),
                            )
                        }
                    }
                    if (Info.isRooted) {
                        RebootButton()
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(padding)
                .verticalScrollbar(scrollState, contentPadding = PaddingValues(vertical = 12.dp))
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.isNoticeVisible) {
                NoticeCard(onHide = viewModel::hideNotice)
            }

            CoreCard(
                modifier = Modifier.fillMaxWidth(),
                state = uiState.magiskState,
                version = uiState.magiskInstalledVersion,
                onInstallClicked = { showInstallDialog = true }
            )

            StatusCard()

            AppCard(
                modifier = Modifier.fillMaxWidth(),
                state = uiState.appState,
                version = uiState.managerInstalledVersion,
                remoteVersion = uiState.managerRemoteVersion,
                progress = uiState.managerProgress,
                isHidden = context.packageName != BuildConfig.APP_PACKAGE_NAME,
                onManagerPressed = viewModel::onManagerPressed,
                onHideRestorePressed = viewModel::onHideRestorePressed,
            )

            Text(
                text = stringResource(CoreR.string.home_support_title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )

            SupportCard(onLinkClicked = viewModel::onLinkPressed)

            Text(
                text = stringResource(CoreR.string.home_follow_title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )
            DevelopersCard(onLinkClicked = viewModel::onLinkPressed)
        }
    }

    InstallDialog(
        show = showInstallDialog,
        onDismiss = { showInstallDialog = false },
        installVm = installVm,
    )
}

@Composable
private fun RebootButton(
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var safeModeEnabled by remember { mutableIntStateOf(Config.bootloop) }

    val showUserspace = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
        context.getSystemService<PowerManager>()?.isRebootingUserspaceSupported == true
    val showSafeMode = Const.Version.atLeast_28_0()

    val items = buildList {
        add(RebootOption(CoreR.string.reboot, Icons.Default.RestartAlt) { reboot() })
        if (showUserspace) {
            add(RebootOption(CoreR.string.reboot_userspace, Icons.Default.Refresh) { reboot("userspace") })
        }
        add(RebootOption(CoreR.string.reboot_recovery, Icons.Default.Build) { reboot("recovery") })
        add(RebootOption(CoreR.string.reboot_bootloader, Icons.Default.Android) { reboot("bootloader") })
        add(RebootOption(CoreR.string.reboot_download, Icons.Default.Download) { reboot("download") })
        add(RebootOption(CoreR.string.reboot_edl, Icons.Default.Memory) { reboot("edl") })
        if (showSafeMode) {
            add(RebootOption(CoreR.string.reboot_safe_mode, Icons.Default.Security) {
                val newVal = if (safeModeEnabled >= 2) 0 else 2
                Config.bootloop = newVal
                safeModeEnabled = newVal
            })
        }
    }

    Box(modifier = modifier) {
        IconButton(
            onClick = { showMenu = true },
        ) {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = stringResource(CoreR.string.reboot),
            )
        }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            offset = DpOffset(x = (-8).dp, y = 0.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            items.forEach { item ->
                val isSafeMode = item.labelRes == CoreR.string.reboot_safe_mode
                if (isSafeMode) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(item.labelRes),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = if (isSafeMode && safeModeEnabled >= 2) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else null,
                    onClick = {
                        item.action()
                        if (!isSafeMode) showMenu = false
                    }
                )
            }
        }
    }
}

private class RebootOption(val labelRes: Int, val icon: ImageVector, val action: () -> Unit)

@Composable
private fun NoticeCard(
    onHide: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 6.dp, bottom = 6.dp, end = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(CoreR.string.home_notice_content),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 6.dp)
            )
            IconButton(onClick = onHide) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(CoreR.string.hide),
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }
    }
}

@Composable
private fun InstallButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
) {
    val buttonContent = @Composable {
        Icon(
            painter = painterResource(R.drawable.ic_download),
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize),
        )
        Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
        )
    }

    if (isPrimary) {
        Button(
            onClick = onClick,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            modifier = modifier,
        ) {
            buttonContent()
        }
    } else {
        FilledTonalButton(
            onClick = onClick,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            modifier = modifier,
        ) {
            buttonContent()
        }
    }
}

@Composable
private fun CoreCard(
    state: HomeViewModel.State,
    version: String,
    onInstallClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isInstalled = state != HomeViewModel.State.INVALID
    val actionLabel = when (state) {
        HomeViewModel.State.OUTDATED -> stringResource(CoreR.string.update)
        HomeViewModel.State.INVALID -> stringResource(CoreR.string.install)
        HomeViewModel.State.UP_TO_DATE -> stringResource(CoreR.string.reinstall)
        HomeViewModel.State.LOADING -> null
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Magisk",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer( 
