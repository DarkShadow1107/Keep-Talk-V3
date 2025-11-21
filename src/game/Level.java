package game;

import java.util.List;
import java.util.ArrayList;

public class Level {
    private String name;
    private int time; // seconds
    private int maxStrikes;
    private int moduleCount;
    private String description;
    private String difficulty;

    public Level(String name, int time, int maxStrikes, int moduleCount, String description, String difficulty) {
        this.name = name;
        this.time = time;
        this.maxStrikes = maxStrikes;
        this.moduleCount = moduleCount;
        this.description = description;
        this.difficulty = difficulty;
    }

    public String getName() { return name; }
    public int getTime() { return time; }
    public int getMaxStrikes() { return maxStrikes; }
    public int getModuleCount() { return moduleCount; }
    public String getDescription() { return description; }
    public String getDifficulty() { return difficulty; }

    public static Level getTrainingLevel() {
        return new Level("Training Camp", 600, 5, 2, "Learn the basics. Plenty of time.", "TRAINING");
    }

    public static List<Level> getLevels() {
        List<Level> levels = new ArrayList<>();
        levels.add(new Level("The First Bomb", 300, 3, 3, "A simple start. 5 minutes, 3 modules.", "EASY"));
        levels.add(new Level("Something Old, Something New", 300, 3, 5, "A bit harder. 5 minutes, 5 modules.", "MEDIUM"));
        levels.add(new Level("Double Trouble", 240, 2, 6, "Less time, more modules.", "HARD"));
        levels.add(new Level("Exotic Bombs", 180, 1, 4, "3 minutes, 1 strike allowed. Be careful.", "HARD"));
        levels.add(new Level("Tick Tock", 120, 2, 3, "2 minutes. Speed is key.", "MEDIUM"));
        levels.add(new Level("The Gauntlet", 420, 3, 8, "Endurance test. 7 minutes.", "HARD"));
        levels.add(new Level("One Mistake", 300, 1, 5, "One strike and you're out.", "HARD"));
        levels.add(new Level("Chaos Theory", 300, 3, 7, "Randomness is your enemy.", "HARD"));
        levels.add(new Level("The Centurion", 900, 5, 15, "A massive bomb. 15 minutes.", "EXPERT"));
        levels.add(new Level("I Am Hardcore", 600, 3, 11, "The ultimate challenge.", "EXPERT"));
        levels.add(new Level("Mission Impossible", 300, 1, 10, "Good luck.", "INSANE"));
        levels.add(new Level("Doomsday", 1200, 5, 20, "The end is near. 20 modules.", "INSANE"));
        levels.add(new Level("Speed Run", 60, 1, 2, "1 minute. Go fast.", "EXPERT"));
        levels.add(new Level("Marathon", 1800, 10, 25, "A true test of endurance.", "INSANE"));
        return levels;
    }
}