#define MyAppName "BacNote Calcule"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "younesboy90"
#define MyAppExeName "BacNoteCalcule.exe"
#define PublishDir "..\release\x64"

[Setup]
AppId={{9A61B8D5-D4E8-4CBA-BF08-F261C5F9C14D}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={localappdata}\Programs\BacNoteCalcule
DefaultGroupName={#MyAppName}
UninstallDisplayIcon={app}\{#MyAppExeName}
ArchitecturesAllowed=x64
ArchitecturesInstallIn64BitMode=x64
PrivilegesRequired=lowest
OutputDir=..\release\installer
OutputBaseFilename=BacNoteCalcule-Setup
SetupIconFile=..\resources\bac_logo.ico
Compression=lzma2
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "french"; MessagesFile: "compiler:Languages\French.isl"
Name: "arabic"; MessagesFile: "compiler:Languages\Arabic.isl"

[CustomMessages]
english.DesktopIconTask=Create a desktop shortcut
english.AdditionalShortcuts=Additional shortcuts:
english.LaunchApp=Launch {#MyAppName}
french.DesktopIconTask=Créer un raccourci sur le bureau
french.AdditionalShortcuts=Raccourcis supplémentaires :
french.LaunchApp=Lancer {#MyAppName}
arabic.DesktopIconTask=إنشاء اختصار على سطح المكتب
arabic.AdditionalShortcuts=اختصارات إضافية:
arabic.LaunchApp=تشغيل {#MyAppName}

[Tasks]
Name: "desktopicon"; Description: "{cm:DesktopIconTask}"; GroupDescription: "{cm:AdditionalShortcuts}"; Flags: unchecked

[Files]
Source: "{#PublishDir}\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchApp}"; Flags: nowait postinstall skipifsilent
