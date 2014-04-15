@echo off
setlocal

rem Change only these two environment variables to fit your environment.
rem ----------------------------------------------------------------------------
set libRoot=b:\fw\netui\build\lib
set webAppRoot=b:\fw\netui\applications\netuiDRTApp\netuiDRT\qaNetuiWeb

rem Prep for and compile the struts classes.
rem ----------------------------------------------------------------------------
set classesDir=%webAppRoot%\WEB-INF\classes
set testRoot=%webAppRoot%\WEB-INF\src\miscJpf\test13

set cp=%libRoot%\tomcat\4x\servlet.jar
set cp=%cp%;%libRoot%\struts\struts.jar
javac -d %classesDir% -classpath %cp% %testRoot%\*.java

rem Copy the message properties file to the classes directory.
rem ----------------------------------------------------------------------------
attrib -r %classesDir%\miscJpf\test13\test13.properties
copy /y %testRoot%\test13.properties %classesDir%\miscJpf\test13\test13.properties > nul

:exit
endlocal
