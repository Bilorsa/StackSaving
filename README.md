# StackSave - Android Studio Prototype



## Firebase setup
1. Create a NEW Firebase project.
2. Add Android app package: `com.example.stacksave`.
3. Download `google-services.json` and place it in `app/google-services.json`.
4. Enable Authentication > Email/Password.
5. Create Firestore Database and deploy `firestore.rules`.
6. Sync Gradle in Android Studio.



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


## UI / Accessibility Improvements
- Added a user-controlled **Dark mode** setting that persists across app launches.
- Added **Reduce motion** accessibility support; main tab transitions and the profile avatar animation can be simplified.
- Redesigned the **Profile** tab to match the Eats & Deals, Basket and Analytics tabs.
- Profile now surfaces saved deals, total savings, savings rate, active deals, average deal price, shopping area, language, offline sync and notification settings.
- Added editable display name support and account sync loading after Firebase login.
- Added richer branded gradients, cards, icon treatments and subtle animation while keeping accessibility labels on interactive controls.

**YouTube Link:** https://youtu.be/1q13_FuJwlc
