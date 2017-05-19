#!/usr/bin/env bash

# run unit test
echo "--------------------------- Starting Android Test ---------------------------"

./gradlew connectedAndroidTest --profile -PdisablePreDex

echo "--------------------------- Starting Compile APK for Release ---------------------------"

./gradlew assembleRelease --profile -PdisablePreDex