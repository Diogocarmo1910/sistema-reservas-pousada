@echo off
setlocal
set "JDK=%LOCALAPPDATA%\Programs\jdk-17\jdk-17.0.20.1+1"
if exist "%JDK%\bin\java.exe" set "PATH=%JDK%\bin;%PATH%"
if not exist bin\pousada\ui\Main.class call compilar.bat
java -cp bin pousada.ui.Main
endlocal
