package game;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private String name;
    private int time; // in seconds
    private int moduleCount;
    private int maxStrikes;
    private String description;
    private String difficulty;

    public Level(String name, int time, int moduleCount, int maxStrikes) {
        this(name, time, moduleCount, maxStrikes, "Standard Mission", "NORMAL");
    }

    public Level(String name, int time, int moduleCount, int maxStrikes, String description, String difficulty) {
        this.name = name;
        this.time = time;
        this.moduleCount = moduleCount;
        this.maxStrikes = maxStrikes;
        this.description = description;
        this.difficulty = difficulty;
    }

    public String getName() { return name; }
    public int getTime() { return time; }
    public int getModuleCount() { return moduleCount; }
    public int getMaxStrikes() { return maxStrikes; }
    public String getDescription() { return description; }
    public String getDifficulty() { return difficulty; }

    public static List<Level> getLevels() {
        List<Level> levels = new ArrayList<>();
        
        // EASY (5 Levels)
        levels.add(new Level("The First Bomb", 300, 3, 3, "A simple task to get you started.", "EASY"));
        levels.add(new Level("Something Old", 300, 4, 3, "A mix of classic modules.", "EASY"));
        levels.add(new Level("Training Day", 300, 2, 5, "Practice makes perfect.", "EASY"));
        levels.add(new Level("Baby Steps", 420, 3, 4, "Take your time.", "EASY"));
        levels.add(new Level("Warm Up", 240, 3, 3, "Getting ready for the real deal.", "EASY"));

        // MEDIUM (5 Levels)
        levels.add(new Level("Keep Talking", 300, 5, 3, "Communication is key.", "MEDIUM"));
        levels.add(new Level("Turn The Key", 180, 3, 3, "Quick thinking required.", "MEDIUM"));
        levels.add(new Level("Standard Issue", 300, 6, 3, "Standard bomb defusal.", "MEDIUM"));
        levels.add(new Level("Clockwork", 240, 5, 2, "Watch the timer.", "MEDIUM"));
        levels.add(new Level("Mixed Bag", 360, 7, 3, "A little bit of everything.", "MEDIUM"));

        // HARD (5 Levels)
        levels.add(new Level("Double Trouble", 240, 6, 3, "Twice the modules, twice the fun.", "HARD"));
        levels.add(new Level("Hardcore", 180, 5, 2, "Less time, fewer strikes.", "HARD"));
        levels.add(new Level("Marathon", 600, 11, 5, "A long haul.", "HARD"));
        levels.add(new Level("Digital Age", 300, 8, 3, "Featuring Binary and Logic modules.", "HARD"));
        levels.add(new Level("Pressure Cooker", 120, 5, 3, "Under pressure.", "HARD"));

        // EXPERT (5 Levels)
        levels.add(new Level("Extreme Measures", 120, 6, 1, "One mistake is all it takes.", "EXPERT"));
        levels.add(new Level("Brain Teaser", 300, 9, 2, "Complex modules only.", "EXPERT"));
        levels.add(new Level("Speed Run", 90, 5, 1, "Go fast or go home.", "EXPERT"));
        levels.add(new Level("Chaos Theory", 400, 12, 3, "Unpredictable.", "EXPERT"));
        levels.add(new Level("The Specialist", 240, 8, 1, "For the pros.", "EXPERT"));

        // INSANE (5 Levels)
        levels.add(new Level("Impossible", 60, 4, 1, "Can you do it?", "INSANE"));
        levels.add(new Level("The Gauntlet", 600, 15, 1, "A true test of endurance.", "INSANE"));
        levels.add(new Level("Doomsday", 300, 11, 1, "The end is near.", "INSANE"));
        levels.add(new Level("Final Exam", 180, 10, 1, "Everything you've learned.", "INSANE"));
        levels.add(new Level("Game Over", 60, 6, 0, "Zero margin for error.", "INSANE"));

        return levels;
    }
}