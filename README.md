# Keep Talking and Nobody Explodes - Java Edition

This is a Java implementation of a bomb defusal game inspired by "Keep Talking and Nobody Explodes".

## Features

-   **Modern UI**: Dark theme with neon accents, gradients, and custom components.
-   **Main Menu**: Navigate between game modes, manual, and credits.
-   **Level System**: Choose from multiple difficulty levels (Easy, Medium, Hard, Expert) and a Training mode.
-   **Modules**:
    -   **Wires**: Cut the correct wire.
    -   **The Button**: Press or hold based on complex rules.
    -   **Keypad**: Press symbols in the correct order.
    -   **Simon Says**: Repeat the flashing color sequence.
    -   **Password**: Guess the 5-letter word.
    -   **Maze**: Navigate a hidden maze.
    -   **Morse Code**: Decode the flashing light and tune the frequency.
-   **In-Game Manual**: View the rules directly within the game with improved formatting.
-   **Visual Effects**: Explosion animations and dynamic feedback.

## How to Run

1. Ensure you have Java installed (Java 21 is recommended).
2. Open a terminal in the project root.
3. Run the `run.bat` file (Windows) or compile manually:
    ```bash
    cd src
    javac -d ../out game/Main.java game/App.java game/MainMenu.java game/LevelSelect.java game/Level.java game/Manual.java game/Credits.java game/Game.java game/Bomb.java game/Theme.java modules/BombModule.java modules/WiresModule.java modules/ButtonModule.java modules/KeypadModule.java modules/SimonSaysModule.java modules/PasswordModule.java modules/MazeModule.java modules/MorseCodeModule.java
    java -cp ../out game.Main
    ```

## Credits

Original game concept by Steel Crate Games.
Java implementation by GitHub Copilot.
