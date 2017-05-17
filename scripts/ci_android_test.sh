#!/usr/bin/env bash

# create emulation
android create avd --name "Pixel_25" --path ./.android --device "pixel" --target 2

# run emulator
emulator @Pixel_25

# android tool 25.3.0 or later
# $ANDROID_HOME/tools/bin/avdmanager create avd --name "Pixel_25" -d "pixel" -k "system-images;android-25;google_apis;x86"

# run android test
# ./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon