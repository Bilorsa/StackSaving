# StackSave — Android Application Prototype

A modern, offline-first Android application designed to help shoppers discover grocery promotions, monitor household savings metrics, and manage baskets dynamically. StackSave features Firebase cloud persistence, REST API connectivity with local fallback resilience, accessible multi-language localization (English & isiZulu), and an adaptive UI.

---

## 📺 Video Demonstration

- **YouTube Walkthrough:** [Watch the StackSave Demo](https://youtu.be/1q13_FuJwlc)

---

## 📋 Rubric & Portfolio of Evidence (POE) Coverage

| Rubric Requirement | Implementation Details | Status |
| :--- | :--- | :---: |
| **Authentication & Security** | Masked password inputs, client-side input validation, registration, and login flows. | Complete |
| **Cloud Persistence** | Firebase Authentication integrated with Cloud Firestore for user profiles and settings synchronization. | Complete |
| **Settings & Preferences** | Persistent user configurations stored locally and synced with cloud profiles upon login. | Complete |
| **Bilingual Localization** | Runtime localization switching between **English** and **isiZulu** with dynamic string re-binding. | Complete |
| **Remote REST API Feed** | HTTP client fetching deals feed with automated fallback to local bundled JSON during network failures. | Complete |
| **Offline Cache & Sync** | Local cache layer with automated pull-to-refresh sync detection and update handling. | Complete |
| **Local Notifications** | In-app notification triggers demonstrating timed deal alerts and background broadcast handling. | Complete |
| **FCM Scaffold (POE Scope)** | Firebase Cloud Messaging background service skeleton marked for POE grading scope. | Complete |
| **Google SSO (POE Scope)** | Single Sign-On integration intent and credential broker placeholders marked for POE scope. | Complete |

---

## 🛠 Tech Stack & Specifications

- **Platform:** Android OS
- **Target SDK:** API 34+
- **Minimum SDK:** API 24 (Android 7.0 Nougat)
- **Programming Language:** Kotlin / Java
- **Development Tool:** Android Studio (Ladybug / Hedgehog or newer)
- **JDK Version:** OpenJDK 17
- **Backend / BaaS:** Firebase (Authentication, Cloud Firestore, Cloud Messaging scaffold)
- **Networking:** REST client with resilient offline JSON fallbacks
- **Architecture Pattern:** MVVM (Model-View-ViewModel) with Single Source of Truth Repository

---

## 🚀 Getting Started & Setup Guide

### 1. Prerequisites
Ensure you have **Android Studio** installed and configured to use **JDK 17**:
- Navigate to **Settings / Preferences** $\rightarrow$ **Build, Execution, Deployment** $\rightarrow$ **Build Tools** $\rightarrow$ **Gradle**.
- Set **Gradle JDK** to **Embedded JDK 17** or an installed OpenJDK 17.

### 2. Firebase Configuration
To run cloud synchronization and authentication against your own backend:
1. Open the [Firebase Console](https://console.firebase.google.com/) and create a new project.
2. Register an Android Application with the package identifier:
   ```text
   com.example.stacksave
   ```
3. Download your generated `google-services.json` file.
4. Place the file inside the `app/` directory of this project:
   ```text
   StackSave/
   └── app/
       ├── google-services.json
       ├── build.gradle.kts (or build.gradle)
       └── src/
   ```
5. In the Firebase Console:
   - Navigate to **Authentication** $\rightarrow$ **Sign-in method** and enable **Email/Password**.
   - Navigate to **Firestore Database** $\rightarrow$ **Create database** (start in Test Mode or deploy the local `firestore.rules`).

### 3. Build & Run
1. Open this repository folder in **Android Studio**.
2. Allow Android Studio to sync the Gradle build files and resolve all project dependencies.
3. Select your target device (Physical Android phone with USB debugging enabled or an Android Virtual Device / Emulator running API 26+).
4. Click **Run** (`Shift + F10`) or debug the project directly.

---

## 🎨 UI, UX & Accessibility Enhancements

- **Persistent Dark Mode:** User-controlled theme engine stored locally, avoiding flash-of-white on launch and adhering to Material Design dark themes.
- **Reduce Motion Accessibility:** User-selectable setting to minimize intense transitions, tab sliding animations, and avatar pulses for users sensitive to motion.
- **Unified Design Hierarchy:** Restyled **Profile** tab to maintain visual parity with the **Eats & Deals**, **Basket**, and **Analytics** tabs using branded gradient headers and surface elevation.
- **Live User Analytics:** Comprehensive profile dashboard surfacing:
  - Total accumulated savings & savings rate percentage
  - Active tracked deals & average deal price
  - Selected primary shopping region/area
  - Instant account sync loading state after Firebase authentication
- **Accessible Content Controls:** Semantic content labels (`contentDescription`) applied across all interactive widgets and icon elements for screen-reader accessibility (TalkBack compatible).

---

## 📱 Visual Demonstration & Screenshots

<p align="center">
  <img width="220" alt="Eats and Deals Screen" src="https://github.com/user-attachments/assets/54cbb3e4-3c75-4ba8-b3f2-623af17bbbf1" />
  <img width="220" alt="Basket and Specials" src="https://github.com/user-attachments/assets/e6fe95af-c2dd-44f5-8102-4b66a9755aad" />
  <img width="220" alt="Analytics and Savings Breakdown" src="https://github.com/user-attachments/assets/21ab9bcf-4443-4a23-92cb-f83e4a65a665" />
  <img width="220" alt="User Profile and Preferences" src="https://github.com/user-attachments/assets/8d02f144-7ea5-4145-bd0b-21dbe24d34e6" />
</p>

---

## 📂 Project Directory Structure

```text
StackSave/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/stacksave/
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/          # REST client interfaces & DTO models
│   │   │   │   │   ├── local/        # SharedPrefs, local cache & fallback assets
│   │   │   │   │   └── repository/   # Unified repository (API + offline cache)
│   │   │   │   ├── services/
│   │   │   │   │   ├── fcm/          # Firebase Cloud Messaging service handlers
│   │   │   │   │   └── notification/ # Local alert and notification managers
│   │   │   │   ├── ui/
│   │   │   │   │   ├── auth/         # Login, registration & validation
│   │   │   │   │   ├── deals/        # Deal feeds, filters & adapters
│   │   │   │   │   ├── basket/       # Cart, checkout & budget calculation
│   │   │   │   │   ├── analytics/    # Metrics, charts & summary models
│   │   │   │   │   └── profile/      # User account, dark mode & language settings
│   │   │   │   └── utils/            # Locale helpers, animations & formatters
│   │   │   ├── res/
│   │   │   │   ├── values/           # Default English strings & color schemes
│   │   │   │   ├── values-zu/        # isiZulu localization strings
│   │   │   │   └── values-night/     # Dark mode theme overrides
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle.kts
│   ├── google-services.json          # (Add your file here)
│   └── firestore.rules
└── README.md
```
