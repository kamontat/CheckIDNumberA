#!/usr/bin/env bash

# run android test
start_emulator.sh --api=25 --headless
echo "y\n"
./gradlew --no-daemon --stacktrace :app:connectedAndroidTest -PDisableRibbon