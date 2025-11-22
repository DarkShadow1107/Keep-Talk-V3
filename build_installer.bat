@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   Keep Talking and Nobody Explodes V3 - Builder
echo ===================================================
echo.

if not exist bin mkdir bin
if not exist dist mkdir dist

echo [1/3] Compiling Java sources...
javac -d bin -sourcepath src src/game/*.java src/modules/*.java
if %errorlevel% neq 0 (
    echo [ERROR] Java compilation failed!
    pause
    exit /b
)

echo [2/3] Creating JAR file...
echo Main-Class: game.Main > Manifest.txt
jar cfm dist\KeepTalking.jar Manifest.txt -C bin .
del Manifest.txt

echo [3/3] Building installer...
echo.
echo Checking for NSIS...

set "NSIS_FOUND=0"

if exist "C:\Program Files (x86)\NSIS\Bin\makensis.exe" (
    set "NSIS_PATH=C:\Program Files (x86)\NSIS\Bin\makensis.exe"
    set "NSIS_FOUND=1"
)

if !NSIS_FOUND!==0 (
    if exist "C:\Program Files (x86)\NSIS\makensis.exe" (
        set "NSIS_PATH=C:\Program Files (x86)\NSIS\makensis.exe"
        set "NSIS_FOUND=1"
    )
)

if !NSIS_FOUND!==0 (
    if exist "C:\Program Files\NSIS\Bin\makensis.exe" (
        set "NSIS_PATH=C:\Program Files\NSIS\Bin\makensis.exe"
        set "NSIS_FOUND=1"
    )
)

if !NSIS_FOUND!==0 (
    if exist "C:\Program Files\NSIS\makensis.exe" (
        set "NSIS_PATH=C:\Program Files\NSIS\makensis.exe"
        set "NSIS_FOUND=1"
    )
)

if !NSIS_FOUND!==1 (
    echo NSIS found!
    echo Building professional installer...
    echo.
    "!NSIS_PATH!" installer.nsi
    if !errorlevel! equ 0 (
        echo.
        echo ===================================================
        echo Build Complete!
        echo ===================================================
        echo.
        echo Professional installer created:
        echo   - dist\KeepTalkingV3_Setup.exe
        echo.
        echo Features:
        echo   + GUI with custom install location
        echo   + Registers in Windows Control Panel  
        echo   + Desktop and Start Menu shortcuts
        echo   + Complete uninstaller
        echo ===================================================
        pause
        exit /b 0
    ) else (
        echo.
        echo [WARNING] NSIS compilation failed
        echo Creating basic installer as fallback...
        echo.
        goto BASIC_INSTALLER
    )
) else (
    echo NSIS not found in common locations.
    echo.
    echo For a professional installer with GUI:
    echo 1. Download NSIS from https://nsis.sourceforge.io/
    echo 2. Install NSIS
    echo 3. Run this script again
    echo.
    goto BASIC_INSTALLER
)

:BASIC_INSTALLER
echo Creating basic Install.bat...

(
echo @echo off
echo title Keep Talking V3 Installer
echo color 0A
echo echo ===================================================
echo echo      KEEP TALKING AND NOBODY EXPLODES V3
echo echo              INSTALLATION WIZARD
echo echo ===================================================
echo echo.
echo set "INSTALL_DIR=%%LOCALAPPDATA%%\KeepTalkingV3"
echo echo Installing to: %%INSTALL_DIR%%
echo echo.
echo if not exist "%%INSTALL_DIR%%" mkdir "%%INSTALL_DIR%%"
echo copy /Y KeepTalking.jar "%%INSTALL_DIR%%\" ^>nul
echo.
echo echo Creating Desktop Shortcut...
echo set SCRIPT="%%TEMP%%\kt_install.vbs"
echo echo Set oWS = WScript.CreateObject("WScript.Shell"^) ^> %%SCRIPT%%
echo echo sLinkFile = "%%USERPROFILE%%\Desktop\Keep Talking V3.lnk" ^>^> %%SCRIPT%%
echo echo Set oLink = oWS.CreateShortcut(sLinkFile^) ^>^> %%SCRIPT%%
echo echo oLink.TargetPath = "javaw" ^>^> %%SCRIPT%%
echo echo oLink.Arguments = "-jar """ ^& "%%INSTALL_DIR%%\KeepTalking.jar" ^& """" ^>^> %%SCRIPT%%
echo echo oLink.WorkingDirectory = "%%INSTALL_DIR%%" ^>^> %%SCRIPT%%
echo echo oLink.IconLocation = "%%INSTALL_DIR%%\KeepTalking.jar" ^>^> %%SCRIPT%%
echo echo oLink.Save ^>^> %%SCRIPT%%
echo cscript //nologo %%SCRIPT%%
echo del %%SCRIPT%%
echo.
echo echo Installation Complete!
echo echo You can launch the game from your Desktop.
echo echo.
echo echo To uninstall, delete:
echo echo   - %%INSTALL_DIR%%
echo echo   - %%USERPROFILE%%\Desktop\Keep Talking V3.lnk
echo pause
) > dist\Install.bat

echo.
echo ===================================================
echo Build Complete!
echo ===================================================
echo.
echo Basic installer created:
  - dist\Install.bat
echo.
echo For better experience, install NSIS and rebuild.
echo ===================================================
pause
