package game;

import modules.BombModule;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bomb {
    private int timeRemaining; // in seconds
    private int strikes;
    private final int maxStrikes;
    private List<BombModule> modules;
    private Timer timer;
    private Game game;
    private boolean exploded = false;
    private boolean defused = false;
    private String serialNumber;
    private int batteries;
    private boolean parallelPort;
    private List<String> indicators;

    public Bomb(int timeInSeconds, int maxStrikes, Game game) {
        this.timeRemaining = timeInSeconds;
        this.maxStrikes = maxStrikes;
        this.game = game;
        this.modules = new ArrayList<>();
        this.strikes = 0;
        
        // Randomize bomb properties
        java.util.Random rand = new java.util.Random();
        this.serialNumber = "AB1CD" + rand.nextInt(10);
        this.batteries = rand.nextInt(5); // 0 to 4
        this.parallelPort = rand.nextBoolean();
        this.indicators = new ArrayList<>();
        if (rand.nextBoolean()) indicators.add("FRK");
        if (rand.nextBoolean()) indicators.add("CAR");

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
    }

    public void addModule(BombModule module) {
        modules.add(module);
    }

    public List<BombModule> getModules() {
        return modules;
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    private void tick() {
        if (exploded || defused) return;

        timeRemaining--;
        game.updateTimer(timeRemaining);

        if (timeRemaining <= 0) {
            explode("Time ran out!");
        }
    }

    public void addStrike() {
        if (exploded || defused) return;
        
        strikes++;
        game.updateStrikes(strikes);
        if (strikes >= maxStrikes) {
            explode("Too many strikes!");
        }
    }

    public void checkDefused() {
        if (exploded) return;

        boolean allSolved = true;
        for (BombModule m : modules) {
            if (!m.isSolved()) {
                allSolved = false;
                break;
            }
        }

        if (allSolved) {
            defused = true;
            stop();
            game.onDefused();
        }
    }

    private void explode(String reason) {
        exploded = true;
        stop();
        game.onExplode(reason);
    }

    public int getMaxStrikes() {
        return maxStrikes;
    }

    public int getStrikes() {
        return strikes;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }
    
    public int getTimeRemaining() {
        return timeRemaining;
    }
    
    public int getBatteries() { return batteries; }
    public boolean hasParallelPort() { return parallelPort; }
    public boolean hasIndicator(String label) { return indicators.contains(label); }
    
    public List<String> getIndicators() {
        return indicators;
    }
}