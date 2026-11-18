# Risk-Scanner

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)

**Risk-Scanner** is a comprehensive, on-device Android security suite designed to protect user privacy. It features advanced permission analysis, local network traffic monitoring, and memory-efficient malware detection to flag potential spyware, overzealous ad SDKs, and malicious files.

<p align="center">
  <img width="250" alt="Risk-Scanner Hero Image" src="https://github.com/user-attachments/assets/97000777-4c87-441e-93c8-268532766be4" />
</p>

---

## Key Features

### 1. Sensor & Permission Analyzer
Proactively monitors hardware sensor usage and evaluates system-wide application privileges.
* **Health Scoring:** Generates a cumulative privacy score for individual apps and the entire device based on sensor access, permissions, and developer trust (official vs. unofficial).
* **Live Monitoring:** Flags apps that request excessive permissions or utilize sensors suspiciously in the background.
* **Manual Control:** Toggle sensor monitoring on or off to optimize device battery life.

<p align="center">
  <img width="250" alt="Sensor Analyzer UI" src="https://github.com/user-attachments/assets/3bbc2e3d-2f6e-4b30-92f9-f7c68abc103e" />
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img width="250" alt="Device Score UI" src="https://github.com/user-attachments/assets/702e12b7-d173-460b-8ed4-6efe8eafba5d" />
</p>

### 2. Network Shield (Local Firewall & Traffic Monitor)
Utilizes Android's `VpnService` to route and inspect on-device traffic without sending your data to a remote server.
* **Traffic Visualization:** View cumulative and app-specific graphs of download and upload behaviors.
* **Anomaly Detection:** Automatically flags apps with suspicious transfer patterns or unusually high background uploads.
* **Alert System:** Real-time push notifications for flagged activity, alongside a dedicated Notification History screen to track persistent threats.

<p align="center">
  <img width="250" alt="Network Graph UI" src="https://github.com/user-attachments/assets/5477324a-3c75-4593-b59c-9691b2520e06" />
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img width="250" alt="Notification Log UI" src="https://github.com/user-attachments/assets/e5956338-6c68-4551-afa7-4eb893f65289" />
</p>

### 3. File & Malware Scanner
A dual-layered file scanning system utilizing highly efficient local cryptography and cloud intelligence.
* **Offline Hash Matching:** Powered by the **Google Guava Bloom Filter**. Quickly scans files (under a user-defined max size limit) against a massive pre-stored local database of known malicious signatures with extreme memory efficiency.
* **Online Deep Scan:** Seamless integration with **VirusTotal** for deep-dive analysis. Upload suspicious files directly to the VirusTotal web interface for cloud-based threat intelligence.

<p align="center">
  <img width="250" alt="File Scanner UI" src="https://github.com/user-attachments/assets/ff918d03-a3ed-46de-a6f5-fd463638b619" />
</p>

---

## Tech Stack & Architecture

* **Language:** Kotlin / Java
* **Framework:** Android SDK
* **Architecture:** MVVM (Model-View-ViewModel)
* **Core Libraries & APIs:**
  * `VpnService`: For local network packet interception and routing.
  * `SensorManager` & `AppOpsManager`: For system-level resource and permission monitoring.
  * `Google Guava`: Specifically utilizing Bloom Filters for rapid, memory-optimized $O(1)$ offline hash lookups.
  * `Room Database`: For persistent storage of network logs and notification history.
  * `Coroutines & Flow`: For asynchronous background scanning and real-time UI updates.

---

## Installation & Setup

To install Risk-Scanner directly on your Android device:

1. **Download the APK:** [Click here to download the latest Risk-Scanner release](https://drive.google.com/file/d/1ZIS9tmL0WY5KeunsQ0GG0spqovjDpFuC/view?usp=sharing)
2. **Locate the File:** Open your device's file manager and find the downloaded APK.
3. **Allow Permissions:** If prompted, enable **"Install from Unknown Sources"** in your Android security settings.
4. **Install:** Tap the APK file to install the application on your device.

*(Note: The Network Shield / VpnService features require a physical device and may not function accurately on standard Android emulators).*

---

## How It Works (Under the Hood)

* **Why a Bloom Filter?** Mobile devices have strict memory constraints. By using a Bloom Filter, Risk-Scanner can check a file's hash against millions of malware signatures locally without needing gigabytes of RAM or an active internet connection.
* **Why a Local VPN?** The Network Shield acts as a sinkhole. By creating a local VPN interface, the app can intercept and analyze network requests made by other apps directly on the device, ensuring user data never leaves the phone for analysis.
