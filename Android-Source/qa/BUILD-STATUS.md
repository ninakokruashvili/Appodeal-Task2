# Final build verification

Date: 7 October 2026.

Final SDK: Appodeal core 4.4.0. Adapters: IAB 1.8.1.0, BidMachine 3.7.1.0, AdMob 25.2.0.0. Android Gradle Plugin 8.13.2, Gradle 8.13, Java 21. Android minimum 24, compile/target 36.

`:app:assembleDebug :app:lintDebug` — BUILD SUCCESSFUL (2m 30s; 45 executed tasks). Installed successfully on Medium_Phone_API_37.0 Android emulator. Final lint report is saved in evidence/lint-report.html. Warnings concern available tool versions, Android target version, cleartext configuration, backup policy, missing launcher icon and untranslated demo strings; no lint errors remain.

Earlier fixes: SDK allowBackup manifest conflict resolved with tools:replace; BidMachine Media3 notification declaration added. The demo does not request notification permission or post notifications.

Runtime: all four Test Mode formats loaded and displayed. Interstitial closed and rewarded Finished/Closed(true) callbacks verified. Global initialization callback returned SdkConfigurationError. This is an unresolved requirement, not a passing initialization result.

Final APK: ../deliverables/AppodealQA.apk

After local diagnostics, the final SDK 4.4.0 source including its debug-only launch helper was rebuilt and passed assembleDebug/lintDebug in 9s. The restored APK is included in deliverables; final-build.log records this verification.
