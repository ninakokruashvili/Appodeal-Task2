#!/usr/bin/env python3
"""Check captured evidence; never infer a success from a UI action."""
from pathlib import Path
import re
import sys

folder = Path(__file__).resolve().parent.parent / 'qa' / 'evidence'
logfile = folder / 'console.log'
if not logfile.exists():
    sys.exit('No console.log captured; runtime QA remains pending.')
text = logfile.read_text(errors='replace')
initialized = 'CALLBACK onInitializationFinished errors=null' in text
all_pass = initialized
print('Initialization callback without errors:', initialized)
for label, prefix, shot in [('Banner', 'Banner', 'banner'),
                             ('Interstitial', 'Interstitial', 'interstitial'),
                             ('Rewarded', 'RewardedVideo', 'rewarded'),
                             ('Native', 'Native', 'native')]:
    flags = {
        'initialized': f'STATE {label} initialized=true' in text,
        'loaded': f'CALLBACK on{prefix}Loaded' in text,
        'shown': bool(re.search(rf'CALLBACK on{prefix}Shown\b', text)),
        'screenshot': (folder / f'{shot}.png').exists(),
    }
    if label == 'Interstitial':
        flags['closed'] = 'CALLBACK onInterstitialClosed' in text
    if label == 'Rewarded':
        flags['finished'] = 'CALLBACK onRewardedVideoFinished' in text
        flags['closed_after_completion'] = 'CALLBACK onRewardedVideoClosed finished=true' in text
    passed = all(flags.values())
    all_pass &= passed
    print(label, 'EVIDENCE PRESENT' if passed else 'INCOMPLETE', flags)
print('Screenshots still require visual review; callback ordering needs manual review.')
sys.exit(0 if all_pass else 1)
