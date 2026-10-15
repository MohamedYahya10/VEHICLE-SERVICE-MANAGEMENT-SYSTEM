@echo off
REM ============================================================
REM  compile.bat - compiles all Java source files.
REM  Run this FIRST. It creates the "out" folder with .class files.
REM ============================================================
setlocal

cd /d "%~dp0"

set JAR=lib\mysql-connector-j-8.3.0.jar

if not exist "out" mkdir out

echo Compiling Java sources...
javac -encoding UTF-8 -cp "%JAR%" -d out src\*.java
if errorlevel 1 (
    echo.
    echo *** COMPILATION FAILED - see the errors above. ***
    pause
    exit /b 1
)

echo.
echo *** COMPILATION SUCCESSFUL. Class files are in the "out" folder. ***
pause
