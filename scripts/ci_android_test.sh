#!/usr/bin/env bash

export ANDROID_EMULATOR_FORCE_32BIT='true'

# update android
echo y | $ANDROID_HOME/tools/bin/sdkmanager --update
echo y | $ANDROID_HOME/tools/bin/sdkmanager --verbose "system-images;android-25;google_apis;x86"

# create avd
$ANDROID_HOME/tools/bin/avdmanager create avd -n test -d pixel -k "system-images;android-25;google_apis;x86" -c 1000M

# run emulator
emulator @Pixel_25

# run android test
# ./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon