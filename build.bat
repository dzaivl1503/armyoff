@echo off
title Build Army 2 Offline - Auto Obfuscated
echo =========================================================
echo   Dang bien dich ma nguon Army 2 Offline (Java 8)...
echo =========================================================
if not exist bin mkdir bin

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

powershell -Command "Get-ChildItem -Path 'src' -Filter '*.java' -Recurse | Select-Object -ExpandProperty FullName | Out-File -Encoding ascii 'sources.txt'"
"%JAVAC_CMD%" --release 8 -encoding UTF-8 -cp "lib/*" -d bin @sources.txt
del sources.txt 2>nul
if %ERRORLEVEL% NEQ 0 goto :BUILD_FAIL

echo [OK] Bien dich ma nguon Java thanh cong!
echo Dang dong bo tai nguyen vao bin...
xcopy /s /e /y /q res bin\res >nul 2>nul
xcopy /s /e /y /q rms bin\rms >nul 2>nul
copy /y icon.png bin\icon.png >nul 2>nul

echo Dang dong goi va ma hoa Army2Offline.jar...
"%JAVA_CMD%" -cp bin com.teamobi.mobiarmy2.JarBuilder
if %ERRORLEVEL% NEQ 0 goto :BUILD_FAIL

echo =========================================================
echo   [HOAN TAT] Da tao xong file Army2Offline.jar!
echo =========================================================
goto :END

:BUILD_FAIL
echo =========================================================
echo   [LOI] Qua trinh build that bai!
echo =========================================================

:END
pause
