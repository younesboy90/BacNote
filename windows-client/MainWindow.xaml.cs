using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Diagnostics;
using System.Globalization;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Windows.Controls.Primitives;

namespace BacNoteCalcule;

public partial class MainWindow : Window
{
    private const string PayPalUrl = "https://www.paypal.com/paypalme/xyounes9";
    private const string RedditUrl = "https://www.reddit.com/r/BAcalculeNote/";

    private static readonly BacTrack[] Tracks =
    [
        new("2 Bac Sciences Physiques (PC)", "الثانية باكالوريا علوم فيزيائية (PC)",
            [7, 7, 3, 2, 2],
            ["Mathématiques", "Physique-Chimie", "Sciences de la Vie et de la Terre", "Anglais", "Philosophie"],
            ["الرياضيات", "الفيزياء والكيمياء", "علوم الحياة والأرض", "الإنجليزية", "الفلسفة"]),
        new("2 Bac Sciences de la Vie et de la Terre (SVT)", "الثانية باكالوريا علوم الحياة والأرض (SVT)",
            [5, 5, 7, 2, 2],
            ["Mathématiques", "Physique-Chimie", "Sciences de la Vie et de la Terre", "Anglais", "Philosophie"],
            ["الرياضيات", "الفيزياء والكيمياء", "علوم الحياة والأرض", "الإنجليزية", "الفلسفة"]),
        new("2 Bac Sciences Mathématiques A", "الثانية باكالوريا علوم رياضية أ",
            [9, 7, 3, 2, 2],
            ["Mathématiques", "Physique-Chimie", "Sciences de la Vie et de la Terre", "Anglais", "Philosophie"],
            ["الرياضيات", "الفيزياء والكيمياء", "علوم الحياة والأرض", "الإنجليزية", "الفلسفة"]),
        new("2 Bac Sciences Mathématiques B", "الثانية باكالوريا علوم رياضية ب",
            [9, 7, 3, 2, 2],
            ["Mathématiques", "Physique-Chimie", "Sciences de l’ingénieur", "Anglais", "Philosophie"],
            ["الرياضيات", "الفيزياء والكيمياء", "علوم المهندس", "الإنجليزية", "الفلسفة"])
    ];

    private static readonly (string French, string Arabic, int Coefficient)[] RegionalDefinitions =
    [
        ("Arabe", "اللغة العربية", 2),
        ("Français", "اللغة الفرنسية", 4),
        ("Éducation islamique", "التربية الإسلامية", 2),
        ("Histoire-Géographie", "التاريخ والجغرافيا", 2)
    ];

    private readonly ObservableCollection<LocalizedGrade> _nationalGrades = [];
    private readonly ObservableCollection<LocalizedGrade> _regionalGrades = [];
    private readonly ObservableCollection<LocalizedGrade> _continuousGrades = [];
    private bool _isArabic;
    private bool _isDarkMode;
    private double? _nationalAverage;
    private double? _regionalAverage;
    private double? _continuousAverage;

    public MainWindow()
    {
        InitializeComponent();

        NationalSubjects.ItemsSource = _nationalGrades;
        RegionalSubjects.ItemsSource = _regionalGrades;
        ContinuousSubjects.ItemsSource = _continuousGrades;
        TrackCombo.ItemsSource = Tracks;
        TrackCombo.SelectedIndex = 0;

        _regionalGrades.Add(new("Arabe", "اللغة العربية", 2));
        _regionalGrades.Add(new("Français", "اللغة الفرنسية", 4));
        _regionalGrades.Add(new("Éducation islamique", "التربية الإسلامية", 2));
        _regionalGrades.Add(new("Histoire-Géographie", "التاريخ والجغرافيا", 2));
        _continuousGrades.Add(new("Contrôle continu — semestre 1",
            "المراقبة المستمرة — الدورة الأولى", 1));
        _continuousGrades.Add(new("Contrôle continu — semestre 2",
            "المراقبة المستمرة — الدورة الثانية", 1));

        ApplyLanguage();
        UpdateTrackNames();
        ApplyTheme();
    }

    private void TrackCombo_SelectionChanged(object sender, SelectionChangedEventArgs e)
    {
        if (TrackCombo.SelectedItem is not BacTrack track)
            return;

        _nationalGrades.Clear();
        for (var i = 0; i < track.FrenchSubjects.Length; i++)
            _nationalGrades.Add(new(track.FrenchSubjects[i], track.ArabicSubjects[i],
                track.Coefficients[i]));

        _nationalAverage = null;
        NationalResultText.Text = Localized("Moyenne nationale : -- / 20", "المعدل الوطني: -- / 20");
    }

    private void LanguageButton_Click(object sender, RoutedEventArgs e)
    {
        var menu = new ContextMenu
        {
            Background = (Brush)FindResource("SurfaceBrush"),
            Foreground = (Brush)FindResource("TextBrush"),
            BorderBrush = (Brush)FindResource("LineBrush"),
            Padding = new Thickness(5)
        };
        menu.Items.Add(CreateLanguageMenuItem("Français", false));
        menu.Items.Add(CreateLanguageMenuItem("العربية", true));
        menu.PlacementTarget = LanguageButton;
        menu.Placement = PlacementMode.Bottom;
        menu.IsOpen = true;
    }

    private MenuItem CreateLanguageMenuItem(string language, bool useArabic)
    {
        var header = new StackPanel { Orientation = Orientation.Horizontal };
        header.Children.Add(CreateFlag(useArabic));
        header.Children.Add(new TextBlock
        {
            Text = language,
            VerticalAlignment = VerticalAlignment.Center,
            Margin = new Thickness(9, 0, 2, 0)
        });
        var item = new MenuItem
        {
            Header = header,
            IsChecked = _isArabic == useArabic,
            Background = (Brush)FindResource("SurfaceBrush"),
            Foreground = (Brush)FindResource("TextBrush")
        };
        item.Click += (_, _) =>
        {
            _isArabic = useArabic;
            ApplyLanguage();
            UpdateTrackNames();
            NationalSubjects.Items.Refresh();
            RegionalSubjects.Items.Refresh();
            ContinuousSubjects.Items.Refresh();
        };
        return item;
    }

    private static FrameworkElement CreateFlag(bool moroccan)
    {
        var flag = new Grid { Width = 24, Height = 16 };
        if (!moroccan)
        {
            flag.ColumnDefinitions.Add(new ColumnDefinition());
            flag.ColumnDefinitions.Add(new ColumnDefinition());
            flag.ColumnDefinitions.Add(new ColumnDefinition());
            flag.Children.Add(new Border { Background = new SolidColorBrush(Color.FromRgb(0, 35, 149)) });
            var white = new Border { Background = Brushes.White };
            Grid.SetColumn(white, 1);
            flag.Children.Add(white);
            var red = new Border { Background = new SolidColorBrush(Color.FromRgb(237, 41, 57)) };
            Grid.SetColumn(red, 2);
            flag.Children.Add(red);
            return flag;
        }

        flag.Children.Add(new Border { Background = new SolidColorBrush(Color.FromRgb(193, 39, 45)) });
        flag.Children.Add(new Polygon
        {
            Fill = new SolidColorBrush(Color.FromRgb(0, 98, 51)),
            Points = PointCollection.Parse("12,2 13.3,6.5 18,6.5 14.2,9.2 15.7,14 12,11.1 8.3,14 9.8,9.2 6,6.5 10.7,6.5")
        });
        return flag;
    }

    private void ThemeButton_Click(object sender, RoutedEventArgs e)
    {
        _isDarkMode = !_isDarkMode;
        ApplyTheme();
    }

    private void ApplyLanguage()
    {
        FlowDirection = _isArabic ? FlowDirection.RightToLeft : FlowDirection.LeftToRight;
        FrenchFlag.Visibility = _isArabic ? Visibility.Collapsed : Visibility.Visible;
        MoroccoFlag.Visibility = _isArabic ? Visibility.Visible : Visibility.Collapsed;
        LanguageCodeLabel.Text = _isArabic ? "MA" : "FR";
        Title = Localized("Calculatrice du Bac Marocain", "حاسبة البكالوريا المغربية");
        HeaderTitle.Text = Localized("BAC Note calculé", "حاسبة معدل البكالوريا");
        HeaderSubtitle.Text = Localized("Calculatrice du baccalauréat marocain",
            "حاسبة معدل البكالوريا المغربية");
        RedditLabel.Text = "Reddit";
        DonationLabel.Text = Localized("Donation", "تبرع");
        TrackCaption.Text = Localized("VOTRE FILIÈRE", "الشعبة");
        TrackDescription.Text = Localized("Choisissez votre filière pour afficher les matières correspondantes.",
            "اختر شعبتك لعرض المواد المناسبة.");
        NationalTitle.Text = Localized("Examen national", "الامتحان الوطني");
        NationalHint.Text = Localized("Saisissez vos notes sur 20.", "أدخل نقطك من 20.");
        NationalCalculateButton.Content = Localized("Calculer la moyenne nationale",
            "احسب المعدل الوطني");
        ContinuousTitle.Text = Localized("Contrôle continu", "المراقبة المستمرة");
        ContinuousHint.Text = Localized("Moyenne des deux semestres.", "معدل الدورتين.");
        ContinuousCalculateButton.Content = Localized("Calculer la moyenne", "احسب المعدل");
        RegionalTitle.Text = Localized("Examen régional", "الامتحان الجهوي");
        RegionalHint.Text = Localized("Saisissez vos notes sur 20.", "أدخل نقطك من 20.");
        RegionalCalculateButton.Content = Localized("Calculer la moyenne régionale",
            "احسب المعدل الجهوي");
        WeightsTitle.Text = Localized("Calcul de la moyenne générale", "حساب المعدل العام");
        WeightsDescription.Text = Localized(
            "Contrôle continu 25 %  ·  Régional 25 %  ·  National 50 %",
            "المراقبة المستمرة 25٪  ·  الجهوي 25٪  ·  الوطني 50٪");
        FooterTitle.Text = Localized("Prêt à calculer votre moyenne ?",
            "هل أنت مستعد لحساب معدلك؟");
        FooterHint.Text = Localized(
            "Complétez les notes nationales, régionales et de contrôle continu.",
            "أكمل نقط الامتحان الوطني والجهوي والمراقبة المستمرة.");
        GeneralCalculateButton.Content = Localized("Calculer la moyenne générale",
            "احسب المعدل العام");
        CloseResultButton.Content = Localized("Continuer", "متابعة");
        PopupTitle.Text = Localized("Votre moyenne générale", "معدلك العام");

        NationalResultText.Text = _nationalAverage is { } national
            ? $"{Localized("Moyenne nationale : ", "المعدل الوطني: ")}{FormatAverage(national)}"
            : Localized("Moyenne nationale : -- / 20", "المعدل الوطني: -- / 20");
        RegionalResultText.Text = _regionalAverage is { } regional
            ? $"{Localized("Moyenne régionale : ", "المعدل الجهوي: ")}{FormatAverage(regional)}"
            : Localized("Moyenne régionale : -- / 20", "المعدل الجهوي: -- / 20");
        ContinuousResultText.Text = _continuousAverage is { } continuous
            ? FormatAverage(continuous)
            : "-- / 20";

        UpdatePopupLanguage();

        foreach (var grade in _nationalGrades.Concat(_regionalGrades).Concat(_continuousGrades))
            grade.SetLanguage(_isArabic);
    }

    private void ApplyTheme()
    {
        SetBrush("AccentBrush", _isDarkMode ? "#35C9AF" : "#159180");
        SetBrush("AccentDarkBrush", _isDarkMode ? "#26A890" : "#107C70");
        SetBrush("TextBrush", _isDarkMode ? "#E8EEF4" : "#273548");
        SetBrush("MutedBrush", _isDarkMode ? "#A5B2C0" : "#78869A");
        SetBrush("PageBrush", _isDarkMode ? "#111820" : "#F3F7F8");
        SetBrush("LineBrush", _isDarkMode ? "#34424F" : "#E4EBEE");
        SetBrush("SurfaceBrush", _isDarkMode ? "#1B2631" : "#FFFFFF");
        SetBrush("InputBrush", _isDarkMode ? "#17212B" : "#FAFCFD");
        SetBrush("SoftGreenBrush", _isDarkMode ? "#193A35" : "#EAF7F3");
        SetBrush("SoftBlueBrush", _isDarkMode ? "#1D3443" : "#EDF6FC");
        SetBrush("SoftCoralBrush", _isDarkMode ? "#412B2A" : "#FFF1EE");
        SetBrush("OverlayBrush", _isDarkMode ? "#C9000000" : "#700D1824");
        SetBrush("CoralBrush", _isDarkMode ? "#FF8076" : "#D34F43");
        ThemeIcon.Text = _isDarkMode ? "☼" : "☾";
        ThemeLabel.Text = Localized(_isDarkMode ? "Light" : "Dark",
            _isDarkMode ? "فاتح" : "داكن");
        ThemeButton.ToolTip = Localized(
            _isDarkMode ? "Passer au thème clair" : "Passer au thème sombre",
            _isDarkMode ? "التبديل إلى المظهر الفاتح" : "التبديل إلى المظهر الداكن");
    }

    private static void SetBrush(string key, string color) =>
        Application.Current.Resources[key] =
            new SolidColorBrush((Color)ColorConverter.ConvertFromString(color));

    private void UpdateTrackNames()
    {
        foreach (var track in Tracks)
            track.SetLanguage(_isArabic);
    }

    private string Localized(string french, string arabic) => _isArabic ? arabic : french;

    private void NationalCalculateButton_Click(object sender, RoutedEventArgs e)
    {
        if (!TryReadGrades(_nationalGrades, requireAll: false, "national", out var grades))
            return;

        _nationalAverage = WeightedAverage(_nationalGrades, grades);
        NationalResultText.Text =
            $"{Localized("Moyenne nationale : ", "المعدل الوطني: ")}{FormatAverage(_nationalAverage.Value)}";
    }

    private void ContinuousCalculateButton_Click(object sender, RoutedEventArgs e)
    {
        if (!TryReadGrades(_continuousGrades, requireAll: false, "continuous", out var grades))
            return;
        if (grades.Count == 0)
        {
            ShowValidationMessage("Saisissez au moins une note de contrôle continu.",
                "أدخل نقطة واحدة على الأقل في المراقبة المستمرة.");
            return;
        }

        _continuousAverage = grades.Values.Average();
        ContinuousResultText.Text = FormatAverage(_continuousAverage.Value);
    }

    private void RegionalCalculateButton_Click(object sender, RoutedEventArgs e)
    {
        if (!TryReadGrades(_regionalGrades, requireAll: true, "regional", out var grades))
            return;

        _regionalAverage = WeightedAverage(_regionalGrades, grades);
        RegionalResultText.Text =
            $"{Localized("Moyenne régionale : ", "المعدل الجهوي: ")}{FormatAverage(_regionalAverage.Value)}";
    }

    private void GeneralCalculateButton_Click(object sender, RoutedEventArgs e)
    {
        if (!TryReadGrades(_nationalGrades, requireAll: true, "national", out var nationalGrades) ||
            !TryReadGrades(_regionalGrades, requireAll: true, "regional", out var regionalGrades) ||
            !TryReadGrades(_continuousGrades, requireAll: true, "continuous", out var continuousGrades))
            return;

        _nationalAverage = WeightedAverage(_nationalGrades, nationalGrades);
        _regionalAverage = WeightedAverage(_regionalGrades, regionalGrades);
        _continuousAverage = continuousGrades.Values.Average();
        var overall = _continuousAverage.Value * 0.25 +
                      _regionalAverage.Value * 0.25 +
                      _nationalAverage.Value * 0.50;

        NationalResultText.Text =
            $"{Localized("Moyenne nationale : ", "المعدل الوطني: ")}{FormatAverage(_nationalAverage.Value)}";
        RegionalResultText.Text =
            $"{Localized("Moyenne régionale : ", "المعدل الجهوي: ")}{FormatAverage(_regionalAverage.Value)}";
        ContinuousResultText.Text = FormatAverage(_continuousAverage.Value);
        ShowGradeResult(overall);
    }

    private bool TryReadGrades(IReadOnlyList<LocalizedGrade> entries, bool requireAll,
        string category, out Dictionary<LocalizedGrade, double> grades)
    {
        grades = [];
        foreach (var entry in entries)
        {
            if (string.IsNullOrWhiteSpace(entry.Grade))
            {
                if (requireAll)
                {
                    ShowValidationMessage(
                        $"Saisissez la note de « {entry.Name} ».",
                        $"أدخل نقطة « {entry.Name} ».");
                    return false;
                }
                continue;
            }

            if (!TryParseGrade(entry.Grade, out var grade))
            {
                ShowValidationMessage(
                    $"La note de « {entry.Name} » doit être un nombre entre 0 et 20.",
                    $"يجب أن تكون نقطة « {entry.Name} » رقماً بين 0 و20.");
                return false;
            }

            grades.Add(entry, grade);
        }

        if (grades.Count == 0 && !requireAll)
        {
            var message = category switch
            {
                "national" => ("Saisissez au moins une note de l’examen national.",
                    "أدخل نقطة واحدة على الأقل في الامتحان الوطني."),
                _ => ("Saisissez au moins une note de contrôle continu.",
                    "أدخل نقطة واحدة على الأقل في المراقبة المستمرة.")
            };
            ShowValidationMessage(message.Item1, message.Item2);
            return false;
        }

        return true;
    }

    private static bool TryParseGrade(string text, out double grade)
    {
        var normalized = text.Trim().Replace(',', '.');
        return double.TryParse(normalized, NumberStyles.AllowDecimalPoint,
                   CultureInfo.InvariantCulture, out grade) &&
               double.IsFinite(grade) && grade is >= 0 and <= 20;
    }

    private static double WeightedAverage(IReadOnlyList<LocalizedGrade> entries,
        IReadOnlyDictionary<LocalizedGrade, double> grades)
    {
        var totalWeight = grades.Keys.Sum(entry => entry.Coefficient);
        return grades.Sum(pair => pair.Value * pair.Key.Coefficient) / totalWeight;
    }

    private static string FormatAverage(double value) =>
        $"{value.ToString("F2", CultureInfo.InvariantCulture)} / 20";

    private void ShowValidationMessage(string french, string arabic) =>
        MessageBox.Show(this, Localized(french, arabic),
            Localized("Vérifiez les notes", "تحقق من النقط"),
            MessageBoxButton.OK, MessageBoxImage.Warning, MessageBoxResult.OK,
            _isArabic ? MessageBoxOptions.RightAlign | MessageBoxOptions.RtlReading : MessageBoxOptions.None);

    private void ShowGradeResult(double average)
    {
        var passed = average >= 10;
        var accent = (Brush)FindResource(passed ? "AccentBrush" : "CoralBrush");
        MentionBadge.Background = accent;
        ProgressArc.Stroke = accent;
        PopupScore.Foreground = accent;
        PopupScore.Text = average.ToString("F2", CultureInfo.InvariantCulture);
        PopupMention.Text = GradeMention(average);
        PopupFeedbackTitle.Text = average >= 10
            ? Localized("Bravo !", "أحسنت!")
            : Localized("À persévérer", "واصل المحاولة");
        PopupFeedback.Text = average >= 10
            ? Localized("Félicitations pour votre résultat !", "تهانينا على نتيجتك!")
            : Localized("Continuez vos efforts, vous pouvez progresser !",
                "واصل اجتهادك، يمكنك تحقيق نتيجة أفضل!");
        FeedbackPanel.Background = (Brush)FindResource(passed ? "SoftBlueBrush" : "SoftCoralBrush");
        PopupFeedbackTitle.Foreground = accent;
        ProgressArc.Data = CreateProgressGeometry(Math.Clamp(average / 20, 0, 1));
        PopupTitle.Text = Localized("Votre moyenne générale", "معدلك العام");

        ResultOverlay.Visibility = Visibility.Visible;
        ResultOverlay.Opacity = 0;
        ResultScale.ScaleX = 0.94;
        ResultScale.ScaleY = 0.94;
        ResultOverlay.BeginAnimation(OpacityProperty,
            new DoubleAnimation(0, 1, TimeSpan.FromMilliseconds(180))
            {
                EasingFunction = new QuadraticEase { EasingMode = EasingMode.EaseOut }
            });
        var scaleAnimation = new DoubleAnimation(0.94, 1, TimeSpan.FromMilliseconds(220))
        {
            EasingFunction = new BackEase { EasingMode = EasingMode.EaseOut, Amplitude = 0.12 }
        };
        ResultScale.BeginAnimation(ScaleTransform.ScaleXProperty, scaleAnimation);
        ResultScale.BeginAnimation(ScaleTransform.ScaleYProperty, scaleAnimation);
        CloseResultButton.Focus();
    }

    private Geometry CreateProgressGeometry(double progress)
    {
        var center = 95d;
        var radius = 83d;
        if (progress <= 0)
            return Geometry.Empty;
        if (progress >= 0.9999)
            return new EllipseGeometry(new Point(center, center), radius, radius);

        var angle = progress * 360 - 90;
        var radians = angle * Math.PI / 180;
        var end = new Point(center + radius * Math.Cos(radians),
            center + radius * Math.Sin(radians));
        var figure = new PathFigure
        {
            StartPoint = new Point(center, center - radius),
            IsClosed = false,
            IsFilled = false
        };
        figure.Segments.Add(new ArcSegment(end, new Size(radius, radius), 0,
            progress > 0.5, SweepDirection.Clockwise, true));
        var geometry = new PathGeometry();
        geometry.Figures.Add(figure);
        return geometry;
    }

    private string GradeMention(double average) => average switch
    {
        < 10 => Localized("Non admis", "غير ناجح"),
        < 12 => Localized("Passable", "مقبول"),
        < 14 => Localized("Assez bien", "مستحسن"),
        < 16 => Localized("Bien", "جيد"),
        < 18 => Localized("Très bien", "جيد جداً"),
        _ => Localized("Excellent", "ممتاز")
    };

    private void UpdatePopupLanguage()
    {
        if (ResultOverlay.Visibility != Visibility.Visible)
            return;
        PopupTitle.Text = Localized("Votre moyenne générale", "معدلك العام");
        CloseResultButton.Content = Localized("Continuer", "متابعة");
    }

    private void CloseResultButton_Click(object sender, RoutedEventArgs e) => CloseResult();

    private void ResultOverlay_MouseDown(object sender, MouseButtonEventArgs e)
    {
        if (e.OriginalSource == ResultOverlay)
            CloseResult();
    }

    private void CloseResult()
    {
        var fadeOut = new DoubleAnimation(1, 0, TimeSpan.FromMilliseconds(130));
        fadeOut.Completed += (_, _) => ResultOverlay.Visibility = Visibility.Collapsed;
        ResultOverlay.BeginAnimation(OpacityProperty, fadeOut);
    }

    private void DonationButton_Click(object sender, RoutedEventArgs e) => OpenLink(PayPalUrl);
    private void RedditButton_Click(object sender, RoutedEventArgs e) => OpenLink(RedditUrl);

    private void OpenLink(string url)
    {
        try
        {
            Process.Start(new ProcessStartInfo(url) { UseShellExecute = true });
        }
        catch (System.ComponentModel.Win32Exception)
        {
            ShowValidationMessage("Impossible d’ouvrir le lien. Vérifiez votre navigateur.",
                "تعذر فتح الرابط. تحقق من المتصفح.");
        }
    }

    private sealed class LocalizedGrade : INotifyPropertyChanged
    {
        private readonly string _frenchName;
        private readonly string _arabicName;
        private string _grade = "";
        private string _name;

        public LocalizedGrade(string frenchName, string arabicName, int coefficient)
        {
            _frenchName = frenchName;
            _arabicName = arabicName;
            _name = frenchName;
            Coefficient = coefficient;
        }

        public string Name => _name;
        public int Coefficient { get; }

        public void SetLanguage(bool isArabic)
        {
            _name = isArabic ? _arabicName : _frenchName;
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Name)));
        }

        public string Grade
        {
            get => _grade;
            set
            {
                if (_grade == value)
                    return;
                _grade = value;
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Grade)));
            }
        }

        public event PropertyChangedEventHandler? PropertyChanged;
    }
}
