@echo off
title Build Army 2 Offline
echo Dang bien dich ma nguon Army 2 Offline (Java 8 compatibility)...
if not exist bin mkdir bin
powershell -Command "Get-ChildItem -Path 'src' -Filter '*.java' -Recurse | Select-Object -ExpandProperty FullName | Out-File -Encoding ascii 'sources.txt'"
javac --release 8 -encoding UTF-8 -cp "lib/*" -d bin @sources.txt
if %ERRORLEVEL% EQU 0 (
    echo [OK] Bien dich thanh cong 100%%!
    del sources.txt 2>nul
    echo Dang dong bo tai nguyen vao bin...
    xcopy /s /e /y /q res bin\res >nul 2>nul
    xcopy /s /e /y /q rms bin\rms >nul 2>nul
    copy /y icon.png bin\icon.png >nul 2>nul
    echo Dang dong goi Army2Offline.jar...
    java -cp bin com.teamobi.mobiarmy2.JarBuilder
    echo Hoan tat build!
) else (
    echo [LOI] Bien dich that bai!
)
pause
