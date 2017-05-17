#!/usr/bin/env bash

export ANDROID_EMULATOR_FORCE_32BIT='true'

# list abi
android update sdk --no-ui --all --filter 1

# create emulation
android create avd --name "Pixel_25" --device "pixel" --target 2 --abi "google_apis/x86"

# run emulator
emulator @Pixel_25

# android tool 25.3.0 or later
# $ANDROID_HOME/tools/bin/avdmanager create avd --name "Pixel_25" -d "pixel" -k "system-images;android-25;google_apis;x86"

# run android test
# ./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon