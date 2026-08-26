@echo off
title Build Army 2 Server
cd /d "%~dp0"

echo =========================================================
echo   Dang bien dich Army2Server.jar...
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
echo [OK] Da tao thanh cong Army2Server.jar (Fat JAR)!
pause
