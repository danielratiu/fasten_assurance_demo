@echo off

echo Resolving MPS ...
CALL gradlew.bat resolveMPS

echo Starting FASTEN ...
cd build\mps\bin\
CALL build\mps\bin\fasten.bat
echo FASTEN starting in progrss ... it might take a couple of seconds!  