# Appodeal configuration issue — reproduction details

Package: com.task2.appodealqa. The user has confirmed the dashboard Bundle ID and app key match the running application. Supply the key privately if Appodeal support requests it; it is redacted from the attached logs.

Environment: Android API 37 Google Play emulator on macOS; Appodeal core 4.4.0 (also reproduced on 4.3.0), IAB 1.8.1.0, BidMachine 3.7.1.0 and AdMob 25.2.0.0. Build and Android lint pass.

Reproduction: setTesting(true), setLogLevel(verbose), disable auto-cache for banner/interstitial/rewarded/native, register callbacks, initialize with all four formats. The initialization callback returns InternalError.SdkConfigurationError. Verbose logs contain IllegalArgumentException: Required value was null in com.appodeal.ads.networking.usecases.b.a. All four isInitialized flags are true.

Subsequent manual caching successfully loads Appodeal Test Mode MRAID/VAST/native creatives. All four formats display and report Shown. Rewarded completion reports Finished(amount=0.0,currency="") and Closed(finished=true).

Question for support: why is the SDK configuration/services response absent or incomplete for this newly registered application, and what account configuration or SDK handling is required for an error-free initialization? The missing services object is an inference from the SDK parser, not a confirmed server diagnosis.

Evidence: console.log, callbacks.log, initialization-error.log and screenshots under qa/evidence.

Appodeal SDK Issue ticket #142311431 was submitted on 7 October 2026 through the authenticated dashboard support panel. Submission was confirmed by the visible “Ticket is created” message. The ticket contains this reproduction information, initialization error, and stacktrace excerpt. No app key was entered into the ticket; the Bundle ID was used instead. Files were not uploaded. Appodeal will notify the account by email when the ticket is updated.
