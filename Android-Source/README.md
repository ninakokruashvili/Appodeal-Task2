# Task 2 — Appodeal Android Integration & QA

Android Java demo, application ID `com.task2.appodealqa`. This is a Test Mode integration with banner, interstitial, rewarded video and native controls. All four Test Mode ads loaded and displayed; error-free initialization is still unresolved; see [QA report](qa/REPORT.md).

## Run

1. Open this folder in Android Studio. Use the included Gradle 8.13 wrapper and JDK 17 or 21, Android SDK platform 36 and Build Tools 36.0.0. Sync dependencies (network access required).
2. Run `app` on a connected Android device or emulator, API 24+ with internet access.
3. Paste the Appodeal app key registered for `com.task2.appodealqa` into the app, then tap **Initialize SDK**. The key is kept only in memory and never logged.
4. Wait for `onInitializationFinished` and check each `initialized=true` state.
5. For each format, tap **Load**, wait for its Loaded callback, then tap **Show**. Native appears above the event log; banner uses a dedicated bottom view. Scroll to native if necessary so it is visible.
6. Watch the rewarded video through completion, then repeat and close it early. Only Finished increases the demo reward total.

Test Mode is enabled unconditionally before initialization. All callbacks for the four requested ad formats and the initialization callback are logged with tag `AppodealQA`; verbose SDK logging is also enabled. ACTION lines describe requests, CALLBACK lines are SDK notifications, and STATE lines show initialization/reward checks. A successful show return value alone is not proof of an impression.

Dependencies use the modular core and AdMob adapter coordinates from Appodeal's official Android demo. The demo currently pins core 4.4.0, AdMob 25.2.0.0, IAB 1.8.1.0, and BidMachine 3.7.1.0. The final app builds and passes Android lint. Real device logs and screenshots are included, along with the unresolved configuration error. Google's sample AdMob application ID is used only for this test app. Test inventory availability also depends on the Appodeal account/network configuration.

## Evidence

Use `scripts/capture.sh logs` to start console capture and `scripts/capture.sh screenshot banner` (or interstitial/rewarded/native) while the ad is visible. The script uses `adb` from PATH or the usual Android SDK directory. Recording instructions and pass criteria are in [the QA report](qa/REPORT.md).

Local diagnostics ruled out a stale app cache, missing optional Adjust adapter and a failure unique to this registered package. The SDK configuration response omits its required services field; see the final local-investigation section in qa/REPORT.md. SDK errors remain visible.
