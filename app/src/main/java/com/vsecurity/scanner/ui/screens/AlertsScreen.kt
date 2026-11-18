package com.vsecurity.scanner.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import com.vsecurity.scanner.data.model.AlertType
import com.vsecurity.scanner.data.model.PrivacyAlert
import com.vsecurity.scanner.data.model.SensorType
import com.vsecurity.scanner.ui.theme.*
import com.vsecurity.scanner.ui.viewmodel.AlertsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    navController: NavController,
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Privacy Alerts",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${uiState.unacknowledgedCount} unread security events",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (uiState.alerts.isNotEmpty()) {
                IconButton(
                    onClick = { viewModel.acknowledgeAll() },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.DoneAll, contentDescription = "Clear All", tint = CyberBlue)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Filter Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val filters = listOf(
                null to "All",
                SensorType.CAMERA to "Camera",
                SensorType.MICROPHONE to "Microphone",
                SensorType.LOCATION to "Location"
            )

            items(filters) { pair ->
                val type = pair.first
                val label = pair.second
                val isSelected = uiState.selectedFilter == type
                val icon = when(type) {
                    SensorType.CAMERA -> Icons.Outlined.CameraAlt
                    SensorType.MICROPHONE -> Icons.Outlined.Mic
                    SensorType.LOCATION -> Icons.Outlined.LocationOn
                    else -> Icons.Outlined.FilterList
                }

                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setFilter(type) },
                    label = { Text(label) },
                    leadingIcon = { Icon(icon, null, modifier = Modifier.size(18.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberBlue.copy(alpha = 0.15f),
                        selectedLabelColor = CyberBlue,
                        selectedLeadingIconColor = CyberBlue,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) CyberBlue else Color.Transparent,
                        borderWidth = 1.dp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Alerts List
        val filteredAlerts = if (uiState.selectedFilter != null) {
            uiState.alerts.filter { it.sensorType == uiState.selectedFilter }
        } else {
            uiState.alerts
        }

        AnimatedContent(
            targetState = filteredAlerts.isEmpty(),
            transitionSpec = {
                fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
            },
            label = "alerts_content"
        ) { isEmpty ->
            if (isEmpty) {
                EmptyAlertsState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredAlerts, key = { it.id }) { alert ->
                        AlertItemCard(
                            alert = alert,
                            onAcknowledge = { viewModel.acknowledgeAlert(alert.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlertItemCard(
    alert: PrivacyAlert,
    onAcknowledge: () -> Unit
) {
    val (color, icon) = when (alert.sensorType) {
        SensorType.CAMERA -> CyberBlue to Icons.Outlined.CameraAlt
        SensorType.MICROPHONE -> CyberPurple to Icons.Outlined.Mic
        SensorType.LOCATION -> CyberGreen to Icons.Outlined.LocationOn
        else -> CyberOrange to Icons.Outlined.Warning
    }

    val alertTypeLabel = alert.alertType.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (alert.wasAcknowledged) 0.5.dp else 2.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (!alert.wasAcknowledged) 
                        Brush.horizontalGradient(listOf(color.copy(alpha = 0.08f), Color.Transparent))
                    else 
                        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, null, modifier = Modifier.size(18.dp), tint = color)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = alert.appName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formatAlertTime(alert.timestamp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (!alert.wasAcknowledged) {
                        Surface(
                            color = CyberRed,
                            shape = CircleShape,
                            modifier = Modifier.size(8.dp)
                        ) {}
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = alert.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = alertTypeLabel,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (!alert.wasAcknowledged) {
                        TextButton(
                            onClick = onAcknowledge,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Dismiss", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyAlertsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(CyberGreen.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Shield,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = CyberGreen.copy(alpha = 0.5f)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Everything is Secure",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "No suspicious sensor activity has been detected recently.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
    }
}

private fun formatAlertTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000}m ago"
        diff < 86400_000 -> "${diff / 3600_000}h ago"
        else -> {
            val sdf = java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault())
            sdf.format(java.util.Date(timestamp))
        }
    }
}
