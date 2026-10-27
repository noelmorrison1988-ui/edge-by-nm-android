# EDGE by NM — Android Mobile APK

This is an **Android source project**, not a precompiled APK. It loads the existing working HTTPS EDGE site in a dedicated Android WebView. The YouTube API key stays securely on Netlify and is not included in the project. It doesn't require login.

## Build the APK (easiest: GitHub Actions)
1. Create a **new private GitHub repository** named `edge-by-nm-android` (do not overwrite the existing Netlify website repository).
2. Upload the **extracted project folders**, preserving `.github/workflows/build-apk.yml`, `app`, `build.gradle`, `settings.gradle`, and `gradle.properties`. GitHub web uploads may hide dot-directories; alternatively use a Git client to push the project.
3. Open **Actions → Build EDGE Mobile APK → Run workflow** (or commit on main to trigger automatically).
4. When the run succeeds, download artifact `EDGE-by-NM-Mobile-debug-APK`; extract ZIP and install `app-debug.apk` on Android, accepting the trusted source installation prompt.

**Build requires internet and Android SDK**. Debug builds are signed automatically for sideloading. Keep a consistent signing key before distributing future release updates; debug keystores on GitHub hosted runners can change, and updates may require uninstalling the previous app. This package is for personal testing.

## Android Studio method
Open project folder; allow Gradle to sync; use Build > Build APK(s) (debug). Android Gradle plugin 8.9.2, Gradle 8.11.1, JDK 17, compileSdk 35; minimum Android 8.0.

## Limitations
- YouTube iframe playback **inside Android WebView requires on-device testing**; Error 153 may occur on some WebView versions, despite the hosted site working in Chrome. Update Android System WebView/Chrome if needed. A Trusted Web Activity (TWA) can be a fallback but requires Digital Asset Links on your Netlify site.
- Videos require an internet connection. The live site updates without rebuilding the APK.
- Android system navigation and embedded YouTube fullscreen may differ from Chrome. The wrapper requests audible autoplay, but platform/provider restrictions still apply.
- The standalone APK has not been compiled or tested in this environment.
