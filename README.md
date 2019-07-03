# spoofy

## What is it?
A spotify companion app that shows some listening metrics and recommendations.

## Why?
Spoofy was created for some coursework, hence has some 'design decisions' to meet certain course criteria. It's since become a side project as:
* Metrics are interesting to look at. Having a second opinion on who your favourite band is both nice and upsetting at the same time. I have no shame.
* Spotify's discover weekly is often too slow to change and isn't always that good.

## What's next and when?
* The ability to log out (oops)
* ~improved recommendations~
* moving recommendations to an API
* A web interface for the aforementioned API
* A better name/icon and hopefully Play store release

Happening when I have free time. This is a side project and therefore is lower on the priorities now.
The recommendations were intended to be part of another project (making an API for it to call) 
which also may-or-may-not happen as and when I have time.

## How do I run it?
1) clone the repo into Android Studio
2) Register an app with spotify in their developer console. You'll need the client key it gives you
3) find `app/example.keys.properties` and follow the instructions it has
4) Register the app fingerprint in the dev console. There's a `keys.jks` you can use with Java's `keytool` utility, or make your own.
5) compile and build on your device/emulator. Check the build.gradle for minimum api version.
