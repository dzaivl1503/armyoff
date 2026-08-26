@echo off
title Mobi Army 2 VPS Server
cd /d "%~dp0"

echo =========================================================
echo   Dang khoi dong Mobi Army 2 VPS Hybrid Server...
echo =========================================================

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

if exist Army2Server.jar (
    "%JAVA_CMD%" -cp "Army2Server.jar;lib/*" Army2Server
) else if exist bin\Army2Server.class (
    "%JAVA_CMD%" -cp "bin;lib/*" Army2Server
) else (
    call build.bat
    "%JAVA_CMD%" -cp "bin;lib/*" Army2Server
)
pause
