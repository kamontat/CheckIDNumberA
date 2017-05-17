#!/usr/bin/env bash

# run unit test
./gradlew --no-daemon --stacktrace testDebugUnitTest :app:testDebugUnitTest -PDisableRibbon
# run android test
start_emulator.sh -h
./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon