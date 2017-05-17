#!/usr/bin/env bash

# run emulation
$ANDROID_HOME/tools/bin/avdmanager -h
$ANDROID_HOME/tools/bin/avdmanager list
$ANDROID_HOME/tools/bin/avdmanager list avd
$ANDROID_HOME/tools/bin/avdmanager list target
$ANDROID_HOME/tools/bin/avdmanager list device

# run android test
./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon