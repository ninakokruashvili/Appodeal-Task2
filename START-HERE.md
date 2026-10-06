# Task 2 — SDK Integration & QA submission

Platform: Android. Package: com.task2.appodealqa. SDK: Appodeal 4.4.0. Test Mode is enabled.

- **QA/REPORT.md**: full QA report, implementation explanation, troubleshooting, documentation and AI-assistance details.
- **QA/evidence/**: real screenshots of banner, interstitial, rewarded video and native ads; console/callback logs; final build output and lint report.
- **APK/AppodealQA.apk**: installable debug demo.
- **Android-Source/**: Android Studio project, Gradle wrapper and capture tools. Open this folder in Android Studio.

The source project contains a duplicate QA folder so its relative documentation links and evidence-capture scripts work independently.

## Actual result

All four Test Mode formats loaded and displayed. Their relevant callbacks were recorded. The Android build and lint checks passed.

**Unresolved requirement:** the global initialization callback reports SdkConfigurationError, despite all four format initialization flags being true. Local investigation confirmed a missing services field in the SDK configuration response. The report documents the comparison tests and remaining limitation honestly; this submission is not a fully passing implementation.

The Appodeal app key is not included. Enter the app key at runtime to reproduce. No GitHub login credentials are needed to review these files.
