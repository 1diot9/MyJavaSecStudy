@echo off
setlocal enabledelayedexpansion

rem Source and destination paths
set "SRC=D:\U8CERP"
set "DEST=D:\U8CERP\allupms"

rem Create destination directory if it does not exist
if not exist "%DEST%" (
    mkdir "%DEST%"
)

rem Copy all .jar files
echo Copying all .upm files from %SRC% to %DEST% ...
for /r "%SRC%" %%f in (*.upm) do (
    echo Copying: %%f
    copy "%%f" "%DEST%" >nul
)

rem Copy all "classes" folders recursively
rem echo Copying all "classes" folders from %SRC% to %DEST% ...
rem for /r "%SRC%" %%d in (classes) do (
rem    if exist "%%d" (
rem        echo Copying folder: %%d
rem        xcopy "%%d" "%DEST%\classes" /e /i /y >nul
rem    )
rem )

echo All files copied successfully.
pause
