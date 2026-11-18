package com.vsecurity.scanner.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vsecurity.scanner.data.model.RiskLevel
import com.vsecurity.scanner.data.model.ScannedApp
import com.vsecurity.scanner.ui.theme.*
import com.vsecurity.scanner.ui.viewmodel.ScannerViewModel
import com.vsecurity.scanner.ui.viewmodel.ScanState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    navController: NavController,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scanState by viewModel.scanState.collectAsState()
    var selectedApp by remember { mutableStateOf<ScannedApp?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Header
        Text(
            text = "App Scanner",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Identify vulnerabilities in your installed apps",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Scan Controls with Animation
        AnimatedContent(
            targetState = scanState,
            transitionSpec = {
                fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
            },
            label = "scan_controls",
            contentKey = { it::class.java.name }
        ) { state ->
            when (state) {
                is ScanState.Idle -> {
                    ScanHeroButton(
                        onScan = { viewModel.startScan() },
                        lastScanTime = uiState.lastScanTime
                    )
                }
                is ScanState.Scanning -> {
                    ScanProgressCard(
                        current = state.current,
                        total = state.total,
                        currentApp = state.currentApp
                    )
                }
                is ScanState.Completed -> {
                    ScanCompletedHero(
                        totalApps = uiState.scannedApps.size,
                        highRiskCount = uiState.scannedApps.count { 
                            it.riskLevel in listOf(RiskLevel.CRITICAL, RiskLevel.HIGH) 
                        },
                        onRescan = { viewModel.startScan() }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Filter Chips - Modern Design
        Text(
            text = "Filter by Risk",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        val filters = remember {
            listOf(
                null to "All",
                RiskLevel.CRITICAL to "Critical",
                RiskLevel.HIGH to "High",
                RiskLevel.MEDIUM to "Medium",
                RiskLevel.LOW to "Low"
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filters) { pair ->
                val level = pair.first
                val label = pair.second
                val isSelected = uiState.selectedFilter == level
                val chipColor = when(level) {
                    RiskLevel.CRITICAL -> CyberRed
                    RiskLevel.HIGH -> CyberOrange
                    RiskLevel.LOW -> CyberGreen
                    else -> CyberBlue
                }

                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setFilter(level) },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = chipColor.copy(alpha = 0.2f),
                        selectedLabelColor = chipColor,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) chipColor else Color.Transparent,
                        borderWidth = 1.dp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = Modifier.weight(1f)
        ) {
            val filteredApps = if (uiState.selectedFilter != null) {
                uiState.scannedApps.filter { it.riskLevel == uiState.selectedFilter }
            } else {
                uiState.scannedApps
            }

            items(filteredApps, key = { it.packageName }) { app ->
                ScannedAppCard(
                    app = app,
                    onClick = { selectedApp = app }
                )
            }

            if (filteredApps.isEmpty() && scanState !is ScanState.Scanning) {
                item {
                    EmptyScannerState(
                        message = if (uiState.scannedApps.isEmpty()) {
                            "No apps scanned yet. Tap 'Scan Now' to begin."
                        } else {
                            "No apps match the selected filter."
                        }
                    )
                }
            }
        }
    }

    // App Detail Bottom Sheet
    selectedApp?.let { app ->
        AppDetailBottomSheet(
            app = app,
            onDismiss = { selectedApp = null }
        )
    }
}

@Composable
fun ScanHeroButton(
    onScan: () -> Unit,
    lastScanTime: Long?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(CyberBlue.copy(alpha = 0.1f), Color.Transparent)
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(CyberBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Security,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = CyberBlue
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onScan,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Security Scan", fontWeight = FontWeight.Bold)
            }
            if (lastScanTime != null && lastScanTime > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Last scan: ${formatTimestamp(lastScanTime)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ScanProgressCard(
    current: Int,
    total: Int,
    currentApp: String
) {
    val progress by animateFloatAsState(
        targetValue = current.toFloat() / total.coerceAtLeast(1),
        animationSpec = tween(500),
        label = "scan_progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Scanning Apps...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$current of $total analyzed",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = CyberBlue
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape),
                color = CyberBlue,
                trackColor = CyberBlue.copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = currentApp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ScanCompletedHero(
    totalApps: Int,
    highRiskCount: Int,
    onRescan: () -> Unit
) {
    val statusColor = if (highRiskCount > 0) CyberOrange else CyberGreen
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(statusColor.copy(alpha = 0.1f), Color.Transparent)))
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Scan Complete",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (highRiskCount > 0) 
                        "$highRiskCount threats require your attention" 
                    else 
                        "No threats found in $totalApps apps",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onRescan,
                modifier = Modifier.background(statusColor.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Rescan", tint = statusColor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannedAppCard(
    app: ScannedApp,
    onClick: () -> Unit
) {
    val riskColor = when (app.riskLevel) {
        RiskLevel.CRITICAL -> CyberRed
        RiskLevel.HIGH -> CyberOrange
        RiskLevel.MEDIUM -> Color(0xFFFFEB3B)
        RiskLevel.LOW -> CyberGreen
        else -> CyberBlue
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Risk indicator
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(riskColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = app.riskScore.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = riskColor
                    )
                    Text(
                        text = "SCORE",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        color = riskColor.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = app.appName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    if (app.isFromPlayStore) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            modifier = Modifier.size(14.dp),
                            tint = CyberBlue
                        )
                    }
                }
                Text(
                    text = app.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ) {
                        Text(
                            text = "${app.permissions.size} Perms",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    if (app.dangerousPermissions.isNotEmpty()) {
                        Surface(
                            color = riskColor.copy(alpha = 0.1f),
                            shape = CircleShape
                        ) {
                            Text(
                                text = "${app.dangerousPermissions.size} Risky",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = riskColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailBottomSheet(
    app: ScannedApp,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)) }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Apps, null, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = app.appName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = app.packageName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Risk Score Large
            item {
                RiskHeroSection(app.riskScore, app.riskLevel)
            }

            // App Metadata Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoCard(Modifier.weight(1f), "Version", app.versionName, Icons.Outlined.Info)
                        InfoCard(Modifier.weight(1f), "Source", app.installerSource, Icons.Outlined.Download)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoCard(Modifier.weight(1f), "Permissions", app.permissions.size.toString(), Icons.Outlined.List)
                        InfoCard(Modifier.weight(1f), "System App", if (app.isSystemApp) "Yes" else "No", Icons.Outlined.Settings)
                    }
                }
            }

            // Dangerous Permissions
            if (app.dangerousPermissions.isNotEmpty()) {
                item {
                    Text(
                        text = "Security Analysis",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(app.dangerousPermissions) { perm ->
                    SecurityIssueItem(
                        title = perm.name,
                        description = perm.description,
                        riskLevel = perm.riskLevel,
                        recommendation = perm.mitigations.firstOrNull()
                    )
                }
            }
        }
    }
}

@Composable
fun RiskHeroSection(score: Int, level: RiskLevel) {
    val color = when (level) {
        RiskLevel.CRITICAL -> CyberRed
        RiskLevel.HIGH -> CyberOrange
        RiskLevel.MEDIUM -> Color(0xFFFFEB3B)
        RiskLevel.LOW -> CyberGreen
        else -> CyberBlue
    }

    Surface(
        color = color.copy(alpha = 0.05f),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "${level.name} RISK",
                    style = MaterialTheme.typography.labelLarge,
                    color = color,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Security Score: $score/100",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            CircularProgressIndicator(
                progress = score / 100f,
                modifier = Modifier.size(56.dp),
                color = color,
                strokeWidth = 6.dp,
                trackColor = color.copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun InfoCard(modifier: Modifier, label: String, value: String, icon: ImageVector) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

@Composable
fun SecurityIssueItem(
    title: String,
    description: String,
    riskLevel: RiskLevel,
    recommendation: String?
) {
    val color = when (riskLevel) {
        RiskLevel.CRITICAL -> CyberRed
        RiskLevel.HIGH -> CyberOrange
        else -> CyberBlue
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 20.dp)
        )
        if (recommendation != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier.padding(start = 20.dp),
                color = CyberBlue.copy(alpha = 0.05f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Fix: $recommendation",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = CyberBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun EmptyScannerState(message: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.SearchOff,
            null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}
