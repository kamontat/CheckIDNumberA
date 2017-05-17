#!/usr/bin/env bash

# run emulation
android create avd --name "pixel_25" --path ./.android --device "pixel"


# create pixel device
# $ANDROID_HOME/tools/bin/avdmanager create avd --name "Pixel_25" -d "pixel" -k "system-images;android-25;google_apis;x86"

# run android test
# ./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon