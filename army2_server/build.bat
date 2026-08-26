@echo off
title Build Army 2 Server (Auto Obfuscated)
cd /d "%~dp0"

echo =========================================================
echo   Dang bien dich Army2Server.jar (Auto Obfuscated)...
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
"%JAVAC_CMD%" -cp "lib/*" -encoding UTF-8 -d bin src/Army2Server.java
if %ERRORLEVEL% NEQ 0 (
    echo [LOI] Bien dich that bai!
    pause
    exit /b 1
)

if exist "lib\mysql-connector-j-8.3.0.jar" (
    cd bin
    "%JAR_CMD%" xf "..\lib\mysql-connector-j-8.3.0.jar"
    cd ..
)

(
echo Main-Class: Army2Server
echo Class-Path: lib/mysql-connector-j-8.3.0.jar
) > manifest.txt

"%JAR_CMD%" cfm Army2Server.jar manifest.txt -C bin .

if exist "tools\proguard.jar" (
    if exist "proguard_server.pro" (
        echo [OBF] Dang ma hoa va bao ve bytecode Server (ProGuard)...
        "%JAVA_CMD%" -jar "tools\proguard.jar" "@proguard_server.pro"
        if exist Army2Server_protected.jar (
            move /y Army2Server_protected.jar Army2Server.jar >nul
            echo [OK] Ma hoa Obfuscation thanh cong!
        )
    )
)

echo [OK] Da tao thanh cong Army2Server.jar!
pause
