# Captured evidence

Final app: Appodeal 4.4.0, Android API 37 emulator, package com.task2.appodealqa, 7 October 2026. All four screenshots were captured from real SDK Test Mode inventory and visually reviewed.

- [Banner](banner.png)
- [Interstitial](interstitial.png)
- [Rewarded video](rewarded.png)
- [Native](native.png)
- [Verbose SDK console](console.log)
- [Application callback console](callbacks.log)
- [Final Android lint report](lint-report.html)

The final console is filtered to the final app process; the app key is redacted. The earlier initialization failure is preserved in initialization-error.log and initialization-error.png. build.log preserves the earlier successful build output; see ../BUILD-STATUS.md for final build details.

**Initialization is not error-free:** the callback returns SdkConfigurationError although each isInitialized flag is true and test ads load/display. No successful initialization log is invented. No recording was needed because screenshots cover every format.
