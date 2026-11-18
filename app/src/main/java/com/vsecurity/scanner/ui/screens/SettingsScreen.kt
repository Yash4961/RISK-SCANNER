package com.vsecurity.scanner.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.vsecurity.scanner.ui.theme.*
import com.vsecurity.scanner.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Dialog States
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(vertical = 24.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Customize your security experience",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Permissions Section
        item {
            ModernSettingsSection(title = "Core Permissions") {
                ModernPermissionItem(
                    title = "Usage Access",
                    description = "Analyze app behavior and sensor usage",
                    isGranted = uiState.hasUsageStatsPermission,
                    icon = Icons.Outlined.Analytics,
                    onRequest = {
                        context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                ModernPermissionItem(
                    title = "Notifications",
                    description = "Instant alerts for privacy threats",
                    isGranted = uiState.hasNotificationPermission,
                    icon = Icons.Outlined.NotificationsActive,
                    onRequest = {
                        context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        })
                    }
                )
            }
        }

        // Feature Controls
        item {
            ModernSettingsSection(title = "App Scanner") {
                ModernSettingsToggle(
                    icon = Icons.Outlined.Android,
                    title = "System App Analysis",
                    description = "Include pre-installed system applications",
                    checked = uiState.includeSystemApps,
                    onCheckedChange = { viewModel.setIncludeSystemApps(it) }
                )
            }
        }

        item {
            ModernSettingsSection(title = "Privacy Guardian") {
                ModernSettingsToggle(
                    icon = Icons.Outlined.Shield,
                    title = "Real-time Protection",
                    description = "Continuous monitoring of sensor access",
                    checked = uiState.guardianEnabled,
                    onCheckedChange = { viewModel.setGuardianEnabled(it) }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                ModernSettingsToggle(
                    icon = Icons.Outlined.PowerSettingsNew,
                    title = "Start on Boot",
                    description = "Launch guardian when device starts",
                    checked = uiState.startOnBoot,
                    onCheckedChange = { viewModel.setStartOnBoot(it) }
                )
            }
        }

        // Alert Threshold
        item {
            ModernSettingsSection(title = "Sensitivity") {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Frequency Threshold",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = CyberBlue.copy(alpha = 0.1f),
                            shape = CircleShape
                        ) {
                            Text(
                                "${uiState.frequentThreshold}/hr",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = CyberBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Slider(
                        value = uiState.frequentThreshold.toFloat(),
                        onValueChange = { viewModel.setFrequentThreshold(it.toInt()) },
                        valueRange = 5f..50f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = CyberBlue,
                            activeTrackColor = CyberBlue,
                            inactiveTrackColor = CyberBlue.copy(alpha = 0.1f)
                        )
                    )
                    Text(
                        text = "Threshold for 'Frequent Access' alerts. Lower is more sensitive.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Data Cleanup
        item {
            ModernSettingsSection(title = "Data & Privacy") {
                ModernSettingsAction(
                    icon = Icons.Outlined.DeleteSweep,
                    title = "Reset Security Data",
                    description = "Clear all scan history and sensor logs",
                    color = CyberRed,
                    onClick = { viewModel.clearScanHistory(); viewModel.clearSensorLogs() }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                ModernSettingsAction(
                    icon = Icons.Outlined.NotificationsOff,
                    title = "Clear Alert History",
                    description = "Remove all past privacy alerts",
                    onClick = { viewModel.clearAllAlerts() }
                )
            }
        }

        // Legal & Compliance
        item {
            ModernSettingsSection(title = "Legal & Compliance") {
                ModernSettingsAction(
                    icon = Icons.Outlined.Gavel,
                    title = "Terms of Service",
                    description = "User agreement and security usage rules",
                    onClick = { showTermsDialog = true }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                ModernSettingsAction(
                    icon = Icons.Outlined.PrivacyTip,
                    title = "Privacy Policy",
                    description = "Local data handling and sensor monitoring policy",
                    onClick = { showPrivacyDialog = true }
                )
            }
        }

        // About
        item {
            ModernSettingsSection(title = "Information") {
                ModernSettingsInfo(
                    icon = Icons.Outlined.Info,
                    title = "App Version",
                    value = "1.2.0-PRO"
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                ModernSettingsInfo(
                    icon = Icons.Outlined.VerifiedUser,
                    title = "Security Engine",
                    value = "v4.5.2"
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }

    // Dialogs
    if (showTermsDialog) {
        SettingsDetailDialog(
            title = "Terms of Service",
            content = "By using Security Guardian, you agree that this application is a monitoring assistant. While we use AppOpsManager and UsageStats to identify potential threats, we are not liable for zero-day exploits or hardware-level intrusions that bypass Android's standard API monitoring. All analysis is performed locally.",
            onDismiss = { showTermsDialog = false }
        )
    }

    if (showPrivacyDialog) {
        SettingsDetailDialog(
            title = "Privacy Policy",
            content = "Your privacy is our core mission. This app operates entirely offline and does not require Internet permissions. All sensor access logs, app risk scores, and daily summaries are stored in a local Room database and never leave your device. Clearing data in Settings permanently deletes these logs.",
            onDismiss = { showPrivacyDialog = false }
        )
    }
}

@Composable
fun SettingsDetailDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Understood", color = CyberBlue, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun ModernSettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun ModernPermissionItem(
    title: String,
    description: String,
    isGranted: Boolean,
    icon: ImageVector,
    onRequest: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isGranted) CyberGreen.copy(alpha = 0.1f) else CyberOrange.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon, null,
                modifier = Modifier.size(20.dp),
                tint = if (isGranted) CyberGreen else CyberOrange
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(description, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (!isGranted) {
            TextButton(onClick = onRequest) {
                Text("Grant", fontWeight = FontWeight.Bold)
            }
        } else {
            Icon(Icons.Default.Check, null, tint = CyberGreen, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun ModernSettingsToggle(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(description, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CyberBlue
            )
        )
    }
}

@Composable
fun ModernSettingsAction(
    icon: ImageVector,
    title: String,
    description: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = color.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = color)
            Text(description, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
fun ModernSettingsInfo(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
    }
}