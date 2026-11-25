@echo off
setlocal
cd /d "%~dp0"
set "JDK=%LOCALAPPDATA%\Programs\jdk-17\jdk-17.0.20.1+1"
if exist "%JDK%\bin\javac.exe" set "PATH=%JDK%\bin;%PATH%"
where javac >nul 2>&1
if errorlevel 1 (
  echo Nao encontrei o Java neste computador.
  echo Peca para o laboratorio abrir um prompt e teste o comando: javac -version
  pause
  exit /b 1
)
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin src\pousada\modelo\*.java src\pousada\negocio\*.java src\pousada\persistencia\*.java src\pousada\ui\*.java
if errorlevel 1 (
  echo A compilacao falhou.
  pause
  exit /b 1
)
echo Compilado em bin\
endlocal
