@echo off

echo Resolving MPS ...
CALL gradlew.bat resolveMPS

echo Starting FASTEN ...
cd build\mps\bin\
CALL fasten.bat
echo FASTEN starting in progrss ... it might take a couple of seconds!  
ECHO.
PAUSE
