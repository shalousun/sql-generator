@echo off & setlocal enabledelayedexpansion
::----------------------------------------------------------------------
:: have two main function
:: 1. com.power.sql.generator.window.MainWindow
:: 2. com.power.sql.generator.AppStart
::----------------------------------------------------------------------
set MAIN_FUNC=com.power.sql.generator.window.MainWindow
set LIB_JARS=""
cd ..\lib
for %%i in (*) do set LIB_JARS=!LIB_JARS!;..\lib\%%i
cd ..\bin

if ""%1"" == ""debug"" goto debug
if ""%1"" == ""jmx"" goto jmx

java -Xms512m -Xmx512m -XX:MaxPermSize=64M -classpath ..\conf;%LIB_JARS% %MAIN_FUNC%
goto end

:debug
java -Xms512m -Xmx512m -XX:MaxPermSize=64M -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -classpath ..\conf;%LIB_JARS% %MAIN_FUNC%
goto end

:jmx
java -Xms64m -Xmx512m -XX:MaxPermSize=64M -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -classpath ..\conf;%LIB_JARS% %MAIN_FUNC%

:end
pause