using System.ComponentModel;

namespace BacNoteCalcule;

public sealed class BacTrack : INotifyPropertyChanged
{
    private readonly string _frenchName;
    private readonly string _arabicName;
    private string _displayName;

    public BacTrack(string frenchName, string arabicName, int[] coefficients,
        string[] frenchSubjects, string[] arabicSubjects)
    {
        _frenchName = frenchName;
        _arabicName = arabicName;
        _displayName = frenchName;
        Coefficients = coefficients;
        FrenchSubjects = frenchSubjects;
        ArabicSubjects = arabicSubjects;
    }

    public string[] FrenchSubjects { get; }
    public string[] ArabicSubjects { get; }
    public int[] Coefficients { get; }
    public string DisplayName => _displayName;

    public void SetLanguage(bool isArabic)
    {
        _displayName = isArabic ? _arabicName : _frenchName;
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(DisplayName)));
    }

    public event PropertyChangedEventHandler? PropertyChanged;
}
