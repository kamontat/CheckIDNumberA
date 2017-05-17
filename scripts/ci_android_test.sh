#!/usr/bin/env bash

export ANDROID_EMULATOR_FORCE_32BIT='true'

# update android
echo y | $ANDROID_HOME/tools/bin/sdkmanager --update

# list
$ANDROID_HOME/tools/bin/avdmanager list

# android tool 25.3.0 or later
$ANDROID_HOME/tools/bin/avdmanager create avd --name "Pixel_25" -d "pixel" -k "system-images;android-25;google_apis;x86"

# run emulator
emulator @Pixel_25

# run android test
# ./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon