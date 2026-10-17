const FLAGS = {
  fr: '<svg viewBox="0 0 24 16" aria-hidden="true"><path fill="#002395" d="M0 0h8v16H0z"/><path fill="#fff" d="M8 0h8v16H8z"/><path fill="#ed2939" d="M16 0h8v16h-8z"/></svg>',
  ar: '<svg viewBox="0 0 24 16" aria-hidden="true"><path fill="#c1272d" d="M0 0h24v16H0z"/><path fill="none" stroke="#006233" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.1" d="m12 2 3.35 10.2L3.2 4.65h17.6L8.65 12.2z"/></svg>'
};
const REGIONAL_COEFFICIENTS = [2, 4, 2, 2];
const REGIONAL_SUBJECTS = {
  fr: ["Arabe", "Français", "Éducation islamique", "Histoire-Géographie"],
  ar: ["اللغة العربية", "اللغة الفرنسية", "التربية الإسلامية", "التاريخ والجغرافيا"]
};
const TRACKS = [
  { fr: "2 Bac Sciences Physiques (PC)", ar: "الثانية باكالوريا علوم فيزيائية (PC)", subjects: ["Mathématiques", "Physique-Chimie", "Sciences de la Vie et de la Terre", "Anglais", "Philosophie"], subjectsAr: ["الرياضيات", "الفيزياء والكيمياء", "علوم الحياة والأرض", "الإنجليزية", "الفلسفة"], coefficients: [7, 7, 3, 2, 2] },
  { fr: "2 Bac Sciences de la Vie et de la Terre (SVT)", ar: "الثانية باكالوريا علوم الحياة والأرض (SVT)", subjects: ["Mathématiques", "Physique-Chimie", "Sciences de la Vie et de la Terre", "Anglais", "Philosophie"], subjectsAr: ["الرياضيات", "الفيزياء والكيمياء", "علوم الحياة والأرض", "الإنجليزية", "الفلسفة"], coefficients: [5, 5, 7, 2, 2] },
  { fr: "2 Bac Sciences Mathématiques A", ar: "الثانية باكالوريا علوم رياضية أ", subjects: ["Mathématiques", "Physique-Chimie", "Sciences de la Vie et de la Terre", "Anglais", "Philosophie"], subjectsAr: ["الرياضيات", "الفيزياء والكيمياء", "علوم الحياة والأرض", "الإنجليزية", "الفلسفة"], coefficients: [9, 7, 3, 2, 2] },
  { fr: "2 Bac Sciences Mathématiques B", ar: "الثانية باكالوريا علوم رياضية ب", subjects: ["Mathématiques", "Physique-Chimie", "Sciences de l'ingénieur", "Anglais", "Philosophie"], subjectsAr: ["الرياضيات", "الفيزياء والكيمياء", "علوم المهندس", "الإنجليزية", "الفلسفة"], coefficients: [9, 7, 3, 2, 2] }
];

const TEXT = {
  fr: {
    title: "BAC Note calculé", subtitle: "Calculatrice du baccalauréat marocain", donation: "Donation",
    nationalTab: "Calcul Bac", regionalTab: "Examen régional", track: "VOTRE FILIÈRE",
    nationalExam: "EXAMEN NATIONAL", regionalExam: "EXAMEN RÉGIONAL — CALCUL PAR MATIÈRE",
    continuous: "CONTRÔLE CONTINU", continuousHint: "Moyenne des deux semestres.",
    semesterOne: "Contrôle continu — semestre 1", semesterTwo: "Contrôle continu — semestre 2",
    calculateNational: "Calculer la moyenne nationale", calculateRegional: "Calculer la moyenne régionale",
    calculateContinuous: "Calculer la moyenne du contrôle continu", calculateGeneral: "Calculer la moyenne générale",
    nationalScore: "MOYENNE NATIONALE", regionalScore: "MOYENNE RÉGIONALE", generalScore: "MOYENNE GÉNÉRALE",
    resultPrompt: "Entrez vos notes pour calculer votre moyenne",
    nationalAverage: "Moyenne nationale : ", regionalAverage: "Moyenne régionale : ",
    continuousAverage: "Moyenne du contrôle continu : ",
    formula: "25 % contrôle continu + 25 % régional + 50 % national",
    regionalHint: "Saisissez vos notes sur 20.",
    regionalWeights: "Coefficients régionaux : Arabe 2 · Français 4 · Éducation islamique 2 · Histoire-Géographie 2",
    footer: "© 2026 NeydPeric. All rights reserved.", dialogTitle: "Votre moyenne générale",
    invalid: (subject) => `Saisissez une note entre 0 et 20 pour « ${subject} ».`,
    missingNational: "Saisissez au moins une note de l'examen national.",
    missingContinuous: "Saisissez au moins une note de contrôle continu.",
    missingRegional: "Saisissez toutes les notes de l'examen régional.",
    invalidTitle: "Note invalide", missingTitle: "Notes manquantes", close: "Fermer",
    notAdmitted: "Non admis", passable: "Passable", fairlyGood: "Assez bien", good: "Bien", veryGood: "Très bien", excellent: "Excellent",
    bravo: "Bravo !", persist: "À persévérer",
    passedMessage: "Vous avez une belle moyenne au baccalauréat.",
    failedMessage: "Continue tes efforts, tu peux progresser!",
    admitted: "ADMIS — Félicitations !", notAdmittedStatus: "NON ADMIS — Encore un effort",
    nationalCalculated: "Moyenne nationale calculée", continuousCalculated: "Moyenne du contrôle continu calculée",
    languageDescription: "Changer de langue", themeLight: "Passer au thème clair", themeDark: "Passer au thème sombre"
  },
  ar: {
    title: "حاسبة معدل البكالوريا", subtitle: "حاسبة معدل البكالوريا المغربية", donation: "Donation",
    nationalTab: "حساب البكالوريا", regionalTab: "الامتحان الجهوي", track: "الشعبة",
    nationalExam: "الامتحان الوطني", regionalExam: "الامتحان الجهوي — الحساب حسب المادة",
    continuous: "المراقبة المستمرة", continuousHint: "معدل الدورتين.",
    semesterOne: "المراقبة المستمرة — الدورة الأولى", semesterTwo: "المراقبة المستمرة — الدورة الثانية",
    calculateNational: "احسب المعدل الوطني", calculateRegional: "احسب المعدل الجهوي",
    calculateContinuous: "احسب معدل المراقبة المستمرة", calculateGeneral: "احسب المعدل العام",
    nationalScore: "المعدل الوطني", regionalScore: "المعدل الجهوي", generalScore: "المعدل العام",
    resultPrompt: "أدخل النقط لحساب المعدل",
    nationalAverage: "المعدل الوطني: ", regionalAverage: "المعدل الجهوي: ",
    continuousAverage: "معدل المراقبة المستمرة: ",
    formula: "25٪ المراقبة المستمرة + 25٪ الجهوي + 50٪ الوطني",
    regionalHint: "أدخل نقطك من 20.",
    regionalWeights: "معاملات الجهوي: العربية 2 · الفرنسية 4 · التربية الإسلامية 2 · التاريخ والجغرافيا 2",
    footer: "© 2026 NeydPeric. جميع الحقوق محفوظة.", dialogTitle: "معدلك العام",
    invalid: (subject) => `أدخل نقطة بين 0 و20 في مادة « ${subject} ».`,
    missingNational: "أدخل نقطة واحدة على الأقل في الامتحان الوطني.",
    missingContinuous: "أدخل نقطة واحدة على الأقل في المراقبة المستمرة.",
    missingRegional: "أدخل جميع نقط الامتحان الجهوي.",
    invalidTitle: "نقطة غير صالحة", missingTitle: "نقط ناقصة", close: "إغلاق",
    notAdmitted: "غير ناجح", passable: "مقبول", fairlyGood: "مستحسن", good: "جيد", veryGood: "جيد جدا", excellent: "ممتاز",
    bravo: "أحسنت!", persist: "واصل المحاولة",
    passedMessage: "لقد حصلت على معدل جيد في البكالوريا.",
    failedMessage: "واصل اجتهادك، يمكنك تحقيق نتيجة أفضل!",
    admitted: "ناجح — تهانينا!", notAdmittedStatus: "غير ناجح — حظاً موفقاً",
    nationalCalculated: "تم حساب المعدل الوطني", continuousCalculated: "تم حساب معدل المراقبة المستمرة",
    languageDescription: "تغيير اللغة", themeLight: "التبديل إلى المظهر الفاتح", themeDark: "التبديل إلى المظهر الداكن"
  }
};

const $ = (selector) => document.querySelector(selector);
const gradeState = { national: [], regional: [], semesterOne: "", semesterTwo: "" };
let language = localStorage.getItem("bac-language") === "ar" ? "ar" : "fr";
let theme = localStorage.getItem("bac-theme") === "dark" ? "dark" : "light";
let toastTimer;

function tr(key) {
  return TEXT[language][key];
}

function setTheme(nextTheme) {
  theme = nextTheme;
  document.documentElement.dataset.theme = theme;
  document.querySelector('meta[name="theme-color"]').content = theme === "dark" ? "#111820" : "#f7f8fa";
  $("#theme-icon").textContent = theme === "dark" ? "☼" : "☾";
  $("#theme-button").setAttribute("aria-label", tr(theme === "dark" ? "themeLight" : "themeDark"));
  localStorage.setItem("bac-theme", theme);
}

function persistGrades() {
  gradeState.national = [...document.querySelectorAll("#national-grades input")].map((input) => input.value);
  gradeState.regional = [...document.querySelectorAll("#regional-grades input")].map((input) => input.value);
  gradeState.semesterOne = $("#semester-one").value;
  gradeState.semesterTwo = $("#semester-two").value;
  localStorage.setItem("bac-grades", JSON.stringify(gradeState));
  localStorage.setItem("bac-track", $("#track-select").value);
}

function restoreGrades() {
  try {
    const saved = JSON.parse(localStorage.getItem("bac-grades") || "{}");
    if (Array.isArray(saved.national)) gradeState.national = saved.national;
    if (Array.isArray(saved.regional)) gradeState.regional = saved.regional;
    if (typeof saved.semesterOne === "string") gradeState.semesterOne = saved.semesterOne;
    if (typeof saved.semesterTwo === "string") gradeState.semesterTwo = saved.semesterTwo;
  } catch {
    localStorage.removeItem("bac-grades");
  }
}

function inputGrade(value = "", label, id) {
  const input = document.createElement("input");
  input.className = "grade-input";
  input.type = "text";
  input.inputMode = "decimal";
  input.autocomplete = "off";
  input.placeholder = "0 – 20";
  input.value = value;
  input.id = id;
  input.setAttribute("aria-label", label);
  input.addEventListener("input", () => {
    input.removeAttribute("aria-invalid");
    persistGrades();
  });
  return input;
}

function addGradeRow(parent, { label, value = "", coefficient, id }) {
  const row = document.createElement("label");
  row.className = `grade-row${coefficient === undefined ? "" : " has-coefficient"}`;
  row.htmlFor = id;
  const caption = document.createElement("span");
  caption.textContent = label;
  row.append(caption);
  const input = inputGrade(value, label, id);
  if (coefficient !== undefined) {
    const chip = document.createElement("span");
    chip.className = "grade-coefficient";
    chip.textContent = `×${coefficient}`;
    chip.setAttribute("aria-label", language === "fr" ? `coefficient ${coefficient}` : `المعامل ${coefficient}`);
    row.append(chip, input);
  } else {
    row.append(input);
  }
  parent.append(row);
  return input;
}

function renderTrackOptions() {
  const select = $("#track-select");
  const selected = Number(select.value || localStorage.getItem("bac-track") || 0);
  select.replaceChildren(...TRACKS.map((track, index) => {
    const option = document.createElement("option");
    option.value = String(index);
    option.textContent = track[language];
    return option;
  }));
  select.value = String(Number.isInteger(selected) && selected >= 0 && selected < TRACKS.length ? selected : 0);
}

function renderGradeRows() {
  const track = TRACKS[Number($("#track-select").value)];
  const national = $("#national-grades");
  const regional = $("#regional-grades");
  national.replaceChildren();
  regional.replaceChildren();
  const savedNational = gradeState.national;
  track.subjects.forEach((subject, index) => addGradeRow(national, {
    label: language === "ar" ? track.subjectsAr[index] : subject,
    value: savedNational[index] || "",
    coefficient: track.coefficients[index],
    id: `national-grade-${index}`
  }));
  REGIONAL_SUBJECTS[language].forEach((subject, index) => addGradeRow(regional, {
    label: subject,
    value: gradeState.regional[index] || "",
    coefficient: REGIONAL_COEFFICIENTS[index],
    id: `regional-grade-${index}`
  }));
  $("#semester-one").value = gradeState.semesterOne;
  $("#semester-two").value = gradeState.semesterTwo;
  persistGrades();
}

function updateText() {
  document.documentElement.lang = language;
  document.documentElement.dir = language === "ar" ? "rtl" : "ltr";
  $(".brand").setAttribute("aria-label", language === "fr"
    ? "BAC Note calculé — accueil" : "حاسبة معدل البكالوريا — الصفحة الرئيسية");
  $(".page-tabs").setAttribute("aria-label", language === "fr" ? "Calculatrice" : "حاسبة معدل البكالوريا");
  document.querySelectorAll("[data-i18n]").forEach((node) => {
    node.textContent = tr(node.dataset.i18n);
  });
  $("#current-language").textContent = language.toUpperCase();
  $("#current-flag").innerHTML = FLAGS[language];
  document.querySelectorAll("[data-flag]").forEach((flag) => {
    flag.innerHTML = FLAGS[flag.dataset.flag];
  });
  $("#language-button").setAttribute("aria-label", tr("languageDescription"));
  $("#language-menu button[data-language='fr']").setAttribute("aria-checked", String(language === "fr"));
  $("#language-menu button[data-language='ar']").setAttribute("aria-checked", String(language === "ar"));
  $("#dialog-close").setAttribute("aria-label", tr("close"));
  setTheme(theme);
  renderTrackOptions();
  renderGradeRows();
  resetResultDisplay();
}

function resetResultDisplay() {
  $("#national-average").textContent = `${tr("nationalAverage")}-- / 20`;
  $("#continuous-average").textContent = `${tr("continuousAverage")}-- / 20`;
  $("#regional-average").textContent = `${tr("regionalAverage")}-- / 20`;
  $("#national-score").textContent = "-- / 20";
  $("#regional-score").textContent = "-- / 20";
  $("#general-score").textContent = "-- / 20";
  setResultStatus(tr("resultPrompt"));
}

function setLanguage(nextLanguage) {
  if (language === nextLanguage) return;
  persistGrades();
  language = nextLanguage;
  localStorage.setItem("bac-language", language);
  updateText();
  $("#language-menu").hidden = true;
  $("#language-button").setAttribute("aria-expanded", "false");
}

function readGrade(input, label) {
  const value = input.value.trim().replace(",", ".");
  if (!/^(?:\d+(?:\.\d*)?|\.\d+)$/.test(value)) {
    if (value === "") return null;
    input.setAttribute("aria-invalid", "true");
    input.focus();
    showToast(tr("invalid")(label));
    return NaN;
  }
  const grade = Number(value);
  if (!Number.isFinite(grade) || grade < 0 || grade > 20) {
    input.setAttribute("aria-invalid", "true");
    input.focus();
    showToast(tr("invalid")(label));
    return NaN;
  }
  input.removeAttribute("aria-invalid");
  return grade;
}

function weightedAverage(grades, coefficients) {
  const coefficientTotal = coefficients.reduce((sum, coefficient) => sum + coefficient, 0);
  return grades.reduce((sum, grade, index) => sum + grade * coefficients[index], 0) / coefficientTotal;
}

function formatAverage(value) {
  return `${value.toFixed(2)} / 20`;
}

function setResultStatus(message, passed = null) {
  $("#result-status").textContent = message;
  $("#result-status").style.backgroundColor = passed === null ? "var(--surface)" : passed ? "var(--green-soft)" : "var(--coral-soft)";
  $("#result-status").style.color = passed === null ? "var(--teal)" : passed ? "var(--teal)" : "var(--coral)";
}

function calculateNational() {
  const track = TRACKS[Number($("#track-select").value)];
  const grades = [];
  const coefficients = [];
  const rows = [...document.querySelectorAll("#national-grades .grade-row")];
  rows.forEach((row, index) => {
    const input = row.querySelector("input");
    if (!input.value.trim()) return;
    const grade = readGrade(input, language === "ar" ? track.subjectsAr[index] : track.subjects[index]);
    if (Number.isNaN(grade)) return;
    grades.push(grade);
    coefficients.push(track.coefficients[index]);
  });
  if (rows.some((row) => row.querySelector("input").getAttribute("aria-invalid") === "true")) return;
  if (!grades.length) return showToast(tr("missingNational"), tr("missingTitle"));
  const average = weightedAverage(grades, coefficients);
  $("#national-average").textContent = `${tr("nationalAverage")}${formatAverage(average)}`;
  $("#national-score").textContent = formatAverage(average);
  setResultStatus(tr("nationalCalculated"));
  return average;
}

function calculateContinuous() {
  const inputs = [$("#semester-one"), $("#semester-two")];
  const labels = [tr("semesterOne"), tr("semesterTwo")];
  const values = [];
  inputs.forEach((input, index) => {
    if (!input.value.trim()) return;
    const value = readGrade(input, labels[index]);
    if (!Number.isNaN(value) && value !== null) values.push(value);
  });
  if (inputs.some((input) => input.getAttribute("aria-invalid") === "true")) return;
  if (!values.length) return showToast(tr("missingContinuous"), tr("missingTitle"));
  const average = values.reduce((sum, value) => sum + value, 0) / values.length;
  $("#continuous-average").textContent = `${tr("continuousAverage")}${formatAverage(average)}`;
  setResultStatus(tr("continuousCalculated"));
  return average;
}

function calculateRegional() {
  const inputs = [...document.querySelectorAll("#regional-grades input")];
  const grades = [];
  let missing = false;
  for (const [index, input] of inputs.entries()) {
    if (!input.value.trim()) {
      missing = true;
      continue;
    }
    const value = readGrade(input, REGIONAL_SUBJECTS[language][index]);
    if (Number.isNaN(value)) return null;
    grades.push(value);
  }
  if (missing) {
    showToast(tr("missingRegional"), tr("missingTitle"));
    const firstEmpty = inputs.find((input) => !input.value.trim());
    firstEmpty?.focus();
    return null;
  }
  const average = weightedAverage(grades, REGIONAL_COEFFICIENTS);
  $("#regional-average").textContent = `${tr("regionalAverage")}${formatAverage(average)}`;
  $("#regional-score").textContent = formatAverage(average);
  return average;
}

function calculateGeneral() {
  const nationalInputs = [...document.querySelectorAll("#national-grades input")];
  if (nationalInputs.some((input) => !input.value.trim())) {
    const firstEmpty = nationalInputs.find((input) => !input.value.trim());
    firstEmpty.focus();
    return showToast(tr("missingNational"), tr("missingTitle"));
  }
  const national = [];
  const track = TRACKS[Number($("#track-select").value)];
  for (const [index, input] of nationalInputs.entries()) {
    const value = readGrade(input, language === "ar" ? track.subjectsAr[index] : track.subjects[index]);
    if (value === null || Number.isNaN(value)) {
      if (value === null) {
        input.focus();
        showToast(tr("invalid")(language === "ar" ? track.subjectsAr[index] : track.subjects[index]));
      }
      return;
    }
    national.push(value);
  }
  const semesterInputs = [$("#semester-one"), $("#semester-two")];
  if (semesterInputs.some((input) => !input.value.trim())) {
    const firstEmpty = semesterInputs.find((input) => !input.value.trim());
    firstEmpty.focus();
    return showToast(tr("missingContinuous"), tr("missingTitle"));
  }
  const semesterLabels = [tr("semesterOne"), tr("semesterTwo")];
  const semesters = semesterInputs.map((input, index) => readGrade(input, semesterLabels[index]));
  if (semesters.some((value) => value === null || Number.isNaN(value))) return;
  const regionalInputs = [...document.querySelectorAll("#regional-grades input")];
  if (regionalInputs.some((input) => !input.value.trim())) {
    const firstEmpty = regionalInputs.find((input) => !input.value.trim());
    firstEmpty.focus();
    return showToast(tr("missingRegional"), tr("missingTitle"));
  }
  const regional = regionalInputs.map((input, index) => readGrade(input, REGIONAL_SUBJECTS[language][index]));
  if (regional.some((value) => value === null || Number.isNaN(value))) return;

  const nationalAverage = weightedAverage(national, track.coefficients);
  const regionalAverage = weightedAverage(regional, REGIONAL_COEFFICIENTS);
  const continuousAverage = (semesters[0] + semesters[1]) / 2;
  const generalAverage = continuousAverage * 0.25 + regionalAverage * 0.25 + nationalAverage * 0.5;
  const passed = generalAverage >= 10;
  $("#national-average").textContent = `${tr("nationalAverage")}${formatAverage(nationalAverage)}`;
  $("#continuous-average").textContent = `${tr("continuousAverage")}${formatAverage(continuousAverage)}`;
  $("#regional-average").textContent = `${tr("regionalAverage")}${formatAverage(regionalAverage)}`;
  $("#national-score").textContent = formatAverage(nationalAverage);
  $("#regional-score").textContent = formatAverage(regionalAverage);
  $("#general-score").textContent = formatAverage(generalAverage);
  setResultStatus(tr(passed ? "admitted" : "notAdmittedStatus"), passed);
  showResult(generalAverage);
}

function mentionFor(average) {
  if (average < 10) return tr("notAdmitted");
  if (average < 12) return tr("passable");
  if (average < 14) return tr("fairlyGood");
  if (average < 16) return tr("good");
  if (average < 18) return tr("veryGood");
  return tr("excellent");
}

function showResult(average) {
  const passed = average >= 10;
  const dialog = $("#result-dialog");
  const ring = $("#score-ring");
  dialog.classList.toggle("is-fail", !passed);
  $("#dialog-title").textContent = tr("dialogTitle");
  $("#dialog-score").textContent = "0.00";
  $("#grade-mention").textContent = mentionFor(average);
  $("#dialog-feedback").textContent = tr(passed ? "bravo" : "persist");
  $("#dialog-encouragement").textContent = tr(passed ? "passedMessage" : "failedMessage");
  ring.style.setProperty("--ring-color", passed ? "var(--teal)" : "var(--coral)");
  ring.style.setProperty("--progress", "0deg");
  dialog.showModal();

  const start = performance.now();
  const duration = 1050;
  const target = Math.max(0, Math.min(average, 20));
  function animate(now) {
    const progress = Math.min((now - start) / duration, 1);
    const eased = 1 - (1 - progress) ** 3;
    $("#dialog-score").textContent = (target * eased).toFixed(2);
    ring.style.setProperty("--progress", `${target * eased * 18}deg`);
    if (progress < 1) requestAnimationFrame(animate);
  }
  requestAnimationFrame(animate);
}

function showToast(message, title) {
  const toast = $("#toast");
  toast.textContent = title ? `${title} — ${message}` : message;
  toast.hidden = false;
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => { toast.hidden = true; }, 4200);
}

function showPage(which) {
  const national = which === "national";
  $("#national-page").hidden = !national;
  $("#regional-page").hidden = national;
  $("#national-tab").classList.toggle("is-active", national);
  $("#regional-tab").classList.toggle("is-active", !national);
  $("#national-tab").setAttribute("aria-selected", String(national));
  $("#regional-tab").setAttribute("aria-selected", String(!national));
}

$("#theme-button").addEventListener("click", () => setTheme(theme === "dark" ? "light" : "dark"));
$("#language-button").addEventListener("click", () => {
  const menu = $("#language-menu");
  menu.hidden = !menu.hidden;
  $("#language-button").setAttribute("aria-expanded", String(!menu.hidden));
});
$("#language-menu").addEventListener("click", (event) => {
  const item = event.target.closest("[data-language]");
  if (item) setLanguage(item.dataset.language);
});
document.addEventListener("click", (event) => {
  if (!event.target.closest(".language-wrap")) {
    $("#language-menu").hidden = true;
    $("#language-button").setAttribute("aria-expanded", "false");
  }
});
document.addEventListener("keydown", (event) => {
  if (event.key === "Escape") {
    $("#language-menu").hidden = true;
    $("#language-button").setAttribute("aria-expanded", "false");
  }
});
$("#national-tab").addEventListener("click", () => showPage("national"));
$("#regional-tab").addEventListener("click", () => showPage("regional"));
$("#track-select").addEventListener("change", () => {
  gradeState.national = [];
  renderGradeRows();
  resetResultDisplay();
  persistGrades();
});
$("#semester-one").addEventListener("input", (event) => {
  event.currentTarget.removeAttribute("aria-invalid");
  persistGrades();
});
$("#semester-two").addEventListener("input", (event) => {
  event.currentTarget.removeAttribute("aria-invalid");
  persistGrades();
});
$("#calculate-national").addEventListener("click", calculateNational);
$("#calculate-continuous").addEventListener("click", calculateContinuous);
$("#calculate-regional").addEventListener("click", calculateRegional);
$("#calculate-general").addEventListener("click", calculateGeneral);
$("#dialog-close").addEventListener("click", () => $("#result-dialog").close());
$("#result-dialog").addEventListener("click", (event) => {
  if (event.target === $("#result-dialog")) $("#result-dialog").close();
});

restoreGrades();
renderTrackOptions();
renderGradeRows();
updateText();
