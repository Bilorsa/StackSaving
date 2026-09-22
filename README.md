# StackSave - Android Studio Prototype

University prototype for comparing store deals, Firebase authentication/cloud profiles, offline caching, settings, REST API integration, notifications, SSO and multilingual UI.

## Firebase setup
1. Create a NEW Firebase project.
2. Add Android app package: `com.example.stacksave`.
3. Download `google-services.json` and place it in `app/google-services.json`.
4. Enable Authentication > Email/Password.
5. Create Firestore Database and deploy `firestore.rules`.
6. Sync Gradle in Android Studio.

The Google Services Gradle plugin is applied only when `app/google-services.json` exists. Until then, the app runs in local demo mode so you can build and demonstrate the UI.

## Android Studio
Use JDK 17. Open this folder, allow Gradle sync, then run on an emulator or Android phone.

## Rubric coverage
- Register/login and masked password input
- Firebase Auth + Firestore profile storage when configured
- Settings
- REST API deal feed with local fallback
- Offline cache and re-sync on refresh
- Google SSO placeholder marked POE-only
- Firebase Cloud Messaging service scaffold marked POE-only
- Local notification demonstration
- English + isiZulu language selector
