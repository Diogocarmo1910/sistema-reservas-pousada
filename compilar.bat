@echo off
setlocal
set "JDK=%LOCALAPPDATA%\Programs\jdk-17\jdk-17.0.20.1+1"
if exist "%JDK%\bin\javac.exe" set "PATH=%JDK%\bin;%PATH%"
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin src\pousada\modelo\*.java src\pousada\negocio\*.java src\pousada\persistencia\*.java src\pousada\ui\*.java
if errorlevel 1 exit /b 1
echo Compilado em bin\
endlocal
