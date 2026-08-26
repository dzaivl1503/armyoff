@echo off
title Build Army 2 Server
cd /d "%~dp0"

echo =========================================================
echo   Dang bien dich Army2Server.jar - Auto Obfuscated...
echo =========================================================

set "JAVAC_CMD=javac"
where javac >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    if exist "C:\Program Files\Java\jdk-19\bin\javac.exe" (
        set "JAVAC_CMD=C:\Program Files\Java\jdk-19\bin\javac.exe"
    ) else if exist "C:\Program Files (x86)\Java\jdk1.8.0_291\bin\javac.exe" (
        set "JAVAC_CMD=C:\Program Files (x86)\Java\jdk1.8.0_291\bin\javac.exe"
    ) else if defined JAVA_HOME (
        if exist "%JAVA_HOME%\bin\javac.exe" set "JAVAC_CMD=%JAVA_HOME%\bin\javac.exe"
    )
)

set "JAR_CMD=jar"
where jar >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    if exist "C:\Program Files\Java\jdk-19\bin\jar.exe" (
        set "JAR_CMD=C:\Program Files\Java\jdk-19\bin\jar.exe"
    ) else if exist "C:\Program Files (x86)\Java\jdk1.8.0_291\bin\jar.exe" (
        set "JAR_CMD=C:\Program Files (x86)\Java\jdk1.8.0_291\bin\jar.exe"
    ) else if defined JAVA_HOME (
        if exist "%JAVA_HOME%\bin\jar.exe" set "JAR_CMD=%JAVA_HOME%\bin\jar.exe"
    )
)

set "JAVA_CMD=java"
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    if exist "C:\Program Files\Java\jdk-19\bin\java.exe" (
        set "JAVA_CMD=C:\Program Files\Java\jdk-19\bin\java.exe"
    ) else if exist "C:\Program Files (x86)\Java\jdk1.8.0_291\bin\java.exe" (
        set "JAVA_CMD=C:\Program Files (x86)\Java\jdk1.8.0_291\bin\java.exe"
    ) else if defined JAVA_HOME (
        if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
    )
)

if not exist bin mkdir bin
if exist bin_dist rmdir /s /q bin_dist
mkdir bin_dist

"%JAVAC_CMD%" --release 8 -cp "lib/*" -encoding UTF-8 -d bin src\Army2Server.java
if %ERRORLEVEL% NEQ 0 (
    echo [LOI] Bien dich that bai!
    pause
    exit /b 1
)

if exist "tools\proguard.jar" (
    echo [OBF] Dang ma hoa Obfuscate ma nguon - ProGuard...
    "%JAVA_CMD%" -jar "tools\proguard.jar" "@proguard_server.pro"
    if exist bin_obf.jar (
        powershell -Command "Add-Type -AssemblyName System.IO.Compression.FileSystem; [System.IO.Compression.ZipFile]::ExtractToDirectory('bin_obf.jar', 'bin_dist'); Remove-Item 'bin_obf.jar' -Force -ErrorAction SilentlyContinue"
    ) else (
        xcopy /s /e /y /q bin\* bin_dist\ >nul 2>nul
    )
) else (
    xcopy /s /e /y /q bin\* bin_dist\ >nul 2>nul
)

if exist "lib\mysql-connector-j-8.3.0.jar" (
    powershell -Command "Add-Type -AssemblyName System.IO.Compression.FileSystem; [System.IO.Compression.ZipFile]::ExtractToDirectory('lib\mysql-connector-j-8.3.0.jar', 'bin_dist');"
)

echo Main-Class: Army2Server> manifest.txt
echo Class-Path: lib/mysql-connector-j-8.3.0.jar>> manifest.txt

"%JAR_CMD%" cfm Army2Server.jar manifest.txt -C bin_dist .
rmdir /s /q bin_dist 2>nul

echo [OK] Da tao thanh cong Army2Server.jar - Auto Obfuscated Fat JAR!
pause
