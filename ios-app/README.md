# iPhone app (free personal sideload)

This iOS app bundles the same responsive calculator website used by the Android-matched web version, including its logo as the app icon. The site files are copied into the app during the cloud build, so the calculator works offline. Reddit and PayPal links open in Safari.

## Build the IPA without a Mac

The Xcode build runs on GitHub's hosted macOS runner. This workflow deliberately creates an **unsigned** IPA, so it does not need your Apple ID or signing secrets:

1. Put this project in a GitHub repository and make sure `.github/workflows/build-ios-unsigned.yml` is included.
2. Open the repository's **Actions** tab, select **Build iOS IPA**, and choose **Run workflow**.
3. When the run completes, download the `BACNoteCalcule-unsigned-IPA` artifact and extract it to get `BACNoteCalcule-unsigned.ipa`.

This workspace is not connected to a GitHub repository, so the workflow cannot be started from here. The source and workflow need to be uploaded to GitHub before its hosted Mac can build the IPA.

## Install it with a free Apple ID

Use [AltStore](https://altstore.io/) and AltServer on Windows to sign and install the downloaded IPA with your Apple ID. Follow AltStore's current Windows setup instructions, install AltStore on the iPhone, then use **My Apps → +** in AltStore to select the IPA.

Free Apple ID signing is for personal testing, not App Store distribution. The app's signing expires after about seven days, so refresh it from AltStore regularly while the iPhone and AltServer are available. Apple's free signing also limits how many apps can be active on a device.

No Apple ID password, signing certificate, or provisioning profile is stored in this project or sent to the GitHub build.
