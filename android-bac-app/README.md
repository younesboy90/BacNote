# BAC Note calculé — Android

This is the Android version of the Moroccan baccalaureate calculator. Its mobile interface is written in Java, and the weighted-average calculations are implemented in C++ using the Android NDK.

## Build the APK

Open this folder in Android Studio and allow it to install the Android SDK platform 35 and NDK 27.2.12479018. Then run the `assembleDebug` Gradle task, or use:

```bat
gradlew.bat assembleDebug
```

The installable APK is created at:

```text
app\build\outputs\apk\debug\app-debug.apk
```

For convenience, the final signed APK is also copied to:

```text
release\BacNoteCalcule-Android.apk
```

This APK is signed with the development key for direct installation; publishing to the Play Store
requires a private release signing key.

The app includes the BAC Note calculé logo, a shortcut to the `r/BAcalculeNote`
Reddit community, and a PayPal donation button linking to
`https://www.paypal.com/paypalme/xyounes9`.
The national exam average is also shown directly below its calculate button.

The interface separates the overall Bac calculator and the regional-subject calculator into tabs.
Use the flag-marked language dropdown to switch between French and Arabic; the Arabic interface
uses right-to-left layout, and the selected language is remembered. Use the
sun/moon control beside the language selector to switch between light and dark
themes; the selected theme and calculator inputs are preserved.
The regional calculator accepts grades for Arabic (coefficient 2), French (4), Islamic
Education (2), and History-Geography (2). It displays the regional weighted average
independently. National-exam grades and continuous-assessment semester grades can each be
calculated independently using only the grades entered in that section. The overall Bac
calculation continues to use 25% continuous assessment, 25% regional exam, and 50% national
exam.

---

© 2026 NeydPeric. All rights reserved.
