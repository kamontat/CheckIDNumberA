#!/usr/bin/env bash

export ANDROID_EMULATOR_FORCE_32BIT='true'

# run emulator
emulator -avd "Pixel_25"

# run android test
./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon