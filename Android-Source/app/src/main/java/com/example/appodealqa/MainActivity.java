package com.example.appodealqa;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import com.appodeal.ads.*;
import com.appodeal.ads.nativead.NativeAdViewNewsFeed;
import com.appodeal.ads.utils.Log.LogLevel;
import java.util.List;

/** Test-only integration. SDK callbacks are the source of truth for QA. */
public final class MainActivity extends Activity {
    private LinearLayout content;
    private TextView events;
    private EditText key;
    private NativeAdViewNewsFeed nativeView;
    private boolean started;
    private double rewardTotal;
    private final int[] types = {Appodeal.BANNER, Appodeal.INTERSTITIAL, Appodeal.REWARDED_VIDEO, Appodeal.NATIVE};
    private final String[] names = {"Banner", "Interstitial", "Rewarded", "Native"};

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 30, 20, 20);
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            v.setPadding(20, insets.getSystemWindowInsetTop() + 12, 20, insets.getSystemWindowInsetBottom() + 12);
            return insets;
        });
        TextView heading = new TextView(this);
        heading.setText("Appodeal QA · TEST MODE\n" + getPackageName());
        heading.setTextSize(22);
        root.addView(heading);
        key = new EditText(this);
        key.setSingleLine(true);
        key.setHint("Paste Appodeal app key");
        key.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        root.addView(key);
        Button initialize = new Button(this);
        initialize.setText("Initialize SDK");
        root.addView(initialize);
        ScrollView scroll = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        for (int i = 0; i < types.length; i++) {
            final int type = types[i]; final String name = names[i];
            LinearLayout row = new LinearLayout(this);
            button(row, "Load " + name, () -> load(type, name));
            button(row, "Show " + name, () -> show(type, name));
            content.addView(row);
        }
        button(content, "Hide banner", () -> { Appodeal.hide(this, Appodeal.BANNER); event("ACTION banner hidden"); });
        nativeView = new NativeAdViewNewsFeed(this);
        content.addView(nativeView, new LinearLayout.LayoutParams(-1, -2));
        events = new TextView(this);
        events.setTextSize(12);
        events.setTextIsSelectable(true);
        content.addView(events);
        // Dedicated banner space prevents ads covering controls or callback text.
        root.addView(Appodeal.getBannerView(this), new LinearLayout.LayoutParams(-1, -2));
        setContentView(root);
        initialize.setOnClickListener(v -> {
            if (started) { event("ACTION initialization already requested"); return; }
            String appKey = key.getText().toString().trim();
            if (appKey.isEmpty()) { key.setError("Appodeal app key required"); return; }
            started = true; key.setEnabled(false); initialize.setEnabled(false);
            registerCallbacks();
            Appodeal.setTesting(true);
            Appodeal.setLogLevel(LogLevel.verbose);
            for (int type : types) Appodeal.setAutoCache(type, false);
            event("ACTION initialize testMode=true formats=Banner,Interstitial,Rewarded,Native");
            Appodeal.initialize(this, appKey, Appodeal.BANNER | Appodeal.INTERSTITIAL | Appodeal.REWARDED_VIDEO | Appodeal.NATIVE, errors -> {
                event("CALLBACK onInitializationFinished errors=" + errors);
                for (int i = 0; i < types.length; i++)
                    event("STATE " + names[i] + " initialized=" + Appodeal.isInitialized(types[i]));
            });
        });
        event("SESSION ready; no ad requests until Initialize SDK");
        // Debug-only QA entry point; the supplied key stays in memory.
        if ((getApplicationInfo().flags & android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            String runtimeKey = getIntent().getStringExtra("qa_key");
            if (runtimeKey != null && !runtimeKey.trim().isEmpty()) {
                key.setText(runtimeKey);
                getIntent().removeExtra("qa_key");
                initialize.performClick();
            }
        }
    }
    private void button(LinearLayout parent, String text, Runnable action) {
        Button button = new Button(this); button.setText(text);
        button.setOnClickListener(v -> action.run());
        parent.addView(button, new LinearLayout.LayoutParams(parent == content ? -1 : 0, -2, parent == content ? 0 : 1));
    }
    private boolean ready(int type, String name) {
        if (!started || !Appodeal.isInitialized(type)) {
            event("ACTION " + name + " blocked: SDK format not initialized"); return false;
        }
        return true;
    }
    private void load(int type, String name) {
        if (!ready(type, name)) return;
        event("ACTION cache " + name); Appodeal.cache(this, type);
    }
    private void show(int type, String name) {
        if (!ready(type, name)) return;
        if (!Appodeal.isLoaded(type)) { event("ACTION " + name + " blocked: no loaded ad"); return; }
        if (type == Appodeal.NATIVE) {
            List<NativeAd> ads = Appodeal.getNativeAds(1);
            if (ads.isEmpty()) { event("ACTION native cache empty"); return; }
            nativeView.registerView(ads.get(0));
            event("ACTION native registered; wait for onNativeShown");
        } else {
            boolean accepted = Appodeal.show(this, type == Appodeal.BANNER ? Appodeal.BANNER_VIEW : type);
            event("ACTION show " + name + " accepted=" + accepted + "; wait for Shown callback");
        }
    }
    private void event(String message) {
        String line = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", java.util.Locale.US).format(new java.util.Date()) + " " + message;
        Log.i("AppodealQA", line);
        runOnUiThread(() -> {
            if (events != null) {
                String text = events.getText().toString();
                events.setText(line + "\n" + text.substring(0, Math.min(text.length(), 12000)));
            }
        });
    }
    private void registerCallbacks() {
        Appodeal.setBannerCallbacks(new BannerCallbacks() {
            @Override public void onBannerLoaded(int height, boolean isPrecache) { event("CALLBACK onBannerLoaded " + "height=" + height + " isPrecache=" + isPrecache); }
            @Override public void onBannerFailedToLoad() { event("CALLBACK onBannerFailedToLoad " + ""); }
            @Override public void onBannerShown() { event("CALLBACK onBannerShown " + ""); }
            @Override public void onBannerShowFailed() { event("CALLBACK onBannerShowFailed " + ""); }
            @Override public void onBannerClicked() { event("CALLBACK onBannerClicked " + ""); }
            @Override public void onBannerExpired() { event("CALLBACK onBannerExpired " + ""); }
        });
        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override public void onInterstitialLoaded(boolean isPrecache) { event("CALLBACK onInterstitialLoaded " + "isPrecache=" + isPrecache); }
            @Override public void onInterstitialFailedToLoad() { event("CALLBACK onInterstitialFailedToLoad " + ""); }
            @Override public void onInterstitialShown() { event("CALLBACK onInterstitialShown " + ""); }
            @Override public void onInterstitialShowFailed() { event("CALLBACK onInterstitialShowFailed " + ""); }
            @Override public void onInterstitialClicked() { event("CALLBACK onInterstitialClicked " + ""); }
            @Override public void onInterstitialClosed() { event("CALLBACK onInterstitialClosed " + ""); }
            @Override public void onInterstitialExpired() { event("CALLBACK onInterstitialExpired " + ""); }
        });
        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            @Override public void onRewardedVideoLoaded(boolean isPrecache) { event("CALLBACK onRewardedVideoLoaded " + "isPrecache=" + isPrecache); }
            @Override public void onRewardedVideoFailedToLoad() { event("CALLBACK onRewardedVideoFailedToLoad " + ""); }
            @Override public void onRewardedVideoShown() { event("CALLBACK onRewardedVideoShown " + ""); }
            @Override public void onRewardedVideoShowFailed() { event("CALLBACK onRewardedVideoShowFailed " + ""); }
            @Override public void onRewardedVideoClicked() { event("CALLBACK onRewardedVideoClicked " + ""); }
            @Override public void onRewardedVideoFinished(double amount, String currency) { event("CALLBACK onRewardedVideoFinished " + "amount=" + amount + " currency=" + currency); rewardTotal += amount; event("STATE rewardTotal=" + rewardTotal); }
            @Override public void onRewardedVideoClosed(boolean finished) { event("CALLBACK onRewardedVideoClosed " + "finished=" + finished); }
            @Override public void onRewardedVideoExpired() { event("CALLBACK onRewardedVideoExpired " + ""); }
        });
        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override public void onNativeLoaded() { event("CALLBACK onNativeLoaded " + ""); }
            @Override public void onNativeFailedToLoad() { event("CALLBACK onNativeFailedToLoad " + ""); }
            @Override public void onNativeShown(NativeAd ad) { event("CALLBACK onNativeShown " + "ad=" + ad); }
            @Override public void onNativeShowFailed(NativeAd ad) { event("CALLBACK onNativeShowFailed " + "ad=" + ad); }
            @Override public void onNativeClicked(NativeAd ad) { event("CALLBACK onNativeClicked " + "ad=" + ad); }
            @Override public void onNativeExpired() { event("CALLBACK onNativeExpired " + ""); }
        });
    }
    @Override protected void onDestroy() {
        if (nativeView != null) nativeView.destroy();
        Appodeal.hide(this, Appodeal.BANNER);
        super.onDestroy();
    }
}
