# TASK 1: 
Hi Team,
Thank you for sharing the information! 
So as I can see, the issue is not that the interstitial ads are forbidden, instead, it’s the timing - what it means, that the ads appear unexpectedly during active user interaction. For example while the user is playing, tapping, or interacting with the app, suddenly ads appear and interrupt the experience, which leads us to accidental clicks. 
To solve this, we need to show interstitials only at natural transition points. For example:
Good placements are:
After a level is completed
After a game-over screen
Between Sections or screens
Before starting a new level
Risky placements are:
Immediately after the user taps a button
In the middle of gameplay
While the user is typing, navigating or interacting in general
At any unpredictable moments without a clear transition. 
I would recommend reviewing all interstitial triggers and moving them to predictable breaks in the user journey. Also, it’s crucial to make sure that the ad is fully loaded before attempting to show it and that the same placements are not triggered too frequently. 
Before submitting the new version to Google Play, please check the following:
No interstitials appear during active user interaction and instead, they are shown at natural transition points as instructed above
Ad frequency is important
The user should understand where the gameplay has ended before the ad appears
Test all of the placements on a real device
Full user flow is reviewed before resubmission. 
Also, would you mind sharing some details with us before proceeding by answering my questions? 
At which events or screens are interstitials currently triggered?
are they triggered automatically or after a specific user action?
Could you share a screen recording showing us the current behavior? 
Thank you in advance! 
Regards,



# TASK 3:
Situation:
SDK initialization succeeds. 
Banner ads load correctly. 
Interstitials never become available. 
Rewarded videos sometimes load but never display.
Logic: So, since the SDK initialization succeeds and Banner ads load correctly, means that there is no issue with SDK integration in general.
What I’d check first:
I would check the flow, where exactly the problem is. In this case, I would check if interstitial is included in the ad types passed during SDK initialization. Then the Ad - if it’s actually being requested/cached. After that, the app checks whether the interstitial is loaded before trying to display it and finally, I would check the test mode - if the issue also happens there. 
For Rewarded Video, since it sometimes loads but never displays, I’d focus more whether the app waits until the ad is loaded and available before calling the show method - as recommended by Appodeal.  
https://docs.appodeal.com/android/ad-types/interstitial?utm_source=chatgpt.com
Which logs I would inspect:
I would enable verbose Appodeal SDK logging and inspect Android Studio Logcat, especially the logs under the Appodeal tag. 
SDK initialization errors/warnings.
interstitial load requests and failures
Rewarded video load events
show failures
Adapter/network errors
Relevant callbacks such as loaded, failed to load, show failed, closed or expired. 
I would first identify if the problem occurs while loading or during display. 
Which SDK settings I would verify
I would verify: 
Correct App Key and Bundle/Application ID
Interstitial and Rewarded Video are included in the initialized ad types
The correct Appodeal SDK version is being used
Required mediation adapters are installed correctly
Test Mode is enabled while reproducing the issue
SDK logging is enabled
Auto-caching settings have not accidentally been disabled for interstitial
Placement configuration - if it’s correct
The app checks ad availability before trying to show the ad
Internet connection is available on the test device.
Which questions I would ask Developer
Which platform and OS are affected?
Which Appodeal SDK version are they using?
If the issue occurs on all devices or specific ones.
Is the result same on Test Mode?
I would ask to provide complete Appodeal SDK logs from initialization till the failed ad attempt with screenshots/screen recording. 
If interstitial and Rewarded Video explicitly included during SDK initialization.
What exactly in the user flow do they try to show on each ad.
If they check whether the ad is loaded before calling the show method.
Which callbacks are being received for interstitial and rewarded video.
Is the caching automatic or manual?
does the issue happen with specific placement or all of them?
I would involve Engineering if I ruled out the integration and configuration issues and provided clear documentation of the issue. If the SDK was correctly initialized with required ad formats, app key and application configuration would be correct, logs show unexpected SDK behavior… I would provide Engineering with SDK version, platform/OS/Device details and all of the information I’ve asked the developer. 
Used the :  Integration Review in Appodeal help center. 

# Task 4. 
Casual game (AOS/iOS) - 120.000 DAU - 70% US,UK,DE
Google AdMob, AppLovin, Unity Ads
How to maximize revenue with Appodeal.
I would use mixed monetization strategy, but priority would be Rewarded Video and Interstitials, but would also would use Banner, Native, MREC and App open carefully. 
Rewarded Video - I think it motivates the user, would be one of the main formats, cause it gives player something valuable for them in exchange for watching an ad, creates situation, where game is not being interrupted, I would do it for extra lives, bonuses, daily reward mutlipliers, boosters etc. 
Interstitial - I would use them but control carefully and only in natural breaks - after level completion, game over or several completed levels without interrupting user experience in the middle of a gameplay. I would probably test approximately 1 impression every 1-2 minutes and check the results. 
Banner - can be used on static screens where they don’t interfere with gameplay - main menu, level selections, result screen etc. 
Native - would use less, for example content areas, shop, level selections etc. 
App Open - I would test carefully while launching for example, but I would avoid showing it all the time not to be aggressive.
MREC - larger format, would use on screens with enough available space, may be pause menu, game over, shop etc. 
Recommended placements:
App launch - App open, but only after testing. 
Main menu - Banner or MREC - ad can remain visible without interrupting the user experience.
Level completion - Interstitial or Rewarded Video - would be the best natural break.
Game over - Rewarded Video - top priority - watch an add to gain more lives/continue…
Daily rewards - Rewarded Video - player could watch an ad to multiply the daily reward. 
Shop - Native or MREC - the screen is already focused on content and it doesn’t require user’s fast interaction. Also rewarded video  for getting some extra coins and use them in shop. 
Pause Menu - MREC or Banner - player has already paused the gameplay, display ad would be less disruptive.
Waterfall - since publisher already works with Admob, Applovin and Unity Ads, I would keep these sources in mediation setup and add additional demand to increase competition for each impression. I would test - BidMachine, Mintegral, InMobi, Liftoff etc.
 Since US represents 45 % of DAU, I would pay attention to monetization performance there, also would analyze the UK, Germany and other 30% of GEOs separately. After launch, I would monitor each network and format by GEO using: Revenue, eCPM, Fill Rate, Impressions, Requests, Display Rate, ARPDAU, Impressions per user. I would also check networks that add genuine incremental revenue rather than shifting impressions away from another demand source. I would optimize or remove consistently underperforming demand sources and continue testing new configurations. 
 Success Metrics:
Total revenue - to check if monetization is improving
ARPDAU - revenue generated per active user
eCPM - ad value
 Fill Rate - how many requests receive ads
Impressions per user - to avoid overloading users
D1/D7 retention - to make sure monetization is not hurting user experience. 
My main focus would be ARPDAU + total revenue + retention, not only eCPM. 

# Task 5
Hello,
A 20% increase in eCPM does not always mean total revenue will increase by same amount. It also depends on some other factors, for example if eCPM increased but the number of impressions decreased, total revenue may stay almost the same. 
Also, I would monitor: Total impressions, Fill Rate, Dau, ARPDAU, Impressions per user, Performance by GEO and ad format. 
For Optimization, I would first identify where impression volume or fill may have dropped, then compare performance by GEO, placement, ad format, and demand source.
I would also review ad frequency and placements to find opportunities to increase monetization without negatively affecting the user experience. 



Used sources: 

https://docs.appodeal.com/
https://docs.appodeal.com/android/advanced/testing
https://docs.appodeal.com/android/ad-types/interstitial

# Task 2 — Android SDK Integration & QA

Start with [the submission guide](START-HERE.md).

- [Build and testing instructions, and known issues](Android-Source/README.md)
- [Installable Android APK](APK/AppodealQA.apk)
- [Full QA report and implementation/troubleshooting notes](QA/REPORT.md)
- [Screenshots for all four formats and console/callback logs](QA/evidence/README.md)
- Android source project: `Android-Source/`

All four Test Mode ad formats loaded and displayed. Error-free SDK initialization remains unresolved and is documented in the report. The Appodeal key is entered at runtime and is not included. 
