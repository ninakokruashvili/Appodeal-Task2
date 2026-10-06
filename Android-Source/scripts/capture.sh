#!/bin/sh
set -eu
cd "$(dirname "$0")/.."
adb_bin="${ADB:-${ANDROID_HOME:-$HOME/Library/Android/sdk}/platform-tools/adb}"
if command -v adb >/dev/null 2>&1; then adb_bin="$(command -v adb)"; fi
mkdir -p qa/evidence
case "${1:-}" in
 logs) "$adb_bin" logcat -v threadtime 'AppodealQA:I' 'Appodeal:V' 'AndroidRuntime:E' '*:S' | tee qa/evidence/console.log ;;
 screenshot)
  case "${2:-}" in banner|interstitial|rewarded|native|initialization) name="$2";; *) echo 'Choose banner, interstitial, rewarded, native or initialization'; exit 1;; esac
  "$adb_bin" exec-out screencap -p > "qa/evidence/$name.png" ;;
 record) "$adb_bin" shell screenrecord --time-limit 180 /sdcard/task2-qa.mp4 ;;
 pull-recording) "$adb_bin" pull /sdcard/task2-qa.mp4 qa/evidence/task2-qa.mp4 ;;
 *) echo 'Usage: capture.sh logs | screenshot FORMAT | record | pull-recording'; exit 1 ;;
esac
