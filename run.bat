@echo off
if not exist bin mkdir bin
echo Compiling...
javac -d bin -sourcepath src src/game/Main.java
if %errorlevel% neq 0 (
    echo Compilation Failed!
    pause
    exit /b
)
echo Starting Game...
java -cp bin game.Main
pause