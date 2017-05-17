#!/usr/bin/env bash

# run emulation
ls $ANDROID_HOME/tools/

# $ANDROID_HOME/tools/bin/avdmanager -h
# $ANDROID_HOME/tools/bin/avdmanager list
# $ANDROID_HOME/tools/bin/avdmanager list avd
# $ANDROID_HOME/tools/bin/avdmanager list target
# $ANDROID_HOME/tools/bin/avdmanager list device

# create pixel device
# $ANDROID_HOME/tools/bin/avdmanager create avd --name "Pixel_25" -d "pixel" -k "system-images;android-25;google_apis;x86"

# run android test
# ./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon