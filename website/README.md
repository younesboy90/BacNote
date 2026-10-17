# BAC Note calculé — Website

The website is a responsive, dependency-free version of the Android calculator.
It includes French and Arabic, light and dark themes, all four study tracks,
the national/regional/continuous averages, and the animated final-grade result.
Preferences and entered grades are saved in the browser.

Open `index.html` in a modern browser to preview it, or serve the `website`
folder with any static web server and publish its contents to a static host.
The `assets` folder contains the app logo and Reddit icon.

The same calculator is bundled into the [iPhone app](../ios-app/README.md),
which can be built on a hosted Mac and sideloaded for personal use.

## Calculation rules

- National average: weighted average of the entered national subjects for the
  selected track.
- Regional average: weighted average using the regional subject coefficients.
- Continuous assessment: average of the semester 1 and semester 2 grades.
- Final average: 25% continuous assessment, 25% regional exam, and 50% national
  exam.

Grades are validated from 0 to 20. National-only calculations accept any
non-empty subset of national subjects; the regional and overall calculations
require all their grades.
