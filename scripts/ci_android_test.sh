#!/usr/bin/env bash

# run android test
start_emulator.sh --api=25 --headless
./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon