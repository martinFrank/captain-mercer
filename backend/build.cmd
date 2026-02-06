@echo off
set "JAVA_HOME=C:\Users\m.frank\scoop\apps\temurin25-jdk\current"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
call "%~dp0mvnw.cmd" %*
