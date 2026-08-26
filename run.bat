@echo off
title Army 2 Offline
echo Dang khoi chay Army 2 Offline tren MicroEmulator...

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

"%JAVA_CMD%" -cp "bin;lib/*" Launcher
if %ERRORLEVEL% NEQ 0 (
    echo Co loi xay ra hoac chua build. Dang tien hanh bien dich lai...
    call build.bat
    "%JAVA_CMD%" -cp "bin;lib/*" Launcher
)
pause
