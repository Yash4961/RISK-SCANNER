package com.vsecurity.scanner.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vsecurity.scanner.ui.screens.*

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Dashboard : Screen("dashboard", "Home", Icons.Outlined.Home, Icons.Default.Home)
    object Scanner : Screen("scanner", "Apps", Icons.Outlined.Security, Icons.Default.Security)
    object FileScanner : Screen("file_scanner", "File Scanner", Icons.Outlined.InsertDriveFile, Icons.Default.InsertDriveFile)
    object Guardian : Screen("guardian", "Guardian", Icons.Outlined.Shield, Icons.Default.Shield)
    object Alerts : Screen("alerts", "Alerts", Icons.Outlined.Notifications, Icons.Default.Notifications)
    object Settings : Screen("settings", "Settings", Icons.Outlined.Settings, Icons.Default.Settings)
    object AppNetworkUsage : Screen("app_network_usage", "App Network Usage", Icons.Outlined.Public, Icons.Default.Public)
}

val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Scanner,
    Screen.Guardian,
    Screen.Alerts,
    Screen.FileScanner
)

@Composable
fun VScannerNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Dashboard.route
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show bottom bar for main destination screens
    val isMainDestination = bottomNavItems.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            if (isMainDestination) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                        val contentColor = if (selected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) screen.selectedIcon else screen.icon,
                                    contentDescription = screen.title,
                                    tint = contentColor
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    color = contentColor
                                )
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController)
            }
            composable(Screen.Scanner.route) {
                ScannerScreen(navController)
            }
            composable(Screen.FileScanner.route) {
                FileScannerScreen(navController)
            }
            composable(Screen.Guardian.route) {
                GuardianScreen(navController)
            }
            composable(Screen.Alerts.route) {
                AlertsScreen(navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController)
            }
            composable(Screen.AppNetworkUsage.route) {
                AppNetworkUsageScreen(navController)
            }
        }
    }
}
