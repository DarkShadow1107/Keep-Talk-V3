# Build Script for Keep Talking and Nobody Explodes V3

# 1. Setup Directories
$projectDir = Get-Location
$binDir = "$projectDir\bin"
$distDir = "$projectDir\dist"
$installerDir = "$projectDir\installer"

Write-Host "Cleaning directories..."
if (Test-Path $binDir) { Remove-Item -Recurse -Force $binDir }
if (Test-Path $distDir) { Remove-Item -Recurse -Force $distDir }
if (Test-Path $installerDir) { Remove-Item -Recurse -Force $installerDir }

New-Item -ItemType Directory -Force -Path $binDir | Out-Null
New-Item -ItemType Directory -Force -Path $distDir | Out-Null
New-Item -ItemType Directory -Force -Path $installerDir | Out-Null

# 2. Compile
Write-Host "Compiling source code..."
$sources = Get-ChildItem -Recurse -Path "$projectDir\src" -Filter *.java | Select-Object -ExpandProperty FullName
javac -d $binDir $sources

if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed!"
    exit 1
}

# 3. Create JAR
Write-Host "Creating JAR file..."
$jarFile = "$distDir\KeepTalkingV3.jar"
# Create Manifest
$manifestContent = "Main-Class: game.Main`r`n"
$manifestFile = "$projectDir\Manifest.txt"
Set-Content -Path $manifestFile -Value $manifestContent

# Package JAR
jar cfm $jarFile $manifestFile -C $binDir .

if ($LASTEXITCODE -ne 0) {
    Write-Error "JAR creation failed!"
    exit 1
}

Remove-Item $manifestFile

# 4. Create Installer using jpackage
Write-Host "Creating Installer (this may take a minute)..."

# Try to find jpackage
$jpackagePath = "jpackage"
if (-not (Get-Command jpackage -ErrorAction SilentlyContinue)) {
    # Try to find it in the java.home of the current java executable
    $javaHome = java -XshowSettings:properties -version 2>&1 | Select-String "java.home" | ForEach-Object { $_.ToString().Split('=')[1].Trim() }
    $potentialPath = Join-Path $javaHome "bin\jpackage.exe"
    if (Test-Path $potentialPath) {
        $jpackagePath = $potentialPath
        Write-Host "Found jpackage at: $jpackagePath"
    } else {
        Write-Error "jpackage not found! Please ensure you have JDK 14+ installed."
        exit 1
    }
}

Write-Host "Note: Creating an App Image (Portable Folder) because WiX Toolset might not be installed."
Write-Host "To create a single .exe installer, install WiX Toolset (https://wixtoolset.org) and change --type to 'exe'."

& $jpackagePath `
    --name "KeepTalkingV3" `
    --input $distDir `
    --main-jar "KeepTalkingV3.jar" `
    --main-class "game.Main" `
    --type app-image `
    --dest $installerDir `
    --description "Keep Talking and Nobody Explodes V3 - Java Edition" `
    --vendor "GitHub Copilot" `
    --app-version "2.0.0"

if ($LASTEXITCODE -eq 0) {
    Write-Host "App Image created successfully in: $installerDir"
    Write-Host "You can run the game from: $installerDir\KeepTalkingV3\KeepTalkingV3.exe"
    Invoke-Item "$installerDir\KeepTalkingV3"
} else {
    Write-Error "Installer creation failed!"
}
