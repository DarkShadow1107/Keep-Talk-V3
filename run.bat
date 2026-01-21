@echo off
if not exist bin mkdir bin
echo Compiling...
javac -d bin -sourcepath src src/game/*.java src/modules/*.java
if %errorlevel% neq 0 (
    echo Compilation Failed!
    pause
    exit /b
)
echo Copying resources...
if not exist bin\resources mkdir bin\resources
xcopy /y /s src\resources\* bin\resources\
echo Starting Game...
java -cp bin game.Main
pause