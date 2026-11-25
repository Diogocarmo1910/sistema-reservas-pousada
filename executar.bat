@echo off
setlocal
cd /d "%~dp0"
set "JDK=%LOCALAPPDATA%\Programs\jdk-17\jdk-17.0.20.1+1"
if exist "%JDK%\bin\java.exe" set "PATH=%JDK%\bin;%PATH%"
where java >nul 2>&1
if errorlevel 1 (
  echo Nao encontrei o Java neste computador.
  echo Peca para o laboratorio abrir um prompt e teste o comando: java -version
  pause
  exit /b 1
)
if not exist bin\pousada\ui\Main.class call "%~dp0compilar.bat"
if errorlevel 1 exit /b 1
java -cp bin pousada.ui.Main
endlocal
