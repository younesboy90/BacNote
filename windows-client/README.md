# BAC Note calculé — Windows client

The Windows client is a bilingual French/Arabic WPF desktop application built
with C# and .NET 10. It is published as a self-contained x64 executable, so
the .NET runtime does not need to be installed separately.

The wide two-column layout keeps national and regional subjects visible
together. Subject coefficients are used internally in the weighted averages
but are not displayed. The final grade appears in an animated in-window
overlay to avoid the flicker caused by the former layered popup window.
Grade inputs use the standard WPF text editor so full values, including
decimals, remain visible while typing. The header includes the Reddit and
PayPal shortcuts, a French/Moroccan flag language menu, and a live light/dark
theme switch.

## Build

Install the .NET 10 SDK, then run from this folder:

```powershell
dotnet build .\BacNoteCalcule.csproj --configuration Release
dotnet publish .\BacNoteCalcule.csproj --configuration Release --runtime win-x64 --self-contained true -p:PublishSingleFile=true -o .\release\x64
```

The packaged x64 client is `release\x64\BacNoteCalcule.exe`. Keep the WPF
native support DLLs in the same folder as the executable.

## Build an installer

Install [Inno Setup 6](https://jrsoftware.org/isinfo.php), then publish the
client using the command above. From this folder, compile the installer with:

```powershell
& "${env:ProgramFiles(x86)}\Inno Setup 6\ISCC.exe" .\installer\BacNoteCalcule.iss
```

The setup program is created at
`release\installer\BacNoteCalcule-Setup.exe`. It installs the app for the
current Windows user and can optionally create a desktop shortcut. The
installer lets users select English, French, or Arabic.

## Layout

- `App.xaml` — shared WPF colors and control styles
- `MainWindow.xaml` / `MainWindow.xaml.cs` — application layout and behavior
- `BacTrack.cs` — exam-stream metadata and hidden subject weights
- `resources\` — application icon and logo
- `release\x64\` — packaged standalone x64 client
- `installer\BacNoteCalcule.iss` — Inno Setup installer definition
