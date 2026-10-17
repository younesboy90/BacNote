package com.bacnotecalc;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.net.Uri;
import android.os.Build;
import android.text.InputType;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class MainActivity extends Activity {
    private static int BACKGROUND = Color.rgb(247, 248, 250);
    private static int TEXT = Color.rgb(45, 53, 68);
    private static int MUTED = Color.rgb(126, 135, 150);
    private static int TEAL = Color.rgb(21, 145, 128);
    private static int CORAL = Color.rgb(211, 66, 53);
    private static int SURFACE = Color.WHITE;
    private static int BORDER = Color.rgb(225, 230, 236);
    private static int INPUT = Color.rgb(249, 250, 252);
    private static int CHIP = Color.rgb(241, 243, 246);
    private static int TAB_BACKGROUND = Color.rgb(235, 238, 242);
    private static int SOFT_GREEN = Color.rgb(232, 247, 244);
    private static int SOFT_BLUE = Color.rgb(238, 248, 255);
    private static int SOFT_CORAL = Color.rgb(255, 244, 241);
    private static int SOFT_BLUE_ICON = Color.rgb(214, 237, 255);
    private static int SOFT_CORAL_ICON = Color.rgb(255, 225, 218);
    private static final int REDDIT_ORANGE = Color.rgb(255, 69, 0);
    private static final int PAYPAL_BLUE = Color.rgb(0, 112, 186);
    private static final String[] REGIONAL_SUBJECTS = {
            "Arabe", "Français", "Éducation islamique", "Histoire-Géographie"
    };
    private static final String[] REGIONAL_SUBJECTS_AR = {
            "اللغة العربية", "اللغة الفرنسية", "التربية الإسلامية", "التاريخ والجغرافيا"
    };
    private static final int[] REGIONAL_COEFFICIENTS = {2, 4, 2, 2};
    private static final String[] TRACK_NAMES_AR = {
            "الثانية باكالوريا علوم فيزيائية (PC)",
            "الثانية باكالوريا علوم الحياة والأرض (SVT)",
            "الثانية باكالوريا علوم رياضية أ",
            "الثانية باكالوريا علوم رياضية ب"
    };
    private static final String[][] TRACK_SUBJECTS_AR = {
            {"الرياضيات", "الفيزياء والكيمياء", "علوم الحياة والأرض", "الإنجليزية", "الفلسفة"},
            {"الرياضيات", "الفيزياء والكيمياء", "علوم الحياة والأرض", "الإنجليزية", "الفلسفة"},
            {"الرياضيات", "الفيزياء والكيمياء", "علوم الحياة والأرض", "الإنجليزية", "الفلسفة"},
            {"الرياضيات", "الفيزياء والكيمياء", "علوم المهندس", "الإنجليزية", "الفلسفة"}
    };

    private static final Track[] TRACKS = {
            new Track("2 Bac Sciences Physiques (PC)",
                    new String[]{"Mathématiques", "Physique-Chimie",
                            "Sciences de la Vie et de la Terre", "Anglais", "Philosophie"},
                    new int[]{7, 7, 3, 2, 2}),
            new Track("2 Bac Sciences de la Vie et de la Terre (SVT)",
                    new String[]{"Mathématiques", "Physique-Chimie",
                            "Sciences de la Vie et de la Terre", "Anglais", "Philosophie"},
                    new int[]{5, 5, 7, 2, 2}),
            new Track("2 Bac Sciences Mathématiques A",
                    new String[]{"Mathématiques", "Physique-Chimie",
                            "Sciences de la Vie et de la Terre", "Anglais", "Philosophie"},
                    new int[]{9, 7, 3, 2, 2}),
            new Track("2 Bac Sciences Mathématiques B",
                    new String[]{"Mathématiques", "Physique-Chimie",
                            "Sciences de l'ingénieur", "Anglais", "Philosophie"},
                    new int[]{9, 7, 3, 2, 2})
    };

    static {
        System.loadLibrary("baccalculator");
    }

    private final List<EditText> subjectInputs = new ArrayList<>();
    private final List<EditText> regionalInputs = new ArrayList<>();
    private boolean arabicLanguage;
    private boolean darkMode;
    private boolean updatingTrack;
    private LinearLayout subjectRows;
    private LinearLayout regionalRows;
    private LinearLayout pageRoot;
    private LinearLayout nationalPage;
    private LinearLayout regionalPage;
    private LinearLayout languageSelector;
    private LinearLayout languageButton;
    private LinearLayout themeButton;
    private Button nationalTab;
    private Button regionalTab;
    private ImageView languageFlagView;
    private TextView languageCodeView;
    private TextView themeIconView;
    private Spinner trackSpinner;
    private ArrayAdapter<String> trackAdapter;
    private EditText semesterOneInput;
    private EditText semesterTwoInput;
    private TextView continuousAverage;
    private String semesterOneValue = "";
    private String semesterTwoValue = "";
    private TextView nationalScore;
    private TextView nationalCardAverage;
    private TextView regionalScore;
    private TextView generalScore;
    private TextView regionalAverage;
    private TextView resultStatus;
    private View resultCard;
    private TextView titleView;
    private TextView subtitleView;
    private TextView redditLabelView;
    private TextView paypalLabelView;
    private TextView formulaView;

    private static native double calculateNational(double[] grades, int[] coefficients);

    private static native double calculateRegional(double[] grades, int[] coefficients);

    private static native double calculateGeneral(double semesterOne, double semesterTwo,
                                                   double regional, double national);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arabicLanguage = getPreferences(MODE_PRIVATE).getBoolean("arabic_language", false);
        darkMode = getPreferences(MODE_PRIVATE).getBoolean("dark_mode", false);
        setTheme(darkMode ? R.style.AppTheme_Dark : R.style.AppTheme);
        applyPalette();
        getWindow().setStatusBarColor(BACKGROUND);
        getWindow().setNavigationBarColor(SURFACE);
        getWindow().getDecorView().setSystemUiVisibility(darkMode
                ? 0 : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BACKGROUND);
        if (Build.VERSION.SDK_INT >= 35) {
            scroll.setOnApplyWindowInsetsListener((view, insets) -> {
                view.setPadding(0, insets.getSystemWindowInsetTop(), 0,
                        insets.getSystemWindowInsetBottom());
                return insets;
            });
        }

        pageRoot = new LinearLayout(this);
        pageRoot.setOrientation(LinearLayout.VERTICAL);
        pageRoot.setPadding(dp(20), dp(18), dp(20), dp(24));
        pageRoot.setLayoutDirection(arabicLanguage
                ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        scroll.addView(pageRoot);

        buildHeader(pageRoot);
        buildLanguageSelector(pageRoot);
        buildPageTabs(pageRoot);
        int selectedTrack = savedInstanceState == null ? 0
                : savedInstanceState.getInt("selected_track", 0);
        List<String> nationalGrades = savedInstanceState == null ? new ArrayList<>()
                : savedGrades(savedInstanceState, "national_grades");
        List<String> regionalGrades = savedInstanceState == null ? new ArrayList<>()
                : savedGrades(savedInstanceState, "regional_grades");
        if (savedInstanceState != null) {
            semesterOneValue = savedInstanceState.getString("semester_one", "");
            semesterTwoValue = savedInstanceState.getString("semester_two", "");
        }
        rebuildCalculatorPages(selectedTrack, nationalGrades, regionalGrades);
        showPage(savedInstanceState != null && savedInstanceState.getBoolean("regional_page", false));

        setContentView(scroll);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected_track", trackSpinner == null ? 0 : trackSpinner.getSelectedItemPosition());
        outState.putStringArrayList("national_grades", new ArrayList<>(gradeValues(subjectInputs)));
        outState.putStringArrayList("regional_grades", new ArrayList<>(gradeValues(regionalInputs)));
        outState.putString("semester_one", semesterOneInput == null
                ? semesterOneValue : semesterOneInput.getText().toString());
        outState.putString("semester_two", semesterTwoInput == null
                ? semesterTwoValue : semesterTwoInput.getText().toString());
        outState.putBoolean("regional_page", regionalPage != null
                && regionalPage.getVisibility() == View.VISIBLE);
    }

    private List<String> savedGrades(Bundle state, String key) {
        ArrayList<String> grades = state.getStringArrayList(key);
        return grades == null ? new ArrayList<>() : grades;
    }

    private void applyPalette() {
        if (darkMode) {
            BACKGROUND = Color.rgb(17, 24, 32);
            TEXT = Color.rgb(232, 238, 244);
            MUTED = Color.rgb(160, 175, 190);
            TEAL = Color.rgb(53, 201, 175);
            CORAL = Color.rgb(255, 128, 118);
            SURFACE = Color.rgb(27, 38, 49);
            BORDER = Color.rgb(52, 66, 79);
            INPUT = Color.rgb(23, 33, 43);
            CHIP = Color.rgb(36, 49, 62);
            TAB_BACKGROUND = Color.rgb(31, 43, 55);
            SOFT_GREEN = Color.rgb(25, 58, 53);
            SOFT_BLUE = Color.rgb(29, 52, 67);
            SOFT_CORAL = Color.rgb(65, 43, 42);
            SOFT_BLUE_ICON = Color.rgb(36, 71, 91);
            SOFT_CORAL_ICON = Color.rgb(86, 53, 50);
        } else {
            BACKGROUND = Color.rgb(247, 248, 250);
            TEXT = Color.rgb(45, 53, 68);
            MUTED = Color.rgb(126, 135, 150);
            TEAL = Color.rgb(21, 145, 128);
            CORAL = Color.rgb(211, 66, 53);
            SURFACE = Color.WHITE;
            BORDER = Color.rgb(225, 230, 236);
            INPUT = Color.rgb(249, 250, 252);
            CHIP = Color.rgb(241, 243, 246);
            TAB_BACKGROUND = Color.rgb(235, 238, 242);
            SOFT_GREEN = Color.rgb(232, 247, 244);
            SOFT_BLUE = Color.rgb(238, 248, 255);
            SOFT_CORAL = Color.rgb(255, 244, 241);
            SOFT_BLUE_ICON = Color.rgb(214, 237, 255);
            SOFT_CORAL_ICON = Color.rgb(255, 225, 218);
        }
    }

    private void buildHeader(LinearLayout page) {
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);

        ImageView logo = new ImageView(this);
        logo.setImageResource(com.bacnotecalc.R.drawable.bac_logo);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        header.addView(logo, new LinearLayout.LayoutParams(dp(58), dp(83)));

        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.setPadding(dp(12), 0, 0, 0);
        titleView = text(tr("BAC Note calculé", "حاسبة معدل البكالوريا"), 22, TEXT);
        titleView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        titles.addView(titleView, matchWrap());
        subtitleView = text(tr("Calculatrice du baccalauréat marocain",
                "حاسبة معدل البكالوريا المغربية"), 13, MUTED);
        LinearLayout.LayoutParams subtitleParams = matchWrap();
        subtitleParams.topMargin = dp(4);
        titles.addView(subtitleView, subtitleParams);
        header.addView(titles, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        LinearLayout actions = new LinearLayout(this);
        actions.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        actions.setOrientation(LinearLayout.VERTICAL);

        LinearLayout redditButton = new LinearLayout(this);
        redditButton.setGravity(Gravity.CENTER);
        redditButton.setOrientation(LinearLayout.HORIZONTAL);
        redditButton.setPadding(dp(12), 0, dp(14), 0);
        GradientDrawable redditShape = roundDrawable(REDDIT_ORANGE, 22, 0);
        redditButton.setBackground(new RippleDrawable(
                ColorStateList.valueOf(Color.WHITE),
                redditShape, null));
        redditButton.setElevation(dp(2));
        redditButton.setContentDescription(tr("Ouvrir la communauté Reddit BACalculeNote",
                "فتح مجتمع BACalculeNote على ريديت"));
        redditButton.setClickable(true);
        redditButton.setFocusable(true);
        redditButton.setOnClickListener(view -> openReddit());
        ImageView redditIcon = new ImageView(this);
        redditIcon.setImageResource(com.bacnotecalc.R.drawable.reddit_icon);
        redditIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        redditButton.addView(redditIcon, new LinearLayout.LayoutParams(dp(23), dp(23)));
        redditLabelView = text("Reddit", 13, Color.WHITE);
        redditLabelView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        LinearLayout.LayoutParams redditLabelParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        redditLabelParams.leftMargin = dp(6);
        redditButton.addView(redditLabelView, redditLabelParams);
        actions.addView(redditButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(38)));

        LinearLayout paypalButton = new LinearLayout(this);
        paypalButton.setGravity(Gravity.CENTER);
        paypalButton.setPadding(dp(14), 0, dp(14), 0);
        paypalButton.setBackground(new RippleDrawable(
                ColorStateList.valueOf(Color.WHITE),
                roundDrawable(PAYPAL_BLUE, 18, 0), null));
        paypalButton.setElevation(dp(2));
        paypalButton.setContentDescription(tr("Faire un don via PayPal",
                "تبرع عبر PayPal"));
        paypalButton.setClickable(true);
        paypalButton.setFocusable(true);
        paypalButton.setOnClickListener(view -> openPaypalDonation());
        ImageView paypalMark = new ImageView(this);
        paypalMark.setImageResource(R.drawable.paypal_mark);
        paypalMark.setContentDescription(null);
        paypalButton.addView(paypalMark, new LinearLayout.LayoutParams(dp(22), dp(22)));
        paypalLabelView = text("Donation", 13, Color.WHITE);
        paypalLabelView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        LinearLayout.LayoutParams paypalLabelParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paypalLabelParams.leftMargin = dp(5);
        paypalButton.addView(paypalLabelView, paypalLabelParams);
        LinearLayout.LayoutParams paypalParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(34));
        paypalParams.topMargin = dp(6);
        actions.addView(paypalButton, paypalParams);

        LinearLayout.LayoutParams actionsParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        actionsParams.leftMargin = dp(8);
        header.addView(actions, actionsParams);

        LinearLayout.LayoutParams params = matchWrap();
        params.bottomMargin = dp(18);
        page.addView(header, params);
    }

    private void buildLanguageSelector(LinearLayout page) {
        languageSelector = new LinearLayout(this);
        languageSelector.setGravity(Gravity.END);
        languageSelector.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        languageButton = new LinearLayout(this);
        languageButton.setGravity(Gravity.CENTER);
        languageButton.setOrientation(LinearLayout.HORIZONTAL);
        languageButton.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        languageButton.setPadding(dp(10), 0, dp(10), 0);
        languageButton.setBackground(new RippleDrawable(
                ColorStateList.valueOf(BORDER),
                roundDrawable(SURFACE, 10, BORDER), null));
        languageButton.setClickable(true);
        languageButton.setFocusable(true);
        languageButton.setOnClickListener(this::showLanguageMenu);

        languageFlagView = new ImageView(this);
        languageFlagView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        languageButton.addView(languageFlagView, new LinearLayout.LayoutParams(dp(26), dp(18)));
        languageCodeView = text("", 13, TEXT);
        languageCodeView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        LinearLayout.LayoutParams codeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        codeParams.leftMargin = dp(7);
        languageButton.addView(languageCodeView, codeParams);
        TextView arrow = text("▾", 14, MUTED);
        LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        arrowParams.leftMargin = dp(6);
        languageButton.addView(arrow, arrowParams);
        languageSelector.addView(languageButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(40)));

        themeButton = new LinearLayout(this);
        themeButton.setGravity(Gravity.CENTER);
        themeButton.setPadding(dp(12), 0, dp(12), 0);
        themeButton.setBackground(new RippleDrawable(
                ColorStateList.valueOf(BORDER), roundDrawable(SURFACE, 10, BORDER), null));
        themeButton.setClickable(true);
        themeButton.setFocusable(true);
        themeButton.setOnClickListener(view -> toggleTheme());
        themeIconView = text(darkMode ? "☼" : "☾", 18, TEXT);
        themeButton.addView(themeIconView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        themeButton.setContentDescription(darkMode
                ? tr("Passer au thème clair", "التبديل إلى المظهر الفاتح")
                : tr("Passer au thème sombre", "التبديل إلى المظهر الداكن"));
        LinearLayout.LayoutParams themeParams = new LinearLayout.LayoutParams(dp(44), dp(40));
        themeParams.leftMargin = dp(8);
        languageSelector.addView(themeButton, themeParams);

        LinearLayout.LayoutParams params = matchWidth(dp(48));
        params.bottomMargin = dp(12);
        page.addView(languageSelector, params);
        updateLanguageSelector();
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        getPreferences(MODE_PRIVATE).edit().putBoolean("dark_mode", darkMode).apply();
        recreate();
    }

    private void showLanguageMenu(View anchor) {
        LinearLayout options = new LinearLayout(this);
        options.setOrientation(LinearLayout.VERTICAL);
        options.setPadding(dp(5), dp(5), dp(5), dp(5));
        options.setBackground(roundDrawable(SURFACE, 12, BORDER));

        PopupWindow popup = new PopupWindow(options, dp(160),
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable(roundDrawable(SURFACE, 12, BORDER));
        popup.setOutsideTouchable(true);
        popup.setElevation(dp(8));
        addLanguageOption(options, popup, R.drawable.flag_france, "Français", false);
        addLanguageOption(options, popup, R.drawable.flag_morocco, "العربية", true);
        popup.showAsDropDown(anchor, 0, dp(4), Gravity.END);
    }

    private void addLanguageOption(LinearLayout options, PopupWindow popup, int flag,
                                   String label, boolean arabic) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        row.setPadding(dp(10), 0, dp(10), 0);
        row.setBackground(roundDrawable(arabicLanguage == arabic
                ? SOFT_GREEN : SURFACE, 8, 0));

        ImageView flagView = new ImageView(this);
        flagView.setImageResource(flag);
        flagView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        row.addView(flagView, new LinearLayout.LayoutParams(dp(26), dp(18)));

        TextView name = text(label, 14, TEXT);
        name.setGravity(arabic ? Gravity.RIGHT | Gravity.CENTER_VERTICAL
                : Gravity.LEFT | Gravity.CENTER_VERTICAL);
        name.setLayoutDirection(arabic ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        nameParams.leftMargin = dp(10);
        row.addView(name, nameParams);
        row.setClickable(true);
        row.setFocusable(true);
        row.setOnClickListener(view -> {
            popup.dismiss();
            setLanguage(arabic);
        });
        options.addView(row, matchWidth(dp(42)));
    }

    private void updateLanguageSelector() {
        if (languageFlagView == null || languageCodeView == null) {
            return;
        }
        languageFlagView.setImageResource(arabicLanguage
                ? R.drawable.flag_morocco : R.drawable.flag_france);
        languageCodeView.setText(arabicLanguage ? "AR" : "FR");
        languageButton.setContentDescription(tr("Changer de langue",
                "تغيير اللغة"));
        themeIconView.setText(darkMode ? "☼" : "☾");
        themeIconView.setTextColor(TEXT);
        themeButton.setBackground(new RippleDrawable(
                ColorStateList.valueOf(BORDER), roundDrawable(SURFACE, 10, BORDER), null));
        themeButton.setContentDescription(darkMode
                ? tr("Passer au thème clair", "التبديل إلى المظهر الفاتح")
                : tr("Passer au thème sombre", "التبديل إلى المظهر الداكن"));
    }

    private void openReddit() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.reddit.com/r/BAcalculeNote/"));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            new AlertDialog.Builder(this)
                    .setTitle("Reddit")
                    .setMessage(tr("Aucun navigateur n’est installé pour ouvrir la page Reddit.",
                            "لا يوجد متصفح مثبت لفتح صفحة ريديت."))
                    .setPositiveButton(tr("OK", "حسناً"), null)
                    .show();
        }
    }

    private void openPaypalDonation() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.paypal.com/paypalme/xyounes9"));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            new AlertDialog.Builder(this)
                    .setTitle("PayPal")
                    .setMessage(tr("Aucun navigateur n’est installé pour ouvrir PayPal.",
                            "لا يوجد متصفح مثبت لفتح PayPal."))
                    .setPositiveButton(tr("OK", "حسناً"), null)
                    .show();
        }
    }

    private void buildPageTabs(LinearLayout page) {
        LinearLayout tabs = new LinearLayout(this);
        tabs.setPadding(dp(4), dp(4), dp(4), dp(4));
        tabs.setOrientation(LinearLayout.HORIZONTAL);
        tabs.setBackground(roundDrawable(TAB_BACKGROUND, 14, 0));

        nationalTab = pageTab(tr("Calcul Bac", "حساب البكالوريا"));
        regionalTab = pageTab(tr("Examen régional", "الامتحان الجهوي"));
        tabs.addView(nationalTab, new LinearLayout.LayoutParams(0, dp(44), 1));
        tabs.addView(regionalTab, new LinearLayout.LayoutParams(0, dp(44), 1));
        nationalTab.setOnClickListener(view -> showPage(false));
        regionalTab.setOnClickListener(view -> showPage(true));

        LinearLayout.LayoutParams params = matchWidth(dp(52));
        params.bottomMargin = dp(14);
        page.addView(tabs, params);
    }

    private Button pageTab(String label) {
        Button tab = new Button(this);
        tab.setText(label);
        tab.setTextSize(13);
        tab.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tab.setAllCaps(false);
        tab.setStateListAnimator(null);
        return tab;
    }

    private void showPage(boolean showRegional) {
        if (nationalPage == null || regionalPage == null) {
            return;
        }
        nationalPage.setVisibility(showRegional ? View.GONE : View.VISIBLE);
        regionalPage.setVisibility(showRegional ? View.VISIBLE : View.GONE);
        nationalTab.setText(tr("Calcul Bac", "حساب البكالوريا"));
        regionalTab.setText(tr("Examen régional", "الامتحان الجهوي"));
        nationalTab.setTextColor(showRegional ? MUTED : TEAL);
        regionalTab.setTextColor(showRegional ? TEAL : MUTED);
        nationalTab.setBackground(roundDrawable(showRegional ? Color.TRANSPARENT : SURFACE,
                11, 0));
        regionalTab.setBackground(roundDrawable(showRegional ? SURFACE : Color.TRANSPARENT,
                11, 0));
        nationalTab.setContentDescription(tr("Afficher le calcul du Bac",
                "عرض حساب البكالوريا"));
        regionalTab.setContentDescription(tr("Afficher le calcul régional",
                "عرض حساب الامتحان الجهوي"));
    }

    private void rebuildCalculatorPages(int selectedTrack, List<String> nationalGrades,
                                        List<String> regionalGrades) {
        if (nationalPage == null) {
            nationalPage = new LinearLayout(this);
            nationalPage.setOrientation(LinearLayout.VERTICAL);
            pageRoot.addView(nationalPage, matchWrap());
        } else {
            nationalPage.removeAllViews();
        }
        if (regionalPage == null) {
            regionalPage = new LinearLayout(this);
            regionalPage.setOrientation(LinearLayout.VERTICAL);
            pageRoot.addView(regionalPage, matchWrap());
        } else {
            regionalPage.removeAllViews();
        }

        subjectInputs.clear();
        regionalInputs.clear();
        buildTrackPicker(nationalPage);
        buildNationalCard(nationalPage);
        buildGeneralCard(nationalPage);
        buildResultCard(nationalPage);
        formulaView = text(tr("25 % contrôle continu  +  25 % régional  +  50 % national",
                "25٪ المراقبة المستمرة + 25٪ الجهوي + 50٪ الوطني"), 12, MUTED);
        formulaView.setGravity(Gravity.CENTER);
        formulaView.setPadding(0, dp(14), 0, dp(6));
        nationalPage.addView(formulaView, matchWrap());
        buildFooter(nationalPage);
        buildRegionalCard(regionalPage);
        buildFooter(regionalPage);

        updatingTrack = true;
        trackSpinner.setSelection(Math.max(0, Math.min(selectedTrack, TRACKS.length - 1)));
        updateSubjects();
        for (int index = 0; index < subjectInputs.size() && index < nationalGrades.size(); index++) {
            subjectInputs.get(index).setText(nationalGrades.get(index));
        }
        semesterOneInput.setText(semesterOneValue);
        semesterTwoInput.setText(semesterTwoValue);
        for (int index = 0; index < regionalInputs.size() && index < regionalGrades.size(); index++) {
            regionalInputs.get(index).setText(regionalGrades.get(index));
        }
        updatingTrack = false;
    }

    private void setLanguage(boolean useArabic) {
        if (arabicLanguage == useArabic) {
            return;
        }
        int selectedTrack = trackSpinner == null ? 0 : trackSpinner.getSelectedItemPosition();
        List<String> nationalGrades = gradeValues(subjectInputs);
        List<String> regionalGrades = gradeValues(regionalInputs);
        if (semesterOneInput != null) {
            semesterOneValue = semesterOneInput.getText().toString();
        }
        if (semesterTwoInput != null) {
            semesterTwoValue = semesterTwoInput.getText().toString();
        }

        arabicLanguage = useArabic;
        pageRoot.setLayoutDirection(useArabic ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        getPreferences(MODE_PRIVATE).edit().putBoolean("arabic_language", useArabic).apply();
        titleView.setText(tr("BAC Note calculé", "حاسبة معدل البكالوريا"));
        subtitleView.setText(tr("Calculatrice du baccalauréat marocain",
                "حاسبة معدل البكالوريا المغربية"));
        paypalLabelView.setText("Donation");
        updateLanguageSelector();
        nationalTab.setText(tr("Calcul Bac", "حساب البكالوريا"));
        regionalTab.setText(tr("Examen régional", "الامتحان الجهوي"));
        rebuildCalculatorPages(selectedTrack, nationalGrades, regionalGrades);
        showPage(regionalPage.getVisibility() == View.VISIBLE);
    }

    private List<String> gradeValues(List<EditText> inputs) {
        List<String> values = new ArrayList<>(inputs.size());
        for (EditText input : inputs) {
            values.add(input.getText().toString());
        }
        return values;
    }

    private void buildTrackPicker(LinearLayout page) {
        LinearLayout card = card(page);
        addSectionTitle(card, tr("VOTRE FILIÈRE", "الشعبة"));
        trackSpinner = new Spinner(this);
        trackAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, trackNames());
        trackAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trackSpinner.setAdapter(trackAdapter);
        trackSpinner.setBackground(roundDrawable(INPUT, 10, 0));
        trackSpinner.setPadding(dp(12), dp(3), dp(8), dp(3));
        trackSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (!updatingTrack) {
                    updateSubjects();
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
        card.addView(trackSpinner, matchWidth(dp(48)));
    }

    private void buildNationalCard(LinearLayout page) {
        LinearLayout card = card(page);
        addSectionTitle(card, tr("EXAMEN NATIONAL", "الامتحان الوطني"));
        subjectRows = new LinearLayout(this);
        subjectRows.setOrientation(LinearLayout.VERTICAL);
        card.addView(subjectRows, matchWrap());

        Button calculateNational = new Button(this);
        calculateNational.setText(tr("Calculer la moyenne nationale",
                "احسب المعدل الوطني"));
        calculateNational.setTextColor(TEAL);
        calculateNational.setTextSize(14);
        calculateNational.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        calculateNational.setAllCaps(false);
        calculateNational.setBackground(roundDrawable(SURFACE, 12, BORDER));
        calculateNational.setOnClickListener(view -> calculateNationalOnly());
        LinearLayout.LayoutParams buttonParams = matchWidth(dp(48));
        buttonParams.topMargin = dp(10);
        card.addView(calculateNational, buttonParams);

        nationalCardAverage = text(nationalCardAverageText(null), 14, TEAL);
        nationalCardAverage.setGravity(Gravity.CENTER);
        nationalCardAverage.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        LinearLayout.LayoutParams averageParams = matchWidth(dp(34));
        averageParams.topMargin = dp(4);
        card.addView(nationalCardAverage, averageParams);
    }

    private void buildGeneralCard(LinearLayout page) {
        LinearLayout card = card(page);
        addSectionTitle(card, tr("CONTRÔLE CONTINU", "المراقبة المستمرة"));
        semesterOneInput = addGradeRow(card, tr("Contrôle continu — semestre 1",
                "المراقبة المستمرة — الدورة الأولى"));
        semesterTwoInput = addGradeRow(card, tr("Contrôle continu — semestre 2",
                "المراقبة المستمرة — الدورة الثانية"));

        Button calculateContinuous = new Button(this);
        calculateContinuous.setText(tr("Calculer la moyenne du contrôle continu",
                "احسب معدل المراقبة المستمرة"));
        calculateContinuous.setTextColor(TEAL);
        calculateContinuous.setTextSize(14);
        calculateContinuous.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        calculateContinuous.setAllCaps(false);
        calculateContinuous.setBackground(roundDrawable(SURFACE, 12, BORDER));
        calculateContinuous.setOnClickListener(view -> calculateContinuousOnly());
        LinearLayout.LayoutParams buttonParams = matchWidth(dp(48));
        buttonParams.topMargin = dp(10);
        card.addView(calculateContinuous, buttonParams);

        continuousAverage = text(tr("Moyenne du contrôle continu : -- / 20",
                "معدل المراقبة المستمرة: -- / 20"), 14, TEAL);
        continuousAverage.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        continuousAverage.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams averageParams = matchWrap();
        averageParams.topMargin = dp(10);
        card.addView(continuousAverage, averageParams);
    }

    private void buildRegionalCard(LinearLayout page) {
        LinearLayout card = card(page);
        addSectionTitle(card, tr("EXAMEN RÉGIONAL — CALCUL PAR MATIÈRE",
                "الامتحان الجهوي — الحساب حسب المادة"));
        regionalRows = new LinearLayout(this);
        regionalRows.setOrientation(LinearLayout.VERTICAL);
        for (int index = 0; index < REGIONAL_SUBJECTS.length; index++) {
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);

            TextView caption = text(regionalSubject(index), 14, TEXT);
            row.addView(caption, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            TextView coefficient = text("×" + REGIONAL_COEFFICIENTS[index], 12, MUTED);
            coefficient.setGravity(Gravity.CENTER);
            coefficient.setBackground(roundDrawable(CHIP, 8, 0));
            row.addView(coefficient, new LinearLayout.LayoutParams(dp(34), dp(28)));

            EditText input = gradeInput();
            LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(dp(82), dp(43));
            inputParams.leftMargin = dp(8);
            row.addView(input, inputParams);
            regionalInputs.add(input);

            regionalRows.addView(row, matchWidth(dp(47)));
        }
        card.addView(regionalRows, matchWrap());

        Button regionalButton = new Button(this);
        regionalButton.setText(tr("Calculer la moyenne régionale", "احسب المعدل الجهوي"));
        regionalButton.setTextColor(TEAL);
        regionalButton.setTextSize(14);
        regionalButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        regionalButton.setAllCaps(false);
        regionalButton.setBackground(roundDrawable(SURFACE, 12, BORDER));
        regionalButton.setOnClickListener(view -> calculateRegionalOnly());
        LinearLayout.LayoutParams buttonParams = matchWidth(dp(48));
        buttonParams.topMargin = dp(12);
        card.addView(regionalButton, buttonParams);

        regionalAverage = text(tr("Moyenne régionale : -- / 20",
                "المعدل الجهوي: -- / 20"), 14, TEAL);
        regionalAverage.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        regionalAverage.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams averageParams = matchWrap();
        averageParams.topMargin = dp(10);
        card.addView(regionalAverage, averageParams);
    }

    private void buildResultCard(LinearLayout page) {
        LinearLayout card = card(page);
        resultCard = card;

        LinearLayout scores = new LinearLayout(this);
        scores.setGravity(Gravity.CENTER);
        scores.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout nationalColumn = scoreColumn(tr("MOYENNE NATIONALE", "المعدل الوطني"), CORAL);
        nationalScore = (TextView) nationalColumn.getChildAt(1);
        LinearLayout regionalColumn = scoreColumn(tr("MOYENNE RÉGIONALE", "المعدل الجهوي"), TEAL);
        regionalScore = (TextView) regionalColumn.getChildAt(1);
        LinearLayout generalColumn = scoreColumn(tr("MOYENNE GÉNÉRALE", "المعدل العام"), TEAL);
        generalScore = (TextView) generalColumn.getChildAt(1);
        scores.addView(nationalColumn, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        scores.addView(regionalColumn, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        scores.addView(generalColumn, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        card.addView(scores, matchWrap());

        resultStatus = text(tr("Entrez vos notes pour calculer votre moyenne",
                "أدخل النقط لحساب المعدل"), 13, MUTED);
        resultStatus.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams statusParams = matchWrap();
        statusParams.topMargin = dp(8);
        card.addView(resultStatus, statusParams);

        Button calculate = new Button(this);
        calculate.setText(tr("Calculer la moyenne générale",
                "احسب المعدل العام"));
        calculate.setTextColor(Color.WHITE);
        calculate.setTextSize(15);
        calculate.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        calculate.setAllCaps(false);
        calculate.setElevation(dp(3));
        calculate.setBackground(roundDrawable(TEAL, 12, 0));
        calculate.setOnClickListener(view -> calculate());
        LinearLayout.LayoutParams buttonParams = matchWidth(dp(52));
        buttonParams.topMargin = dp(16);
        card.addView(calculate, buttonParams);
    }

    private LinearLayout scoreColumn(String label, int color) {
        LinearLayout column = new LinearLayout(this);
        column.setGravity(Gravity.CENTER);
        column.setOrientation(LinearLayout.VERTICAL);
        TextView caption = text(label, 11, MUTED);
        caption.setGravity(Gravity.CENTER);
        column.addView(caption, matchWrap());
        TextView score = text("-- / 20", 17, color);
        score.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        score.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams scoreParams = matchWrap();
        scoreParams.topMargin = dp(7);
        column.addView(score, scoreParams);
        return column;
    }

    private EditText addGradeRow(LinearLayout parent, String label) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView caption = text(label, 14, TEXT);
        row.addView(caption, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        EditText input = gradeInput();
        row.addView(input, new LinearLayout.LayoutParams(dp(94), dp(43)));

        LinearLayout.LayoutParams rowParams = matchWidth(dp(48));
        rowParams.topMargin = dp(8);
        parent.addView(row, rowParams);
        return input;
    }

    private void updateSubjects() {
        if (trackSpinner == null || subjectRows == null) {
            return;
        }
        subjectRows.removeAllViews();
        subjectInputs.clear();
        Track track = TRACKS[trackSpinner.getSelectedItemPosition()];
        for (int index = 0; index < track.subjects.length; index++) {
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);

            TextView caption = text(trackSubject(track, index), 14, TEXT);
            row.addView(caption, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            TextView coefficient = text("×" + track.coefficients[index], 12, MUTED);
            coefficient.setGravity(Gravity.CENTER);
            coefficient.setBackground(roundDrawable(CHIP, 8, 0));
            row.addView(coefficient, new LinearLayout.LayoutParams(dp(34), dp(28)));

            EditText input = gradeInput();
            LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(dp(82), dp(43));
            inputParams.leftMargin = dp(8);
            row.addView(input, inputParams);
            subjectInputs.add(input);

            subjectRows.addView(row, matchWidth(dp(47)));
        }
        nationalScore.setText("-- / 20");
        nationalCardAverage.setText(nationalCardAverageText(null));
        regionalScore.setText("-- / 20");
        generalScore.setText("-- / 20");
        regionalAverage.setText(tr("Moyenne régionale : -- / 20",
                "المعدل الجهوي: -- / 20"));
        resultStatus.setText(tr("Entrez vos notes pour calculer votre moyenne",
                "أدخل النقط لحساب المعدل"));
        resultStatus.setTextColor(MUTED);
        resultCard.setBackground(roundDrawable(SURFACE, 16, BORDER));
    }

    private void calculate() {
        Track track = TRACKS[trackSpinner.getSelectedItemPosition()];
        double[] grades = new double[subjectInputs.size()];
        for (int index = 0; index < grades.length; index++) {
            Double grade = parseGrade(subjectInputs.get(index), trackSubject(track, index));
            if (grade == null) {
                return;
            }
            grades[index] = grade;
        }

        Double semesterOne = parseGrade(semesterOneInput, tr("Contrôle continu — semestre 1",
                "المراقبة المستمرة — الدورة الأولى"));
        if (semesterOne == null) return;
        Double semesterTwo = parseGrade(semesterTwoInput, tr("Contrôle continu — semestre 2",
                "المراقبة المستمرة — الدورة الثانية"));
        if (semesterTwo == null) return;
        Double regional = calculateRegionalAverage();
        if (regional == null) return;

        double national = calculateNational(grades, track.coefficients);
        double general = calculateGeneral(semesterOne, semesterTwo, regional, national);
        nationalScore.setText(formatAverage(national));
        nationalCardAverage.setText(nationalCardAverageText(national));
        regionalScore.setText(formatAverage(regional));
        generalScore.setText(formatAverage(general));
        resultStatus.setText(general >= 10
                ? tr("ADMIS — Félicitations !", "ناجح — تهانينا!")
                : tr("NON ADMIS — Encore un effort", "غير ناجح — حظاً موفقاً"));
        resultStatus.setTextColor(general >= 10 ? TEAL : CORAL);
        resultCard.setBackground(roundDrawable(general >= 10 ? SOFT_GREEN
                : SOFT_CORAL, 16, BORDER));
        showGradeResult(general);
    }

    private void showGradeResult(double average) {
        boolean passed = average >= 10.0;
        int accent = passed ? TEAL : CORAL;
        Dialog dialog = new Dialog(this);
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER_HORIZONTAL);
        panel.setPadding(dp(20), dp(20), dp(20), dp(18));
        panel.setBackground(roundDrawable(SURFACE, 20, BORDER));
        panel.setLayoutDirection(arabicLanguage
                ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);

        TextView heading = text(tr("Votre moyenne générale", "معدلك العام"), 15, MUTED);
        heading.setGravity(Gravity.CENTER);
        panel.addView(heading, matchWidth(dp(26)));

        FrameLayout scoreRing = new FrameLayout(this);
        GradeRingView ring = new GradeRingView(this, accent);
        scoreRing.addView(ring, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout ringContent = new LinearLayout(this);
        ringContent.setOrientation(LinearLayout.VERTICAL);
        ringContent.setGravity(Gravity.CENTER);
        TextView score = text(String.format(Locale.getDefault(), "%.2f", average), 32, TEXT);
        score.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        score.setGravity(Gravity.CENTER);
        ringContent.addView(score, matchWidth(dp(40)));

        TextView denominator = text("/ 20", 14, MUTED);
        denominator.setGravity(Gravity.CENTER);
        ringContent.addView(denominator, matchWidth(dp(22)));

        TextView mention = text(gradeMention(average), 13, Color.WHITE);
        mention.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        mention.setGravity(Gravity.CENTER);
        mention.setPadding(dp(12), dp(4), dp(12), dp(4));
        mention.setBackground(roundDrawable(accent, 16, 0));
        LinearLayout.LayoutParams mentionParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mentionParams.topMargin = dp(6);
        ringContent.addView(mention, mentionParams);
        scoreRing.addView(ringContent, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER));
        LinearLayout.LayoutParams ringParams = new LinearLayout.LayoutParams(dp(188), dp(188));
        ringParams.topMargin = dp(8);
        panel.addView(scoreRing, ringParams);

        LinearLayout feedback = new LinearLayout(this);
        feedback.setGravity(Gravity.CENTER_VERTICAL);
        feedback.setPadding(dp(12), dp(10), dp(12), dp(10));
        feedback.setBackground(roundDrawable(passed ? SOFT_BLUE : SOFT_CORAL, 14, 0));
        TextView feedbackIcon = text(passed ? "✓" : "↗", 19, accent);
        feedbackIcon.setGravity(Gravity.CENTER);
        feedbackIcon.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        feedbackIcon.setBackground(roundDrawable(passed ? SOFT_BLUE_ICON
                : SOFT_CORAL_ICON, 24, 0));
        feedback.addView(feedbackIcon, new LinearLayout.LayoutParams(dp(40), dp(40)));
        LinearLayout feedbackText = new LinearLayout(this);
        feedbackText.setOrientation(LinearLayout.VERTICAL);
        feedbackText.setPadding(dp(10), 0, 0, 0);
        TextView feedbackTitle = text(gradeFeedbackTitle(passed), 15, accent);
        feedbackTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        feedbackText.addView(feedbackTitle, matchWrap());
        TextView encouragement = text(gradeEncouragement(passed), 12, TEXT);
        feedbackText.addView(encouragement, matchWrap());
        feedback.addView(feedbackText, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        LinearLayout.LayoutParams feedbackParams = matchWrap();
        feedbackParams.topMargin = dp(8);
        panel.addView(feedback, feedbackParams);

        Button close = new Button(this);
        close.setText(tr("Continuer", "متابعة"));
        close.setTextColor(Color.WHITE);
        close.setTextSize(14);
        close.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        close.setAllCaps(false);
        close.setBackground(roundDrawable(accent, 10, 0));
        close.setOnClickListener(view -> dialog.dismiss());
        LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(44));
        closeParams.topMargin = dp(12);
        panel.addView(close, closeParams);

        dialog.setContentView(panel);
        dialog.setCancelable(true);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.getWindow().setDimAmount(0.38f);
            dialog.getWindow().setLayout(
                    getResources().getDisplayMetrics().widthPixels - dp(40),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        panel.setAlpha(0f);
        panel.setScaleX(0.82f);
        panel.setScaleY(0.82f);
        panel.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(420)
                .setInterpolator(new OvershootInterpolator(1.05f))
                .start();
        ValueAnimator ringAnimation = ValueAnimator.ofFloat(0f,
                (float) (Math.max(0.0, Math.min(average, 20.0)) / 20.0));
        ringAnimation.setDuration(1000);
        ringAnimation.setInterpolator(new DecelerateInterpolator());
        ringAnimation.addUpdateListener(animation ->
                ring.setProgress((float) animation.getAnimatedValue()));
        ringAnimation.start();
    }

    private String gradeMention(double average) {
        if (average < 10.0) return tr("Non admis", "غير ناجح");
        if (average < 12.0) return tr("Passable", "مقبول");
        if (average < 14.0) return tr("Assez bien", "مستحسن");
        if (average < 16.0) return tr("Bien", "جيد");
        if (average < 18.0) return tr("Très bien", "جيد جدا");
        return tr("Excellent", "ممتاز");
    }

    private String gradeEncouragement(boolean passed) {
        return passed
                ? tr("Vous avez une belle moyenne au baccalauréat.",
                        "لقد حصلت على معدل جيد في البكالوريا.")
                : tr("Continue tes efforts, tu peux progresser !",
                        "واصل اجتهادك، يمكنك تحقيق نتيجة أفضل!");
    }

    private String gradeFeedbackTitle(boolean passed) {
        return passed ? tr("Bravo !", "أحسنت!") : tr("À persévérer", "واصل المحاولة");
    }

    private void calculateNationalOnly() {
        Track track = TRACKS[trackSpinner.getSelectedItemPosition()];
        List<Double> grades = new ArrayList<>();
        List<Integer> coefficients = new ArrayList<>();
        for (int index = 0; index < subjectInputs.size(); index++) {
            if (subjectInputs.get(index).getText().toString().trim().isEmpty()) {
                continue;
            }
            Double grade = parseGrade(subjectInputs.get(index), trackSubject(track, index));
            if (grade == null) {
                return;
            }
            grades.add(grade);
            coefficients.add(track.coefficients[index]);
        }
        if (grades.isEmpty()) {
            showMissingGradeMessage(tr("Saisissez au moins une note de l'examen national.",
                    "أدخل نقطة واحدة على الأقل في الامتحان الوطني."));
            return;
        }
        double national = calculateNational(toDoubleArray(grades), toIntArray(coefficients));
        nationalScore.setText(formatAverage(national));
        nationalCardAverage.setText(nationalCardAverageText(national));
        regionalScore.setText("-- / 20");
        generalScore.setText("-- / 20");
        resultStatus.setText(tr("Moyenne nationale calculée",
                "تم حساب المعدل الوطني"));
        resultStatus.setTextColor(CORAL);
        resultCard.setBackground(roundDrawable(SURFACE, 16, BORDER));
    }

    private void calculateContinuousOnly() {
        EditText[] inputs = {semesterOneInput, semesterTwoInput};
        String[] labels = {
                tr("Contrôle continu — semestre 1", "المراقبة المستمرة — الدورة الأولى"),
                tr("Contrôle continu — semestre 2", "المراقبة المستمرة — الدورة الثانية")
        };
        double total = 0;
        int count = 0;
        for (int index = 0; index < inputs.length; index++) {
            if (inputs[index].getText().toString().trim().isEmpty()) {
                continue;
            }
            Double grade = parseGrade(inputs[index], labels[index]);
            if (grade == null) {
                return;
            }
            total += grade;
            count++;
        }
        if (count == 0) {
            showMissingGradeMessage(tr("Saisissez au moins une note de contrôle continu.",
                    "أدخل نقطة واحدة على الأقل في المراقبة المستمرة."));
            return;
        }
        continuousAverage.setText(tr("Moyenne du contrôle continu : ",
                "معدل المراقبة المستمرة: ") + formatAverage(total / count));
        regionalScore.setText("-- / 20");
        generalScore.setText("-- / 20");
        resultStatus.setText(tr("Moyenne du contrôle continu calculée",
                "تم حساب معدل المراقبة المستمرة"));
        resultStatus.setTextColor(TEAL);
        resultCard.setBackground(roundDrawable(SURFACE, 16, BORDER));
    }

    private void showMissingGradeMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle(tr("Notes manquantes", "نقط ناقصة"))
                .setMessage(message)
                .setPositiveButton(tr("OK", "حسناً"), null)
                .show();
    }

    private double[] toDoubleArray(List<Double> values) {
        double[] result = new double[values.size()];
        for (int index = 0; index < values.size(); index++) {
            result[index] = values.get(index);
        }
        return result;
    }

    private int[] toIntArray(List<Integer> values) {
        int[] result = new int[values.size()];
        for (int index = 0; index < values.size(); index++) {
            result[index] = values.get(index);
        }
        return result;
    }

    private Double calculateRegionalAverage() {
        double[] grades = new double[regionalInputs.size()];
        for (int index = 0; index < grades.length; index++) {
            Double grade = parseGrade(regionalInputs.get(index), regionalSubject(index));
            if (grade == null) {
                return null;
            }
            grades[index] = grade;
        }
        return calculateRegional(grades, REGIONAL_COEFFICIENTS);
    }

    private void calculateRegionalOnly() {
        Double average = calculateRegionalAverage();
        if (average == null) {
            return;
        }
        regionalAverage.setText(tr("Moyenne régionale : ", "المعدل الجهوي: ")
                + formatAverage(average));
        regionalScore.setText(formatAverage(average));
    }

    private Double parseGrade(EditText input, String label) {
        String value = input.getText().toString().trim().replace(',', '.');
        try {
            double grade = Double.parseDouble(value);
            if (grade >= 0 && grade <= 20 && !Double.isNaN(grade) && !Double.isInfinite(grade)) {
                return grade;
            }
        } catch (NumberFormatException ignored) {
        }
        input.requestFocus();
        new AlertDialog.Builder(this)
                .setTitle(tr("Note invalide", "نقطة غير صالحة"))
                .setMessage(tr("Saisissez une note entre 0 et 20 pour « " + label + " ».",
                        "أدخل نقطة بين 0 و20 في مادة « " + label + " »."))
                .setPositiveButton(tr("OK", "حسناً"), null)
                .show();
        return null;
    }

    private EditText gradeInput() {
        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setTextSize(15);
        input.setTextColor(TEXT);
        input.setHint("0 – 20");
        input.setHintTextColor(MUTED);
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
                | InputType.TYPE_NUMBER_FLAG_SIGNED);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        input.setPadding(dp(6), 0, dp(6), 0);
        input.setBackground(roundDrawable(INPUT, 10, BORDER));
        return input;
    }

    private LinearLayout card(LinearLayout parent) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(roundDrawable(SURFACE, 16, BORDER));
        LinearLayout.LayoutParams params = matchWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dp(14);
        parent.addView(card, params);
        return card;
    }

    private void addSectionTitle(LinearLayout parent, String value) {
        TextView title = text(value, 13, TEAL);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        LinearLayout.LayoutParams params = matchWidth(dp(27));
        params.bottomMargin = dp(10);
        parent.addView(title, params);
    }

    private void buildFooter(LinearLayout parent) {
        TextView footer = text("© 2026 NeydPeric. All rights reserved.", 12, MUTED);
        footer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = matchWrap();
        params.topMargin = dp(16);
        params.bottomMargin = dp(8);
        parent.addView(footer, params);
    }

    private TextView text(String value, int size, int color) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        return view;
    }

    private String[] trackNames() {
        String[] names = new String[TRACKS.length];
        for (int index = 0; index < TRACKS.length; index++) {
            names[index] = arabicLanguage ? TRACK_NAMES_AR[index] : TRACKS[index].name;
        }
        return names;
    }

    private String regionalSubject(int index) {
        return arabicLanguage ? REGIONAL_SUBJECTS_AR[index] : REGIONAL_SUBJECTS[index];
    }

    private String trackSubject(Track track, int index) {
        if (!arabicLanguage) {
            return track.subjects[index];
        }
        int trackIndex = trackSpinner.getSelectedItemPosition();
        return TRACK_SUBJECTS_AR[trackIndex][index];
    }

    private String tr(String french, String arabic) {
        return arabicLanguage ? arabic : french;
    }

    private String formatAverage(double value) {
        return String.format(Locale.getDefault(), "%.2f / 20", value);
    }

    private String nationalCardAverageText(Double value) {
        return tr("Moyenne nationale : ", "المعدل الوطني: ")
                + (value == null ? "-- / 20" : formatAverage(value));
    }

    private GradientDrawable roundDrawable(int color, int radius, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        if (strokeColor != 0) {
            drawable.setStroke(dp(1), strokeColor);
        }
        return drawable;
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private LinearLayout.LayoutParams matchWidth(int height) {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private final class GradeRingView extends View {
        private final Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float progress;

        GradeRingView(Activity context, int accent) {
            super(context);
            float stroke = dp(14);
            trackPaint.setStyle(Paint.Style.STROKE);
            trackPaint.setStrokeWidth(stroke);
            trackPaint.setStrokeCap(Paint.Cap.ROUND);
            trackPaint.setColor(Color.rgb(222, 237, 249));

            progressPaint.setStyle(Paint.Style.STROKE);
            progressPaint.setStrokeWidth(stroke);
            progressPaint.setStrokeCap(Paint.Cap.ROUND);
            int blue = Color.rgb(24, 157, 245);
            progressPaint.setShader(new SweepGradient(0, 0,
                    new int[]{blue, accent, blue}, null));
        }

        void setProgress(float value) {
            progress = Math.max(0f, Math.min(value, 1f));
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float stroke = trackPaint.getStrokeWidth();
            float inset = stroke / 2f + dp(3);
            float diameter = Math.min(getWidth(), getHeight());
            float left = (getWidth() - diameter) / 2f + inset;
            float top = (getHeight() - diameter) / 2f + inset;
            RectF ring = new RectF(left, top, getWidth() - left, getHeight() - top);
            canvas.drawArc(ring, -90f, 360f, false, trackPaint);
            canvas.save();
            canvas.rotate(-90f, getWidth() / 2f, getHeight() / 2f);
            canvas.drawArc(ring, 0f, 360f * progress, false, progressPaint);
            canvas.restore();
        }
    }

    private static final class Track {
        final String name;
        final String[] subjects;
        final int[] coefficients;

        Track(String name, String[] subjects, int[] coefficients) {
            this.name = name;
            this.subjects = subjects;
            this.coefficients = coefficients;
        }
    }
}
