# Task 2 — QA report

Date: 7 October 2026. Platform: Android. Package: `com.task2.appodealqa`.

## Current result

**All four formats loaded and displayed in Test Mode, with real screenshots and SDK callbacks. Initialization returned a configuration error, so the full requirement is not yet passed.** The SDK reported `isInitialized=true` for each format even while `onInitializationFinished` returned `SdkConfigurationError`. This demonstrates why those flags alone are insufficient to claim error-free initialization.

Observed session: Android API 37 emulator, package `com.task2.appodealqa`, Appodeal 4.4.0, IAB 1.8.1.0, BidMachine 3.7.1.0, AdMob 25.2.0.0. Both 4.3.0 and 4.4.0 returned the same configuration error; final screenshots use 4.4.0.

| Format | SDK initialized flag | Loaded | Displayed | Observed callbacks | Evidence |
|---|---|---|---|---|---|
| Banner | true, global error | Yes | Yes | Loaded, Shown | [screenshot](evidence/banner.png) |
| Interstitial | true, global error | Yes | Yes | Loaded, Shown, Closed | [screenshot](evidence/interstitial.png) |
| Rewarded video | true, global error | Yes | Yes | Loaded, Shown, Finished, Closed(true) | [screenshot](evidence/rewarded.png) |
| Native | true, global error | Yes | Yes | Loaded, Shown | [screenshot](evidence/native.png) |

Console evidence: [complete SDK output](evidence/console.log), [application callbacks](evidence/callbacks.log), [first initialization failure](evidence/initialization-error.log). Keys are redacted from saved logs. Screenshots were visually inspected.

Rewarded completion returned amount=0.0 and empty currency in this test configuration; the demo logged those actual values without substituting a fake reward. Completion callback preceded Closed(finished=true). Click, expiry, failure and early-close callbacks are implemented but have not all been observed; these cases remain unverified.

## Implementation

One Java Activity builds a minimal UI with independent Load and Show controls. SDK Test Mode and verbose logging are set before initialization. All four formats are included in the initialization mask, with automatic caching disabled for predictable manual tests. Initialization completion logs the SDK error list and a separate `isInitialized` check for each format. Load uses `cache`; Show checks initialization and loaded inventory first.

Banner occupies its own bottom view. Interstitial and rewarded use SDK full-screen presentation. Native uses Appodeal's NewsFeed template and registers one ad obtained from the SDK cache. The template supplies ad assets and attribution. Native views are destroyed when the Activity is destroyed. Screen rotation is handled without recreating this demo Activity.

Every callback defined by the four ad-format interfaces is implemented: Loaded, FailedToLoad, Shown, ShowFailed, Clicked and Expired; interstitial additionally Closed, rewarded additionally Finished and Closed. Initialization is also logged. Callback parameters are included, including precache flags, banner height, reward amount/currency and rewarded closure completion. A reward is added only in Finished. The UI retains the latest events, while Logcat provides the complete session output.

## Reproduction / remaining QA procedure

1. Start console capture before initializing. Record device model, Android version, network, SDK version, installed adapter and session date/time.
2. Enter the app key for the registered package and tap Initialize SDK. Handle any SDK consent prompt normally. Capture the initialization callback and all four initialized=true states. Errors must be investigated rather than marked successful.
3. Banner: Load → Loaded → Show → Shown. Capture the visible banner. Hide it and check that it disappears.
4. Interstitial: Load → Loaded → Show → Shown → dismiss → Closed. Capture the full-screen ad before dismissing.
5. Rewarded: Load → Loaded → Show → Shown → watch to completion → Finished(amount,currency) → Closed. Capture the video and completion log. Repeat with early dismissal: verify no reward is added without Finished.
6. Native: Load → Loaded → Show → scroll the template into view → Shown. Capture its assets, Ad label and CTA. Registration is an action, not impression proof.
7. On test creatives, click each ad and confirm Clicked if the creative permits interaction. Record any callback that cannot be elicited as untested. Never manufacture callback evidence.
8. Negative checks: Show before Load must be blocked. Test offline load and log actual failure behavior, allowing for previously cached content. Expiry and SDK show-failure callbacks require an actual SDK event; do not claim they were observed merely because the handlers exist.
9. Save console.log and one screenshot per format. Optionally record all formats with `scripts/capture.sh record`, then `scripts/capture.sh pull-recording`.

### Acceptance criteria

A format passes only with all of: initialized=true, its Loaded callback, a screenshot/recording of its real Test Mode creative, and its Shown callback. Interstitial also requires Closed; rewarded completion requires Finished and Closed. Record unexpected ordering/repeated callbacks as observed; do not assume exactly one callback per button press.

## Troubleshooting and investigation

| Problem encountered | Investigation | Resolution/status |
|---|---|---|
| SDK manifest disagreed with app backup setting | Manifest merger reported allowBackup=true from SDK vs false from app | Added explicit tools:replace override; subsequent Java compilation passed |
| First runtime initialization returned SdkConfigurationError | Inspected verbose SDK stack trace and missing-adapter warnings; checked Test Mode documentation | Added mandatory IAB and BidMachine adapters; all four test formats loaded/displayed, but configuration error persisted. Core 4.4.0 also returned the same error. |
| Empty initial workspace | Inspected project files and installed platform tools | Created an Android project |
| `xcodebuild` exists but Xcode is absent | Checked selected developer directory and simulator tooling | Chose Android |
| Android tools absent from PATH | Checked Android Studio and SDK directories | SDK located under `~/Library/Android/sdk`; capture script uses its full path |
| Official demo clone failed with DNS error | Retried with network approval; download was declined | Read official documentation and demo files through web access |
| Initial SDK dependency was outdated | Compared it with official demo and SDK upgrade guide | Replaced legacy bundled coordinate with modular core and AdMob adapter |
| Android Studio bundles Java 25 | Checked runtime version and Gradle compatibility | Downloaded temporary Java 21 and generated the Gradle 8.13 wrapper |
| App key initially unavailable | Guided registration of unpublished Android app | Key received; runtime verification pending |

## Documentation used

- [Android setup and initialization](https://docs.appodeal.com/android/get-started)
- [Test Mode](https://docs.appodeal.com/android/advanced/testing)
- [Banner API and callbacks](https://docs.appodeal.com/android/ad-types/banner)
- [Interstitial API and callbacks](https://docs.appodeal.com/android/ad-types/interstitial)
- [Rewarded video API and callbacks](https://docs.appodeal.com/android/ad-types/rewarded-video)
- [Native templates, callbacks and cleanup](https://docs.appodeal.com/android/ad-types/native)
- [SDK upgrade guide: modular dependency migration](https://docs.appodeal.com/android/upgrade-guide)
- [Official Android native demo dependencies](https://github.com/appodeal/appodeal-android-sdk/blob/master/native/build.gradle)
- [Android Gradle Plugin 8.13 compatibility](https://developer.android.com/build/releases/agp-8-13-0-release-notes)

## AI assistance and corrections

Codex helped inspect the environment, research official APIs, draft the app, add callback logging, and prepare this QA procedure. Its web access retrieved official documentation. No other AI tools were used in this session.

Incorrect/incomplete AI output encountered: the initial generated dependency `com.appodeal.ads:sdk:4.4.0.0` was based on an outdated bundle pattern and was replaced after checking the official source. The first minimal dependency selection included only AdMob and omitted the required Test Mode adapters; real SDK logs and the testing guide identified this gap, and IAB/BidMachine were added. Initial tool checks only examined PATH, missing an Android SDK that existed outside PATH; a directory inspection corrected that. The first explanation that live Test Mode capture needed a key was incomplete about other prerequisites: a compatible Java/Gradle setup, SDK platform, resolved network adapter, and available test inventory are also required.

This report intentionally separates implemented handlers from callbacks observed on a device. Successful logs and screenshots cannot be inferred from source code.

## Initialization error investigation

The exception is `IllegalArgumentException: Required value was null` in the SDK networking configuration parser. Inspection of the SDK bytecode maps this exception to a missing configuration response or missing `services` JSON object. This is diagnostic evidence, not a confirmed root cause. Adding required adapters removed the adapter warnings and enabled Test Mode ads, but did not remove this initialization error. The user confirmed the package/key association; core 4.4.0 returned the same error. No SDK response is patched and no success log is fabricated.

## Remaining action

Investigate the configuration response with Appodeal support or the account configuration. The user has confirmed the registered Bundle ID and app key match the running app. The error occurred on both core 4.3.0 and 4.4.0 with required adapters installed. All tests kept Test Mode enabled; no live ads were requested intentionally. Use the saved SDK logs and package/version details to reproduce. Error-free initialization must be rechecked after resolution.

## Account configuration and support follow-up — 7 October 2026

Inspected the authenticated Appodeal dashboard. Task2's registered Bundle ID and displayed app key match the running app. The app list still shows “No SDK”; onboarding is incomplete. The SDK Integration page shows 25% progress and “No First Ad impression yet,” even after a refresh. Autoconnection is enabled; AdMob account sync and own-network-account confirmations are unchecked. No unsupported account claims or live-mode changes were made. These observations do not establish the root cause of SdkConfigurationError.

Created Appodeal SDK Issue ticket **#142311431** through the dashboard. The visible confirmation states “Ticket is created.” The ticket includes version/package details, integration sequence, exact error/stacktrace, and successful Test Mode results. It requests investigation of the configuration/services response. The app key was not included; Bundle ID was supplied. Support is offline and an email notification is expected when the ticket is updated.

Error-free initialization has **not** been rechecked after a resolution, because no resolution has been supplied yet. The next step is to apply Appodeal's verified fix or account configuration advice, repeat Test Mode initialization, and require an error-free callback before marking the initialization criterion passed.

## Local investigation without further support contact

Fresh internal diagnostics and the test app's private SDK cache were inspected locally. The cached response was freshly fetched from `/config`; it contained `token`, `split_mmp_init` and `ext`, with **no `services` field**. `split_mmp_init` was false. The SDK parser requires that object and throws `Required value was null` when it is absent. Saved evidence contains only response field names, not the response token or user key: [configuration structure](evidence/config-response-structure.json).

Comparison tests:

| Test | Result |
|---|---|
| Default endpoint override check | No custom endpoint override was set |
| Fresh SDK 4.4.0 initialization with internal logging | Same SdkConfigurationError |
| Added official Adjust 5.7.0.0 service adapter and cleared the disposable demo cache | Same error; fresh configuration still omitted services |
| Separate comparison using Appodeal's official public demo package/key | Same error and missing field |
| Prior core SDK 4.2.0 with fresh demo cache | Same SdkConfigurationError |

The public demo comparison used the package/key published in [Appodeal's official native demo](https://github.com/appodeal/appodeal-android-sdk/blob/master/native/build.gradle), only in Test Mode. It was a diagnostic build, not the submitted application. Adding the optional adapter did not solve the issue and that dependency was not retained. The submitted SDK stays at 4.4.0.

These results establish the missing response field and strongly suggest a configuration-response/SDK-parser incompatibility rather than an error unique to Task2's registration. They do not establish why Appodeal omitted the field. No SDK binary was patched, response data fabricated, or callback error hidden. No further support contact occurred. Error-free initialization remains unachieved.

A debug-only launch extra (`qa_key`) was added to make initialization reproducible through Android tooling. It is used only for debuggable builds, removed from the launch intent immediately, and never saved into project files. The normal UI still accepts the key at runtime.
