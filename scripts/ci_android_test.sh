#!/usr/bin/env bash

# run android test
start_emulator.sh --api=23 --headless
./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon