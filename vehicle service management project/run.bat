@echo off
REM ============================================================
REM  run.bat - runs the Vehicle Service Management System.
REM  Run compile.bat first (or this will tell you if it fails).
REM ============================================================
setlocal

cd /d "%~dp0"

set JAR=lib\mysql-connector-j-8.3.0.jar
set OUT=out

if not exist "%OUT%\VehicleServiceApp.class" (
    echo Classes not found. Running compile first...
    call compile.bat
)

echo Starting the application...
java -cp "%OUT%;%JAR%" VehicleServiceApp
