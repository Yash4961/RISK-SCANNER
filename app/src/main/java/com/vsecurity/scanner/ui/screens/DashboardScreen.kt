package com.vsecurity.scanner.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vsecurity.scanner.data.model.RiskLevel
import com.vsecurity.scanner.ui.theme.CyberBlue
import com.vsecurity.scanner.ui.theme.CyberGreen
import com.vsecurity.scanner.ui.theme.CyberOrange
import com.vsecurity.scanner.ui.theme.CyberPurple
import com.vsecurity.scanner.ui.theme.CyberRed
import com.vsecurity.scanner.ui.viewmodel.DashboardViewModel
import kotlin.math.cos
import kotlin.math.sin

private fun NavController.navigateToBottomNavDestination(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Constant Background Animation
        AnimatedBackground()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 40.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Security Dashboard",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Your device security overview",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { navController.navigateToBottomNavDestination("settings") }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            // Overall Security Score
            item {
                SecurityScoreCard(
                    score = uiState.overallScore,
                    riskLevel = uiState.overallRiskLevel
                )
            }

            // Quick Stats
            item {
                SectionHeader(title = "Quick Stats")
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    item {
                        StatCard(
                            title = "Apps Scanned",
                            value = uiState.totalAppsScanned.toString(),
                            icon = Icons.Outlined.Apps,
                            color = CyberBlue
                        )
                    }
                    item {
                        StatCard(
                            title = "High Risk",
                            value = uiState.highRiskApps.toString(),
                            icon = Icons.Outlined.WarningAmber,
                            color = CyberRed
                        )
                    }
                    item {
                        StatCard(
                            title = "Alerts Today",
                            value = uiState.alertsToday.toString(),
                            icon = Icons.Outlined.Notifications,
                            color = CyberOrange
                        )
                    }
                    item {
                        StatCard(
                            title = "Guardian",
                            value = if (uiState.guardianActive) "Active" else "Off",
                            icon = Icons.Outlined.Shield,
                            color = if (uiState.guardianActive) CyberGreen else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Recent Alerts Section
            if (uiState.recentAlerts.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Recent Alerts",
                        actionText = "See All",
                        onActionClick = { navController.navigateToBottomNavDestination("alerts") }
                    )
                }

                items(uiState.recentAlerts.take(3)) { alert ->
                    AlertListItem(
                        appName = alert.appName,
                        message = alert.message,
                        time = formatTime(alert.timestamp)
                    )
                }
            }

            // Quick Actions
            item {
                SectionHeader(title = "Quick Actions")
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    QuickActionButton(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Security,
                        label = "Full Scan",
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = { navController.navigateToBottomNavDestination("scanner") }
                    )
                    QuickActionButton(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Shield,
                        label = "Guardian",
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        onClick = { navController.navigateToBottomNavDestination("guardian") }
                    )
                }
            }

            // Sensor Usage
            item {
                SectionHeader(title = "Today's Sensor Usage")
                Spacer(modifier = Modifier.height(8.dp))
                SensorUsageCard(
                    cameraAccesses = uiState.todayCameraAccesses,
                    microphoneAccesses = uiState.todayMicAccesses,
                    locationAccesses = uiState.todayLocationAccesses,
                    backgroundAccesses = uiState.todayBackgroundAccesses
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun AnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    // Create a single pulse value to sync all particles slightly
    val globalPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "globalPulse"
    )

    // Slower, multi-directional movement
    val movementShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "movement"
    )

    val dotColor = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Define particle positions (Relative to screen size)
        val particles = listOf(
            Triple(0.2f, 0.2f, CyberBlue),
            Triple(0.8f, 0.15f, CyberPurple),
            Triple(0.5f, 0.5f, CyberBlue),
            Triple(0.15f, 0.8f, CyberPurple),
            Triple(0.85f, 0.75f, CyberBlue)
        )

        particles.forEachIndexed { index, (relX, relY, color) ->
            // Use sine/cosine to make them move in small circles
            val offset = Offset(
                x = (relX * width) + (sin(movementShift + index) * 50f),
                y = (relY * height) + (cos(movementShift + index) * 50f)
            )

            val radius = 150f + (index * 20f) // Smaller, varied sizes

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.15f * globalPulse),
                        Color.Transparent
                    ),
                    center = offset,
                    radius = radius
                ),
                center = offset,
                radius = radius
            )
        }

        // Subtle moving dots grid (scaled down)
        val dotSpacing = 60f
        for (x in 0..(width / dotSpacing).toInt()) {
            for (y in 0..(height / dotSpacing).toInt()) {
                // Creates a "wave" effect through the dots
                val dist = sin((x * 0.3f) + (y * 0.3f) + movementShift)
                val dotAlpha = ((dist + 1f) / 2f) * 0.08f

                drawCircle(
                    color = dotColor.copy(alpha = dotAlpha),
                    radius = 1.2f,
                    center = Offset(x * dotSpacing, y * dotSpacing)
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        if (actionText != null && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Text(actionText, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun SecurityScoreCard(
    score: Int,
    riskLevel: RiskLevel
) {
    val (color, statusText) = when (riskLevel) {
        RiskLevel.CRITICAL -> CyberRed to "System Compromised"
        RiskLevel.HIGH -> CyberOrange to "High Risk Detected"
        RiskLevel.MEDIUM -> Color(0xFFFFEB3B) to "Moderate Risk"
        RiskLevel.LOW -> CyberGreen to "Device Protected"
        RiskLevel.INFO -> CyberBlue to "Scanning..."
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            color.copy(alpha = 0.15f * pulseAlpha),
                            color.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Security Status",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = color
                    )
                }
                
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = score / 100f,
                        modifier = Modifier.size(80.dp),
                        color = color,
                        strokeWidth = 8.dp,
                        trackColor = color.copy(alpha = 0.1f)
                    )
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.width(130.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AlertListItem(
    appName: String,
    message: String,
    time: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                    .background(CyberRed.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = CyberRed,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SensorUsageCard(
    cameraAccesses: Int,
    microphoneAccesses: Int,
    locationAccesses: Int,
    backgroundAccesses: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SensorStat(Icons.Outlined.CameraAlt, "Camera", cameraAccesses, CyberBlue)
            SensorStat(Icons.Outlined.Mic, "Mic", microphoneAccesses, CyberPurple)
            SensorStat(Icons.Outlined.LocationOn, "Location", locationAccesses, CyberGreen)
            SensorStat(Icons.Outlined.VisibilityOff, "BG Ops", backgroundAccesses, CyberOrange)
        }
    }
}

@Composable
fun SensorStat(icon: ImageVector, label: String, count: Int, activeColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (count > 0) activeColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun QuickActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontWeight = FontWeight.SemiBold)
    }
}

private fun formatTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000}m ago"
        diff < 86400_000 -> "${diff / 3600_000}h ago"
        else -> "${diff / 86400_000}d ago"
    }
}
