@echo off
title Army 2 Offline
echo Dang khoi chay Army 2 Offline tren MicroEmulator...
java -cp "bin;lib/*" Launcher
if %ERRORLEVEL% NEQ 0 (
    echo Co loi xay ra hoac chua build. Dang tien hanh bien dich lai...
    call build.bat
    java -cp "bin;lib/*" Launcher
)
pause
