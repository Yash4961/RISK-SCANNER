# Risk-Scanner — End-to-End System Architecture

> **Android Security Suite** | Kotlin + Jetpack Compose | MVVM | On-Device Privacy Analysis
> Architecture Version: 1.0 | Analyzed: October 2026

---

## Architecture Diagram

![Risk-Scanner System Architecture](C:\Users\Yash rathore\.gemini\antigravity-ide\brain\6f637191-a891-40ed-8cb9-a8a4dd829552\risk_scanner_architecture_1792557324480.jpg)

---

## Architectural Audit Summary

| Component Category | Status | Details |
|---|---|---|
| Frontend / UI | ✅ EXISTING | Jetpack Compose + Material3, 7 screens |
| Navigation | ✅ EXISTING | Navigation Compose with deep-link support |
| ViewModel Layer | ✅ EXISTING | 6 HiltViewModels with StateFlow |
| Dependency Injection | ✅ EXISTING | Dagger Hilt |
| Permission Scanner | ✅ EXISTING | AppScanner + PermissionDatabase (500+ definitions) |
| Malware Scanner | ✅ EXISTING | Guava BloomFilter + SHA-256, 3M signatures |
| Sensor Monitor | ✅ EXISTING | PrivacyGuardianService (ForegroundService) |
| Network Shield | ✅ EXISTING | NetworkMonitorVpnService (Android VpnService) |
| Background Scan | ✅ EXISTING | SystemScanWorker (WorkManager) |
| Boot Persistence | ✅ EXISTING | BootReceiver (BOOT_COMPLETED) |
| Local Database | ✅ EXISTING | Room v2 (SQLite), 5 entities |
| Preferences | ✅ EXISTING | Android DataStore |
| Push Notifications | ✅ EXISTING | NotificationManager (2 channels) |
| Real-time Charts | ✅ EXISTING | Vico Charts library |
| External Scan | ✅ EXISTING | VirusTotal (browser redirect only) |
| Authentication | ❌ NOT IMPLEMENTED | No user authentication layer |
| Remote Backend | ❌ NOT IMPLEMENTED | Fully on-device architecture |
| Cloud Sync | ❌ NOT IMPLEMENTED | No cloud sync or remote storage |
| API Gateway | ❌ NOT IMPLEMENTED | No REST API server |

---

## Layer-by-Layer Architecture Breakdown

---

### 01 — External Inputs & Device Surface

```
┌─────────────────────┐  ┌──────────────────────┐  ┌──────────────────────────┐
│  ANDROID DEVICE OS  │  │        USER           │  │       VIRUSTOTAL          │
│  Android 8.0+ API26 │  │    App Operator        │  │   External Cloud Service  │
│                     │  │                        │  │                          │
│ · Installed Apps    │  │ · Manual Scan Requests │  │ · Deep-file analysis     │
│ · File System       │  │ · Settings Changes     │  │ · Browser redirect only  │
│ · Hardware Sensors  │  │ · File Picker          │  │ · No API key needed      │
│ · Network Interface │  │ · VPN Toggle           │  │                          │
└──────────┬──────────┘  └──────────┬─────────────┘  └──────────────────────────┘
           │                        │                           │
    App Data / Sensors         User Actions              Browser Intent
```

**Key Points:**
- The device OS is the primary data source — no external data feeds.
- VirusTotal is the **only external system** and is accessed via a browser `Intent`, not an in-app API call. No API key or network request is made within the app.
- Risk-Scanner is a **fully on-device architecture**. No user data leaves the phone during scanning.

---

### 02 — Presentation / UI Layer

**Framework:** Jetpack Compose (BOM `2023.10.01`) + Material3 + Navigation Compose

| Screen | File | Key Features |
|---|---|---|
| Dashboard Screen | `DashboardScreen.kt` | Privacy score gauge, risk ring, sensor access summary for today, top risky apps, recent alerts |
| Scanner Screen | `ScannerScreen.kt` | Installed app list ordered by risk score, permission breakdown, installer source trust |
| Guardian Screen | `GuardianScreen.kt` | Sensor monitoring toggle, network shield VPN toggle, Vico usage graphs per-app and global |
| File Scanner Screen | `FileScannerScreen.kt` | File picker, full system scan with progress + pause/resume/cancel, VirusTotal redirect |
| Alerts Screen | `AlertsScreen.kt` | Historical privacy alerts, alert type classification, acknowledge / bulk clear |
| Network Usage Screen | `AppNetworkUsageScreen.kt` | Per-app download/upload bars, suspicious flagging, traffic history |
| Settings Screen | `SettingsScreen.kt` | Sensor whitelist, dark theme, boot start, scan interval, notification toggles |

**Deep-Link Navigation:** `MainActivity` receives `navigate_to=alerts` Intent extra from notification tap, routing directly to `AlertsScreen`.

---

### 03 — ViewModel / State Management Layer

**Pattern:** MVVM | **DI:** Dagger Hilt | **Async:** Kotlin Coroutines + Flow

| ViewModel | Depends On | Emits | Key Responsibility |
|---|---|---|---|
| `DashboardViewModel` | `ScannerRepository`, `GuardianRepository`, `PreferencesManager` | `DashboardUiState` (StateFlow) | Aggregates overall privacy score (100 - avg risk), risk level, today's sensor stats |
| `ScannerViewModel` | `AppScanner`, `ScannerRepository`, `PreferencesManager` | `ScannerUiState` (StateFlow) | Triggers app scan, stores results to Room, exposes scan report |
| `GuardianViewModel` | `GuardianRepository`, `PreferencesManager` | `GuardianUiState` (StateFlow) | Starts/stops `PrivacyGuardianService` and `NetworkMonitorVpnService`, observes `NetworkStatsRepository` |
| `FileScannerViewModel` | `MalwareScanner`, `PreferencesManager` | `FileScannerUiState`, `SystemScanState` | Triggers file/system scans, schedules `SystemScanWorker` via WorkManager |
| `AlertsViewModel` | `GuardianRepository` | `List<PrivacyAlert>` (Flow) | Provides alert list, acknowledge, delete operations |
| `SettingsViewModel` | `PreferencesManager`, `ScannerRepository`, `GuardianRepository` | `SettingsUiState` | Reads/writes all user preferences via DataStore |

---

### 04 — Core Services & Processing

This layer is split into two distinct halves.

---

#### 04-A: Background Services (Android System Services)

```
BOOT_COMPLETED
     │
     ▼
BootReceiver ──startForegroundService()──► PrivacyGuardianService
                                                    │
                                          15-second poll loop
                                                    │
                      ┌─────────────────────────────┼─────────────────────────┐
                      │                             │                         │
              AppOpsManager              UsageStatsManager            PowerManager
         (check sensor ops)          (foreground/recent apps)     (isInteractive)
                      │                             │
                      └──────────────SensorAccessLog INSERT──────────────────►Room DB
                      └──────────────PrivacyAlert INSERT (if suspicious)──────►Room DB
                      └──────────────Push Notification (HIGH priority)
```

**`PrivacyGuardianService`** — `ForegroundService` (`FOREGROUND_SERVICE_TYPE_SPECIAL_USE`)
- Polls every **15 seconds** using `AppOpsManager.unsafeCheckOpRawNoThrow()`
- Monitors: Camera (`OPSTR_CAMERA`), Microphone (`OPSTR_RECORD_AUDIO`), Location (`OPSTR_FINE_LOCATION`)
- Flags **background access** (app not in foreground) and **screen-off access** as suspicious
- De-duplicates logs: 5-min threshold per sensor per app (1-min if foreground)
- Configurable via `GuardianSettings` (camera/mic/location toggles, whitelist)

---

```
NetworkMonitorVpnService
        │
   VPN Builder ──► Virtual Interface (10.0.0.2/32, MTU 1500)
        │                (acts as local sinkhole — packets analyzed locally)
   2-second poll loop
        │
   NetworkStatsManager.querySummary()
        │           (Mobile + WiFi separately, all UIDs)
        │
   Per-app UID → RxBytes/TxBytes diff calculation
        │
   Suspicion check: diffTx > 5MB in 2 seconds → flag
        │
   NetworkStatsRepository.updateStats()  ◄── in-memory StateFlow (no DB write)
        │
   Push Notification (if suspicious app detected)
        └──► PrivacyAlert INSERT to Room DB
```

**`NetworkMonitorVpnService`** — `VpnService` (`BIND_VPN_SERVICE`)
- Does **not** actually inspect packet payloads — uses `NetworkStatsManager` for byte-count statistics only.
- The VPN tunnel is used to hold the `VpnService` binding; actual traffic inspection is done via Android's `NetworkStatsManager` API.
- Keeps **7 days of total usage data points** (604,800 entries max) in memory for the graph.
- Keeps **1 hour of per-app data** (3,600 entries/app) in memory to avoid OOM.

---

```
WorkManager (PeriodicWorkRequest)
    │
    ├── Interval: user-configurable (1–30 days)
    │
SystemScanWorker.doWork()
    │
    ├── MalwareScanner.initialize()
    ├── MalwareScanner.performFullSystemScan(maxBytes=100MB)
    └── GuardianRepository.saveAlert() for each malicious file found
```

**`BootReceiver`** — `BroadcastReceiver`
- Listens to `BOOT_COMPLETED` + `QUICKBOOT_POWERON`
- Auto-starts `PrivacyGuardianService` if `startOnBoot` preference is `true`

---

#### 04-B: Scanning Engines (Business Logic Singletons)

**`AppScanner`** — Hilt `@Singleton`

```
PackageManager.getInstalledPackages()
        │
        ▼ (per-app, on IO dispatcher)
PackageManager.getPackageInfo(GET_PERMISSIONS) ──► requestedPermissions[]
        │
        ▼
PermissionDatabase.getPermissionInfo(permission)
        │              (500+ defined permissions with risk weights)
        ▼
DangerousPermission list ──► Risk Score Calculation:
    · Raw score = sum(PermissionDatabase.getRiskWeight(riskLevel))
    · Normalize: avgWeight × 5 (max ~60)
    · Bonus: +8 per CRITICAL permission
    · SDK penalty: +15 if targetSdk < 23, +8 if < 26, +3 if < 28
    · Trust discount: ×0.65 if Play Store, ×0.70 if known package prefix, ×0.50 if system
    · Clamp to [0, 100]
        │
        ▼
RiskLevel: CRITICAL(≥70) | HIGH(≥50) | MEDIUM(≥25) | LOW(≥8) | INFO(<8)
        │
        ▼
ScanReport (totalApps, risk counts, top 10 permissions)
```

**`MalwareScanner`** — Hilt `@Singleton`

```
First run:
  assets/full_sha256.txt (69MB, ~3M SHA-256 hashes)
        │
        ▼
  BloomFilter.create(3_000_000 insertions, 0.001 false positive rate)
  ← Guava BloomFilter (Funnels.stringFunnel UTF-8)
        │
        ▼
  Serialize → filesDir/malware_bloom_filter.bin  (fast cache)

Subsequent runs:
  BloomFilter.readFrom(malware_bloom_filter.bin)

File scan:
  File/URI → SHA-256 (MessageDigest, 8KB streaming buffer)
        │
        ▼
  bloomFilter.mightContain(hash.lowercase())
        │
        ├── true  → ScanResult.Malicious(hash)
        └── false → ScanResult.Safe(hash)
```

> **Why Bloom Filter?** O(1) lookup against 3M signatures with ~3.5MB RAM footprint instead of loading a 69MB hash table. False positives at 0.1% rate; no false negatives.

**`PermissionDatabase`** — Static object
- Defines 500+ Android `Manifest.permission.*` entries
- Each entry: `PermissionDefinition(permission, name, riskLevel, category, description, risks[], mitigations[], secureAlternatives[])`
- Provides `getRiskWeight(RiskLevel)` for score calculation
- Categories: `SMS, CONTACTS, CAMERA, LOCATION, MICROPHONE, STORAGE, PHONE, CALENDAR, SENSORS, NETWORK, SPECIAL`

---

### 05 — Data & Persistence Layer

#### Room Database — `VScannerDatabase` (version 2, SQLite)

**File:** `vscanner_database` in app's internal storage

| Table | Entity Class | DAOs | Key Columns |
|---|---|---|---|
| `scanned_apps` | `ScannedApp` | `ScannedAppDao` | `packageName` (PK), `appName`, `riskScore`, `riskLevel`, `permissions`, `dangerousPermissions`, `installerSource`, `isSystemApp`, `isFromPlayStore` |
| `sensor_access_logs` | `SensorAccessLog` | `SensorAccessLogDao` | `id` (auto PK), `packageName`, `sensorType`, `accessTime`, `wasInBackground`, `wasScreenOff`, `isSuspicious`, `suspiciousReason` |
| `privacy_alerts` | `PrivacyAlert` | `PrivacyAlertDao` | `id` (auto PK), `packageName`, `alertType`, `sensorType`, `message`, `timestamp`, `wasAcknowledged` |
| `app_sensor_stats` | `AppSensorStats` | `AppSensorStatsDao` | `packageName` (PK), `cameraAccessCount`, `microphoneAccessCount`, `locationAccessCount`, `totalBackgroundAccesses`, `lastCameraAccess`, `lastMicrophoneAccess`, `lastLocationAccess` |
| `daily_sensor_summary` | `DailySensorSummary` | `DailySummaryDao` | `date` (PK, YYYY-MM-DD), `totalCameraAccesses`, `totalMicrophoneAccesses`, `totalLocationAccesses`, `totalBackgroundAccesses`, `alertsTriggered` |

**Repository Layer:**
- `GuardianRepository` — wraps `SensorAccessLogDao`, `PrivacyAlertDao`, `AppSensorStatsDao`, `DailySummaryDao`
- `ScannerRepository` — wraps `ScannedAppDao`
- All operations use `withContext(Dispatchers.IO)` — never block the main thread.
- Reactive data exposed as `Flow<List<T>>` for UI observation.

#### Android DataStore — `vscanner_preferences`

| Preference Key | Type | Default | Purpose |
|---|---|---|---|
| `guardian_enabled` | Boolean | false | Global guardian toggle |
| `monitor_camera` | Boolean | true | Camera sensor tracking |
| `monitor_microphone` | Boolean | true | Mic sensor tracking |
| `monitor_location` | Boolean | true | Location sensor tracking |
| `alert_on_background` | Boolean | true | Alert on background access |
| `alert_on_screen_off` | Boolean | true | Alert on screen-off access |
| `whitelisted_apps` | StringSet | empty | Per-app monitoring whitelist |
| `include_system_apps` | Boolean | false | Scanner includes system apps |
| `dark_theme` | Boolean | true | UI theme |
| `start_on_boot` | Boolean | false | Boot auto-start |
| `notifications_enabled` | Boolean | true | Push notification toggle |
| `periodic_file_scan_enabled` | Boolean | false | WorkManager schedule enabled |
| `periodic_file_scan_interval` | Long | 7 (days) | WorkManager interval |

#### Local Filesystem

| File | Location | Size | Purpose |
|---|---|---|---|
| `assets/full_sha256.txt` | APK assets bundle | ~69 MB | 3M+ SHA-256 malware hash signatures (MalwareBazaar) |
| `malware_bloom_filter.bin` | `context.filesDir` | ~3.5 MB | Serialized Guava BloomFilter cache (built on first run) |

---

### 06 — Notifications & User Feedback Output

#### Notification Channels

| Channel ID | Name | Priority | Trigger |
|---|---|---|---|
| `privacy_guardian_channel` | Privacy Guardian | LOW | Ongoing foreground service notification |
| `privacy_alert_channel` | Privacy Alerts | HIGH | Suspicious sensor access detected |
| `vpn_monitor_channel` | VPN Network Monitor | LOW | Ongoing VPN service notification |
| *(alert channel)* | Privacy Alert (VPN) | HIGH | Suspicious network upload detected |

All alert notifications deep-link back to `MainActivity` with `navigate_to=alerts` extra.

#### Real-Time UI Updates

```
Background Service (IO Thread)
        │
        │ updateStats() / saveAlert()
        ▼
  Room DB / NetworkStatsRepository (StateFlow)
        │
        │ Flow<T> / StateFlow<T>
        ▼
  ViewModel (Default/Main dispatcher)
        │
        │ collectAsState()
        ▼
  Compose UI (Main Thread recomposition)
```

Data rendering libraries:
- **Vico Charts** (`com.patrykandpatrick.vico:compose:1.13.0`) — Network traffic line charts on Guardian Screen
- **Compose Material3** — Risk gauge rings, progress bars, animated score cards on Dashboard

#### Security Report Output

`ScanReport` exposed by `ScannerViewModel`:
```
ScanReport {
  scanTime: Long
  totalApps: Int
  criticalRiskApps: Int
  highRiskApps: Int
  mediumRiskApps: Int
  lowRiskApps: Int
  totalDangerousPermissions: Int
  mostCommonPermissions: List<Pair<String, Int>>   // Top 10
  apps: List<ScannedApp>                            // Sorted by riskScore DESC
}
```

---

## End-to-End Workflow Traces

### Workflow A: App Permission Scan

```
User taps "Scan" (ScannerScreen)
    │
    ▼
ScannerViewModel.startScan()
    │
    ▼
AppScanner.scanAllApps() → Flow<ScanProgress> (IO dispatcher)
    │
    ├── ScanProgress.Started(totalCount) → UI progress bar
    ├── ScanProgress.Progress(index, total, scannedApp) → per-app row appears
    ├── ScanProgress.Error(pkg, msg) → skipped
    └── ScanProgress.Completed(ScanReport) → summary screen shown
    │
    ▼
ScannerRepository.saveApps(List<ScannedApp>) → Room DB
    │
    ▼
DashboardViewModel observes getAllAppsFlow() → recalculates privacy score
    │
    ▼
Dashboard UI updates score ring + risk breakdown
```

### Workflow B: Sensor Access Detection

```
PrivacyGuardianService running (15-sec loop)
    │
    ▼
getRecentlyActivePackages() via UsageStatsManager (last 10 min)
    │
    ▼
For each active package:
    AppOpsManager.unsafeCheckOpRawNoThrow(OPSTR_CAMERA | OPSTR_RECORD_AUDIO | OPSTR_FINE_LOCATION)
    │
    ├── MODE_ALLOWED + wasInBackground + alertOnBackgroundAccess
    │       └── isSuspicious = true, reason = "Background sensor access"
    │
    └── MODE_ALLOWED + wasScreenOff + alertOnScreenOffAccess
            └── isSuspicious = true, reason = "Sensor access while screen off"
    │
    ▼
repository.logSensorAccess(SensorAccessLog) → INSERT sensor_access_logs
repository.updateAppStats(packageName, sensorType, isBackground) → UPSERT app_sensor_stats
    │
    ▼ (if suspicious)
repository.saveAlert(PrivacyAlert) → INSERT privacy_alerts
NotificationManager.notify() → HIGH priority push notification
    │
    ▼
AlertsViewModel observes getRecentAlertsFlow() → alert list updates
AlertsScreen recomposes with new alert entries
```

### Workflow C: Malware File Scan

```
User selects file via FilePickerLauncher (FileScannerScreen)
    │
    ▼
FileScannerViewModel.scanFile(uri, fileName)
    │
    ▼
MalwareScanner.initialize() (if not already done)
    │   → loads malware_bloom_filter.bin from filesDir
    │   → OR builds from assets/full_sha256.txt (first run, ~30s)
    │
    ▼
MalwareScanner.scanFile(uri)
    │
    ├── ContentResolver.openInputStream(uri) → streaming SHA-256 (8KB buffer)
    │
    ▼
hash.lowercase() → bloomFilter.mightContain(hash)
    │
    ├── true  → ScanResult.Malicious → FileScannerUiState.Malicious (red UI)
    │               User can tap → Intent to VirusTotal (browser)
    └── false → ScanResult.Safe    → FileScannerUiState.Safe (green UI)
```

### Workflow D: Network Anomaly Detection

```
NetworkMonitorVpnService running (2-sec loop)
    │
    ▼
NetworkStatsManager.querySummary(MOBILE + WIFI, 0, currentTime)
    │
    ▼
Per-UID RxBytes + TxBytes accumulated
    │
    ▼
Diff from previous poll (diffTx per app)
    │
    ├── diffTx > 5MB → isSuspicious = true
    │       └── sendPrivacyAlert() → INSERT privacy_alerts + Push Notification
    │
    ▼
NetworkStatsRepository.updateStats(
    List<AppNetworkUsage>,     // current per-app stats
    totalGlobalDiff,           // for global graph
    appDiffs                   // for per-app history graph
)
    │
    ▼
GuardianViewModel.networkUsageFlow.collect() → GuardianScreen Vico chart redraws
```

### Workflow E: Periodic Background Malware Scan

```
WorkManager triggers SystemScanWorker (every N days, user-configured)
    │
    ▼
SystemScanWorker.doWork()
    │
    ├── MalwareScanner.initialize()
    └── MalwareScanner.performFullSystemScan(maxBytes=100MB)
            │
            ├── Environment.getExternalStorageDirectory() recursive walk
            ├── Skip files > 100MB
            └── SHA-256 → BloomFilter lookup per file
    │
    ▼
For each ScanResult.Malicious:
    GuardianRepository.saveAlert(PrivacyAlert) → INSERT privacy_alerts
    │
    ▼
AlertsViewModel observes → AlertsScreen shows new threat alert
```

---

## Technology Stack Index

| Technology | Version | Role |
|---|---|---|
| Kotlin | 2.x | Primary language |
| Android SDK | API 26–35 (min 26, target 35) | Platform |
| Jetpack Compose | BOM 2023.10.01 | UI framework |
| Material3 | (via Compose BOM) | Design system |
| Navigation Compose | 2.7.5 | Screen routing + deep links |
| Dagger Hilt | 2.60.1 | Dependency injection |
| Room | 2.8.4 | Local SQLite ORM |
| Android DataStore | 1.0.0 | Key-value preferences |
| WorkManager | 2.9.0 | Background periodic tasks |
| Kotlin Coroutines | 1.7.3 | Async / concurrency |
| Kotlin Flow / StateFlow | (stdlib) | Reactive data streams |
| Google Guava | 32.1.3-android | BloomFilter implementation |
| Gson | 2.10.1 | JSON type converters for Room |
| Vico Charts | 1.13.0 | Network usage graphs |
| Android VpnService | (system) | Local VPN tunnel for network monitoring |
| AppOpsManager | (system) | Sensor access monitoring |
| NetworkStatsManager | (system) | Per-UID network statistics |
| UsageStatsManager | (system) | Foreground app detection |
| VirusTotal | External | Browser-redirect deep scan (no API key) |

---

## Architectural Boundaries

```
┌──────────────────────────────────────────────────────────────────────┐
│  APPLICATION BOUNDARY (Risk-Scanner APK)                             │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  CLIENT TIER: Jetpack Compose UI (7 screens, 6 ViewModels)   │   │
│  └──────────────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  SERVICES TIER: Background Services + Scanning Engines        │   │
│  │  (PrivacyGuardianService, NetworkMonitorVpnService,           │   │
│  │   SystemScanWorker, AppScanner, MalwareScanner)               │   │
│  └──────────────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  DATA TIER: Room (SQLite) + DataStore + Local Filesystem       │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────┐
│  EXTERNAL BOUNDARY                   │
│  VirusTotal (browser intent only)    │
│  Android OS APIs (system calls)      │
└──────────────────────────────────────┘
```

> [!IMPORTANT]
> **This is a fully on-device security architecture.** No user data, files, or scan results are transmitted to any remote server. VirusTotal is accessed only via a browser redirect from the UI — no API call is made within the app.

> [!NOTE]
> **Why local VPN?** `NetworkMonitorVpnService` creates a local VPN tunnel to hold the Android `VpnService` binding, giving it the right to observe network traffic metadata. It does **not** proxy or inspect packet payloads — all analysis is done via `NetworkStatsManager` (byte count APIs), ensuring privacy-preserving network monitoring entirely on-device.
