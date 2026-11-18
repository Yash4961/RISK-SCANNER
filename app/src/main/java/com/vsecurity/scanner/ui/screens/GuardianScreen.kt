package com.vsecurity.scanner.ui.screens

import android.app.Activity
import android.content.Context
import android.net.VpnService
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhonelinkOff
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.vsecurity.scanner.ui.theme.CyberBlue
import com.vsecurity.scanner.ui.theme.CyberGreen
import com.vsecurity.scanner.ui.theme.CyberOrange
import com.vsecurity.scanner.ui.theme.CyberPurple
import com.vsecurity.scanner.ui.theme.CyberRed
import com.vsecurity.scanner.ui.viewmodel.GuardianViewModel
import java.util.Locale
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianScreen(
    navController: NavController,
    viewModel: GuardianViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isVpnRunning by viewModel.isVpnRunning.collectAsState()
    val totalUsage by viewModel.totalUsageOverTime.collectAsState()

    val context = LocalContext.current
    val vpnLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.startNetworkMonitor()
        }
    }

    // Permission tracking state
    val hasUsageStatsPermission = remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
                val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    appOps.unsafeCheckOpNoThrow(android.app.AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName)
                } else {
                    appOps.checkOpNoThrow(android.app.AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName)
                }
                hasUsageStatsPermission.value = (mode == android.app.AppOpsManager.MODE_ALLOWED)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
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
                    text = "Privacy Guardian",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Real-time protection for your device sensors",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Guardian Status Hero
        item {
            GuardianStatusHero(
                isActive = uiState.isGuardianActive,
                onToggle = { viewModel.toggleGuardian() }
            )
        }

        // Network VPN Monitor Hero
        item {
            NetworkMonitorHero(
                isActive = isVpnRunning,
                onToggle = {
                    if (!isVpnRunning) {
                        if (!hasUsageStatsPermission.value) {
                            // Open settings to request PACKAGE_USAGE_STATS permission
                            val intent = android.content.Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS)
                            context.startActivity(intent)
                        } else {
                            val intent = VpnService.prepare(context)
                            if (intent != null) {
                                vpnLauncher.launch(intent)
                            } else {
                                viewModel.startNetworkMonitor()
                            }
                        }
                    } else {
                        viewModel.stopNetworkMonitor()
                    }
                }
            )
        }

        if (isVpnRunning) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle("Real-Time Network Traffic")
                    TimeframeSelector(
                        currentTimeframe = uiState.networkGraphTimeframe,
                        onTimeframeSelected = { viewModel.setNetworkGraphTimeframe(it) }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Live Bandwidth Usage", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        NetworkUsageGraph(
                            usageData = totalUsage,
                            timeframe = uiState.networkGraphTimeframe
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("app_network_usage") },
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(CyberBlue.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Outlined.Public, contentDescription = null, tint = CyberBlue)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("App Network Usage", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text("View detailed data usage per app", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = "View Details", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Monitoring Sections
        item {
            SectionTitle("Sensor Monitoring")
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ModernToggleItem(
                        icon = Icons.Outlined.CameraAlt,
                        label = "Camera Access",
                        description = "Monitor when apps use your camera",
                        checked = uiState.monitorCamera,
                        activeColor = CyberBlue,
                        onCheckedChange = { viewModel.setMonitorCamera(it) }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    ModernToggleItem(
                        icon = Icons.Outlined.Mic,
                        label = "Microphone Usage",
                        description = "Alert when apps record audio",
                        checked = uiState.monitorMicrophone,
                        activeColor = CyberPurple,
                        onCheckedChange = { viewModel.setMonitorMicrophone(it) }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    ModernToggleItem(
                        icon = Icons.Outlined.LocationOn,
                        label = "Location Tracking",
                        description = "Monitor background location access",
                        checked = uiState.monitorLocation,
                        activeColor = CyberGreen,
                        onCheckedChange = { viewModel.setMonitorLocation(it) }
                    )
                }
            }
        }

        item {
            SectionTitle("Security Policies")
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ModernToggleItem(
                        icon = Icons.Outlined.VisibilityOff,
                        label = "Background Shield",
                        description = "Alert when background apps use sensors",
                        checked = uiState.alertOnBackground,
                        activeColor = CyberOrange,
                        onCheckedChange = { viewModel.setAlertOnBackground(it) }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    ModernToggleItem(
                        icon = Icons.Outlined.PhonelinkOff,
                        label = "Screen-Off Protection",
                        description = "Block sensor usage when screen is locked",
                        checked = uiState.alertOnScreenOff,
                        activeColor = CyberRed,
                        onCheckedChange = { viewModel.setAlertOnScreenOff(it) }
                    )
                }
            }
        }

        // Analytics Card
        item {
            SectionTitle("Activity Analytics")
            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ModernTrendsCard(dailySummaries = uiState.dailySummaries)

                if (uiState.dailySummaries.isNotEmpty()) {
                    SecurityInsightCard(dailySummaries = uiState.dailySummaries)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val totalBackground = uiState.dailySummaries.sumOf { it.totalBackgroundAccesses }
                        val totalAlerts = uiState.dailySummaries.sumOf { it.alertsTriggered }

                        AnalyticsStatCard(
                            modifier = Modifier.weight(1f),
                            label = "Background",
                            value = totalBackground.toString(),
                            icon = Icons.Outlined.VisibilityOff,
                            color = CyberOrange
                        )
                        AnalyticsStatCard(
                            modifier = Modifier.weight(1f),
                            label = "Total Alerts",
                            value = totalAlerts.toString(),
                            icon = Icons.Default.Shield,
                            color = CyberRed
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun TimeframeSelector(
    currentTimeframe: String,
    onTimeframeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val timeframes = listOf("60s", "1h", "24h", "7d")

    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentTimeframe,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            timeframes.forEach { timeframe ->
                DropdownMenuItem(
                    text = { Text(timeframe) },
                    onClick = {
                        onTimeframeSelected(timeframe)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun NetworkUsageGraph(usageData: List<Long>, timeframe: String) {
    val axisColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val density = androidx.compose.ui.platform.LocalDensity.current

    // Convert labelWidth to pixels based on density so it's consistent
    val labelWidthPx = with(density) { 60.dp.toPx() }

    val numSeconds = when (timeframe) {
        "60s" -> 60
        "1h" -> 3600
        "24h" -> 86400
        "7d" -> 604800
        else -> 60
    }

    val aggregatedData = remember(usageData, timeframe) {
        usageData.takeLast(numSeconds)
    }

    val maxUsage = remember(aggregatedData) {
        (aggregatedData.maxOrNull() ?: 0L).coerceAtLeast(1024L).let { (it * 1.15f).toLong() }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (aggregatedData.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Collecting telemetry...", style = MaterialTheme.typography.bodySmall)
            }
        } else {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp, bottom = 30.dp, start = 10.dp, end = 20.dp)
            ) {
                val width = size.width
                val height = size.height
                val graphWidth = width - labelWidthPx
                val graphHeight = height - 20f

                // --- AXES ---
                drawLine(color = axisColor, start = Offset(labelWidthPx, 0f), end = Offset(labelWidthPx, graphHeight), strokeWidth = 2f)
                drawLine(color = axisColor, start = Offset(labelWidthPx, graphHeight), end = Offset(width, graphHeight), strokeWidth = 2f)

                val paint = android.graphics.Paint().apply {
                    color = labelColor
                    textSize = 24f
                    isAntiAlias = true
                }

                // --- Y-LABELS ---
                paint.textAlign = android.graphics.Paint.Align.RIGHT
                listOf(0L, maxUsage / 2, maxUsage).forEach { valL ->
                    val y = graphHeight - (valL.toFloat() / maxUsage.toFloat() * graphHeight)
                    drawContext.canvas.nativeCanvas.drawText(formatBytes(valL), labelWidthPx - 10f, y + 8f, paint)
                }

                // --- X-LABELS ---
                val xLabels = when(timeframe) {
                    "60s" -> listOf("-60s", "-30s", "Now")
                    "1h" -> listOf("-60m", "-30m", "Now")
                    "24h" -> listOf("-24h", "-12h", "Now")
                    "7d" -> listOf("-7d", "-4d", "Now")
                    else -> listOf("-60s", "-30s", "Now")
                }
                paint.textAlign = android.graphics.Paint.Align.CENTER
                xLabels.forEachIndexed { index, label ->
                    val x = labelWidthPx + index * (graphWidth / 2f)
                    drawContext.canvas.nativeCanvas.drawText(label, x, height + 15f, paint)
                }

                val spaceBetweenPoints = graphWidth / (numSeconds - 1)

                if (aggregatedData.isNotEmpty()) {
                    val path = Path()
                    aggregatedData.forEachIndexed { index, value ->
                        val reverseIndex = (aggregatedData.size - 1) - index
                        val x = width - (reverseIndex * spaceBetweenPoints)
                        val y = graphHeight - (value.toFloat() / maxUsage.toFloat() * graphHeight)

                        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }

                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(listOf(CyberBlue, CyberPurple)),
                        style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    val fillPath = Path().apply {
                        addPath(path)
                        lineTo(width, graphHeight)
                        lineTo(labelWidthPx, graphHeight)
                        close()
                    }
                    drawPath(fillPath, Brush.verticalGradient(listOf(CyberBlue.copy(alpha = 0.2f), Color.Transparent)))
                }
            }
        }
    }
}

@Composable
fun AppNetworkCard(appName: String, packageName: String, bytesRx: Long, bytesTx: Long, isSuspicious: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isSuspicious) CyberRed.copy(alpha=0.1f) else CyberBlue.copy(alpha=0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Public, null, tint = if (isSuspicious) CyberRed else CyberBlue)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(appName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text("↓ ${formatBytes(bytesRx)}  ↑ ${formatBytes(bytesTx)}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (isSuspicious) {
                Surface(color = CyberRed.copy(alpha = 0.1f), shape = CircleShape) {
                    Text("SUSPICIOUS", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = CyberRed, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

fun formatBytes(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val kb = bytes / 1024.0
    if (kb < 1024) return String.format(Locale.getDefault(), "%.1f KB", kb)
    val mb = kb / 1024.0
    return String.format(Locale.getDefault(), "%.1f MB", mb)
}

@Composable
fun NetworkMonitorHero(
    isActive: Boolean,
    onToggle: () -> Unit
) {
    val statusColor = if (isActive) CyberBlue else MaterialTheme.colorScheme.onSurfaceVariant
    val brush = if (isActive) {
        Brush.horizontalGradient(listOf(CyberBlue.copy(alpha = 0.15f), Color.Transparent))
    } else {
        Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, Color.Transparent))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush)
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Security,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = if (isActive) "Network Shield Active" else "Network Shield Paused",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isActive) CyberBlue else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isActive) "Monitoring live internet traffic" else "Tap to enable VPN Shield",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Switch(
                checked = isActive,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = CyberBlue,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.scale(0.8f)
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun GuardianStatusHero(
    isActive: Boolean,
    onToggle: () -> Unit
) {
    val statusColor = if (isActive) CyberGreen else MaterialTheme.colorScheme.onSurfaceVariant
    val brush = if (isActive) {
        Brush.horizontalGradient(listOf(CyberGreen.copy(alpha = 0.15f), Color.Transparent))
    } else {
        Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, Color.Transparent))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush)
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isActive) Icons.Default.Shield else Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = if (isActive) "Active Protection" else "Protection Paused",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isActive) CyberGreen else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isActive) "Shielding your privacy" else "Tap to enable Guardian",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Switch(
                checked = isActive,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = CyberGreen,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.scale(0.8f)
            )
        }
    }
}

@Composable
fun ModernToggleItem(
    icon: ImageVector,
    label: String,
    description: String,
    checked: Boolean,
    activeColor: Color,
    onCheckedChange: (Boolean) -> Unit
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
                .clip(MaterialTheme.shapes.small)
                .background(if (checked) activeColor.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (checked) activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.scale(0.8f)
        )
    }
}

@Composable
fun ModernActivityCard(
    appName: String,
    packageName: String,
    cameraCount: Int,
    micCount: Int,
    locationCount: Int,
    isSuspicious: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(appName.take(1), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(appName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(packageName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                    }
                }
                if (isSuspicious) {
                    Surface(color = CyberRed.copy(alpha = 0.1f), shape = CircleShape) {
                        Text("SUSPICIOUS", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = CyberRed, fontWeight = FontWeight.Black)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActivityStat(Icons.Outlined.CameraAlt, "Camera", cameraCount, CyberBlue)
                ActivityStat(Icons.Outlined.Mic, "Mic", micCount, CyberPurple)
                ActivityStat(Icons.Outlined.LocationOn, "Location", locationCount, CyberGreen)
            }
        }
    }
}

@Composable
fun ActivityStat(icon: ImageVector, label: String, count: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(14.dp), tint = if (count > 0) color else MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$label: $count",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (count > 0) FontWeight.Bold else FontWeight.Normal,
            color = if (count > 0) color else MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun ModernTrendsCard(dailySummaries: List<com.vsecurity.scanner.data.model.DailySensorSummary>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        if (dailySummaries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.Shield,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Monitoring for threats...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Privacy Trends",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    val totalAccesses = dailySummaries.takeLast(7).sumOf { it.totalCameraAccesses + it.totalMicrophoneAccesses + it.totalLocationAccesses }
                    Text(
                        "$totalAccesses accesses",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val recentData = dailySummaries.takeLast(7)
                    val maxVal = recentData.maxOf { it.totalCameraAccesses + it.totalMicrophoneAccesses + it.totalLocationAccesses }.coerceAtLeast(1)

                    recentData.forEach { summary ->
                        val total = (summary.totalCameraAccesses + summary.totalMicrophoneAccesses + summary.totalLocationAccesses)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            if (total > 0) {
                                Column(
                                    modifier = Modifier
                                        .width(12.dp)
                                        .weight(total.toFloat() / maxVal, fill = false)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                ) {
                                    if (summary.totalCameraAccesses > 0)
                                        Box(modifier = Modifier.fillMaxWidth().weight(summary.totalCameraAccesses.toFloat()).background(CyberBlue))
                                    if (summary.totalMicrophoneAccesses > 0)
                                        Box(modifier = Modifier.fillMaxWidth().weight(summary.totalMicrophoneAccesses.toFloat()).background(CyberPurple))
                                    if (summary.totalLocationAccesses > 0)
                                        Box(modifier = Modifier.fillMaxWidth().weight(summary.totalLocationAccesses.toFloat()).background(CyberGreen))
                                }
                            } else {
                                Box(modifier = Modifier.size(12.dp, 4.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape))
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                summary.date.split("-").lastOrNull() ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TrendLegend(CyberBlue, "Camera")
                    TrendLegend(CyberPurple, "Mic")
                    TrendLegend(CyberGreen, "Location")
                }
            }
        }
    }
}

@Composable
fun TrendLegend(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun AnalyticsStatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SecurityInsightCard(dailySummaries: List<com.vsecurity.scanner.data.model.DailySensorSummary>) {
    val totalAlerts = dailySummaries.sumOf { it.alertsTriggered }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (totalAlerts > 0) CyberRed.copy(alpha = 0.05f) else CyberGreen.copy(alpha = 0.05f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (totalAlerts > 0) CyberRed.copy(alpha = 0.2f) else CyberGreen.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (totalAlerts > 0) Icons.Default.Shield else Icons.Default.Shield,
                contentDescription = null,
                tint = if (totalAlerts > 0) CyberRed else CyberGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = if (totalAlerts > 0) "Security Risks Detected" else "Privacy is Protected",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (totalAlerts > 0) CyberRed else CyberGreen
                )
                Text(
                    text = if (totalAlerts > 0)
                        "$totalAlerts alerts triggered in the last 7 days. Review recent activity."
                    else "No suspicious sensor activity detected in the past week.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
