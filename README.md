# spoofy

## What is it?
A spotify companion app that shows some listening metrics and related bands.

## Why?
Spoofy was created for some coursework, hence has some 'design decisions' to meet certain criteria and missing features where there was
no incentive to implement

## What's next and when?
* The ability to log out
* improved recommendations

Happening when I have free time. This is a side project and therefore is lower on the priorities now.
The recommendations were intended to be part of another project (making an API for it to call) 
which also may-or-may-not happen as and when I have time.

## How do I run it?
1) clone the repo into Android Studio
2) Register an app with spotify in their developer console. You'll need the client key it gives you
3) find `app/example.keys.properties` and follow the instructions it has
4) Register the app fingerprint in the dev console. This can be found by using Java's `keytool` utility on `keys.jks`
5) compile and build on your device/emulator. Check the build.gradle for minimum api version.
