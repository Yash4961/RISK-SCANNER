package com.vsecurity.scanner.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vsecurity.scanner.ui.MainActivity
import com.vsecurity.scanner.ui.viewmodel.FileScannerUiState
import com.vsecurity.scanner.ui.viewmodel.FileScannerViewModel
import com.vsecurity.scanner.ui.viewmodel.SystemScanState

@Composable
fun FileScannerScreen(
    navController: NavController,
    viewModel: FileScannerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? MainActivity
    
    val uiState by viewModel.uiState.collectAsState()
    val systemScanState by viewModel.systemScanState.collectAsState()

    val periodicScanEnabled by viewModel.periodicScanEnabled.collectAsState(initial = false)
    val periodicScanInterval by viewModel.periodicScanInterval.collectAsState(initial = 7L)
    val maxScanSizeMb by viewModel.maxScanSizeMb.collectAsState()

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }

    // Helper to extract file name from URI
    fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        result = it.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "Unknown File"
    }

    // Process intent for shared files
    fun processIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND) {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Intent.EXTRA_STREAM)
            }
            if (uri != null) {
                selectedFileUri = uri
                selectedFileName = getFileName(uri)
                // Clear the intent action so we don't process it again on recomposition
                intent.action = null
                viewModel.reset()
            }
        }
    }

    // Handle initial intent
    LaunchedEffect(Unit) {
        processIntent(activity?.intent)
    }

    // Handle new intents (if activity was already open and a new file was shared)
    DisposableEffect(activity) {
        val listener = Consumer<Intent> { intent -> processIntent(intent) }
        activity?.addOnNewIntentListener(listener)
        onDispose {
            activity?.removeOnNewIntentListener(listener)
        }
    }

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            selectedFileName = getFileName(uri)
            viewModel.reset()
        }
    }

    // Permission launcher for full system scan
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.performFullSystemScan()
        }
    }

    fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", context.packageName))
                context.startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                context.startActivity(intent)
            }
        } else {
            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "File Scanner",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp, top = 24.dp)
        )
        
        Text(
            text = "Scan files for malware and security threats.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // System Scan Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Full System Scan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Scan all device storage files offline against our malware database.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Max file size selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Max file size:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    var sizeExpanded by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { sizeExpanded = true }) {
                            Text(if (maxScanSizeMb == 0L) "No Limit" else "${maxScanSizeMb}MB")
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = sizeExpanded,
                            onDismissRequest = { sizeExpanded = false }
                        ) {
                            val options = listOf(50L, 100L, 250L, 500L, 1024L, 0L)
                            options.forEach { size ->
                                DropdownMenuItem(
                                    text = { Text(if (size == 0L) "No Limit" else "${size}MB") },
                                    onClick = { 
                                        viewModel.setMaxScanSizeMb(size)
                                        sizeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(visible = systemScanState is SystemScanState.Idle || systemScanState is SystemScanState.Finished) {
                    Button(
                        onClick = {
                            if (hasStoragePermission()) {
                                viewModel.performFullSystemScan()
                            } else {
                                requestStoragePermission()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Storage, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (systemScanState is SystemScanState.Finished) "Run System Scan Again" else "Start System Scan")
                    }
                }

                if (systemScanState is SystemScanState.Scanning) {
                    val state = systemScanState as SystemScanState.Scanning
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (state.isPaused) "Scan Paused" else "Scanning file: ${state.scannedCount}",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = state.currentFile,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Controls: Pause/Play, Stop
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            if (state.isPaused) {
                                IconButton(onClick = { viewModel.resumeSystemScan() }) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Resume", tint = MaterialTheme.colorScheme.primary)
                                }
                            } else {
                                IconButton(onClick = { viewModel.pauseSystemScan() }) {
                                    Icon(Icons.Default.Pause, contentDescription = "Pause", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                            
                            IconButton(onClick = { viewModel.cancelSystemScan() }) {
                                Icon(Icons.Default.Stop, contentDescription = "Stop", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }

                if (systemScanState is SystemScanState.Finished) {
                    val state = systemScanState as SystemScanState.Finished
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (state.threatsFound > 0) MaterialTheme.colorScheme.errorContainer else Color(0xFFE8F5E9)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (state.threatsFound > 0) Icons.Default.Warning else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (state.threatsFound > 0) MaterialTheme.colorScheme.error else Color(0xFF4CAF50)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (state.threatsFound > 0) "Threats Found!" else "System Clean",
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.threatsFound > 0) MaterialTheme.colorScheme.error else Color(0xFF2E7D32)
                                )
                                Text(
                                    text = "${state.threatsFound} malicious files detected.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(8.dp))

                // Periodic Scan Settings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Periodic System Scan",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Automatically scan in background",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = periodicScanEnabled,
                        onCheckedChange = { isEnabled ->
                            viewModel.setPeriodicScanEnabled(isEnabled)
                        }
                    )
                }

                if (periodicScanEnabled) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Frequency:",
                            style = MaterialTheme.typography.bodySmall
                        )
                        
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            TextButton(onClick = { expanded = true }) {
                                Text(if (periodicScanInterval == 1L) "Daily" else if (periodicScanInterval == 7L) "Weekly" else "Monthly")
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Daily") },
                                    onClick = { viewModel.setPeriodicScanInterval(1L); expanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Weekly") },
                                    onClick = { viewModel.setPeriodicScanInterval(7L); expanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Monthly") },
                                    onClick = { viewModel.setPeriodicScanInterval(30L); expanded = false }
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        // Individual File Selection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "File",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                if (selectedFileName != null) {
                    Text(
                        text = "Selected File:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedFileName!!,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "No file selected",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { filePickerLauncher.launch("*/*") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is FileScannerUiState.Scanning
                ) {
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (selectedFileUri == null) "Select a File" else "Change File")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        // Dynamic Status / Action Section
        AnimatedVisibility(visible = true) {
            when (val state = uiState) {
                is FileScannerUiState.Initializing -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Initializing database... Please wait.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                is FileScannerUiState.Scanning -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Analyzing file hash...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                is FileScannerUiState.Safe -> {
                    ScanResultCard(
                        title = "File is Safe",
                        message = "No malware signatures detected.",
                        hash = state.hash,
                        icon = Icons.Default.CheckCircle,
                        color = Color(0xFF4CAF50)
                    )
                }
                is FileScannerUiState.Malicious -> {
                    ScanResultCard(
                        title = "Malicious File Detected!",
                        message = "This file matches a known malware signature. Do not open it.",
                        hash = state.hash,
                        icon = Icons.Default.Warning,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is FileScannerUiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                is FileScannerUiState.Idle -> {
                    // Action Buttons
                    Button(
                        onClick = {
                            if (selectedFileUri != null && selectedFileName != null) {
                                viewModel.scanFile(selectedFileUri!!, selectedFileName!!)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = selectedFileUri != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(Icons.Default.OfflineBolt, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Start Offline Scan", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.virustotal.com/gui/home/upload"))
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = uiState !is FileScannerUiState.Scanning
        ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Upload to VirusTotal", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun ScanResultCard(
    title: String,
    message: String,
    hash: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "SHA-256:\n$hash",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
