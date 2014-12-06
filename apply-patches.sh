#! /bin/bash
patch -p1 -d pd-for-android/ < patches/pd-for-android-gradle.patch
patch -p1 -d pd-for-android/midi/ < patches/android-midi-gradle.patch
