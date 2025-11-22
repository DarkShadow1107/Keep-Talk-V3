; Keep Talking and Nobody Explodes V3 Installer Script
; Requires NSIS (Nullsoft Scriptable Install System)

!include "MUI2.nsh"

; General
Name "Keep Talking and Nobody Explodes V3"
OutFile "dist\KeepTalkingV3_Setup.exe"
InstallDir "$LOCALAPPDATA\KeepTalkingV3"
InstallDirRegKey HKCU "Software\KeepTalkingV3" "InstallDir"
RequestExecutionLevel user

; Interface Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"
!define MUI_WELCOMEFINISHPAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Wizard\orange.bmp"
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Header\orange.bmp"
!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_TEXT "Launch Keep Talking V3"
!define MUI_FINISHPAGE_RUN_FUNCTION "LaunchApplication"

; Pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "LICENSE.txt"
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH

!insertmacro MUI_LANGUAGE "English"

; Version Information
VIProductVersion "3.0.0.0"
VIAddVersionKey "ProductName" "Keep Talking and Nobody Explodes V3"
VIAddVersionKey "CompanyName" "DarkShadow1107"
VIAddVersionKey "FileDescription" "Keep Talking and Nobody Explodes V3 Installer"
VIAddVersionKey "FileVersion" "3.0.0.0"
VIAddVersionKey "LegalCopyright" "© 2025"

; Installer Section
Section "Install"
    SetOutPath "$INSTDIR"
    
    ; Copy files
    File "dist\KeepTalking.jar"
    
    ; Create uninstaller
    WriteUninstaller "$INSTDIR\Uninstall.exe"
    
    ; Create desktop shortcut
    CreateShortcut "$DESKTOP\Keep Talking V3.lnk" "javaw" '-jar "$INSTDIR\KeepTalking.jar"' "$INSTDIR\KeepTalking.jar" 0 SW_SHOWNORMAL "" "Play Keep Talking and Nobody Explodes V3"
    
    ; Create Start Menu shortcuts
    CreateDirectory "$SMPROGRAMS\Keep Talking V3"
    CreateShortcut "$SMPROGRAMS\Keep Talking V3\Keep Talking V3.lnk" "javaw" '-jar "$INSTDIR\KeepTalking.jar"' "$INSTDIR\KeepTalking.jar" 0
    CreateShortcut "$SMPROGRAMS\Keep Talking V3\Uninstall.lnk" "$INSTDIR\Uninstall.exe"
    
    ; Write registry keys for Add/Remove Programs
    WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\KeepTalkingV3" "DisplayName" "Keep Talking and Nobody Explodes V3"
    WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\KeepTalkingV3" "UninstallString" "$\"$INSTDIR\Uninstall.exe$\""
    WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\KeepTalkingV3" "DisplayIcon" "$INSTDIR\KeepTalking.jar"
    WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\KeepTalkingV3" "Publisher" "DarkShadow1107"
    WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\KeepTalkingV3" "DisplayVersion" "3.0.0"
    WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\KeepTalkingV3" "NoModify" 1
    WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\KeepTalkingV3" "NoRepair" 1
    
    ; Store installation folder
    WriteRegStr HKCU "Software\KeepTalkingV3" "InstallDir" $INSTDIR
SectionEnd

; Uninstaller Section
Section "Uninstall"
    ; Remove files
    Delete "$INSTDIR\KeepTalking.jar"
    Delete "$INSTDIR\Uninstall.exe"
    
    ; Remove shortcuts
    Delete "$DESKTOP\Keep Talking V3.lnk"
    Delete "$SMPROGRAMS\Keep Talking V3\Keep Talking V3.lnk"
    Delete "$SMPROGRAMS\Keep Talking V3\Uninstall.lnk"
    RMDir "$SMPROGRAMS\Keep Talking V3"
    
    ; Remove registry keys
    DeleteRegKey HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\KeepTalkingV3"
    DeleteRegKey HKCU "Software\KeepTalkingV3"
    
    ; Remove installation directory
    RMDir /r "$INSTDIR"
SectionEnd

; Launch function
Function LaunchApplication
    Exec 'javaw -jar "$INSTDIR\KeepTalking.jar"'
FunctionEnd
