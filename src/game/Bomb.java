package game;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import modules.BombModule;

/**
 * Reprezinta bomba propriu-zisa si starea acesteia.
 * Gestioneaza timpul, greselile (strikes), modulele si conditiile de victorie/esec.
 */
public class Bomb {
    private int timeRemaining; // Timpul ramas in secunde
    private int strikes; // Numarul actual de greseli
    private final int maxStrikes; // Numarul maxim de greseli permise
    private List<BombModule> modules; // Lista modulelor de pe bomba
    private Timer timer; // Timerul care scade timpul la fiecare secunda
    private Game game; // Referinta catre panelul jocului pentru actualizari UI
    private boolean exploded = false; // Flag pentru a verifica daca bomba a explodat
    private boolean defused = false; // Flag pentru a verifica daca bomba a fost dezamorsata
    
    // Proprietati generate aleatoriu utile pentru rezolvarea modulelor
    private String serialNumber; 
    private int batteries;
    private boolean parallelPort;
    private List<String> indicators;

    /**
     * Constructor pentru clasa Bomb.
     * Initializeaza proprietatile bombei si genereaza valori aleatorii pentru detalii.
     */
    public Bomb(int timeInSeconds, int maxStrikes, Game game) {
        this.timeRemaining = timeInSeconds;
        this.maxStrikes = maxStrikes;
        this.game = game;
        this.modules = new ArrayList<>();
        this.strikes = 0;
        
        // Randomizarea proprietatilor bombei la fiecare joc nou
        java.util.Random rand = new java.util.Random();
        this.serialNumber = "AB1CD" + rand.nextInt(10);
        this.batteries = rand.nextInt(5); // Intre 0 si 4 baterii
        this.parallelPort = rand.nextBoolean();
        this.indicators = new ArrayList<>();
        if (rand.nextBoolean()) indicators.add("FRK");
        if (rand.nextBoolean()) indicators.add("CAR");

        // Timerul apeleaza metoda tick() in fiecare secunda
        timer = new Timer(1000, e -> tick());
    }

    /** Adauga un modul pe bomba. */
    public void addModule(BombModule module) {
        modules.add(module);
    }

    /** Returneaza lista modulelor. */
    public List<BombModule> getModules() {
        return modules;
    }

    /** Porneste timerul bombei. */
    public void start() {
        timer.start();
    }

    /** Opreste timerul bombei. */
    public void stop() {
        timer.stop();
    }

    /** Metoda apelata in fiecare secunda de catre timer. */
    private void tick() {
        if (exploded || defused) return;

        timeRemaining--;
        game.updateTimer(timeRemaining);

        // Daca timpul a expirat, bomba explodeaza
        if (timeRemaining <= 0) {
            explode(Localization.get("GAME_TIME"));
        }
    }

    /** Adauga o greseala. Daca se atinge numarul maxim, bomba explodeaza. */
    public void addStrike() {
        if (exploded || defused) return;
        
        strikes++;
        game.updateStrikes(strikes);
        if (strikes >= maxStrikes) {
            explode(Localization.get("GAME_STRIKES"));
        }
    }

    /** Verifica daca toate modulele au fost rezolvate pentru a declara bomba dezamorsata. */
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
        game.repaint();
    }

    /** Produce explozia bombei cu motivul specificat. */
    private void explode(String reason) {
        exploded = true;
        stop();
        game.onExplode(reason);
    }

    // Gettere pentru diverse proprietati ale bombei
    public int getMaxStrikes() { return maxStrikes; }
    public int getStrikes() { return strikes; }
    public boolean isExploded() { return exploded; }
    public String getSerialNumber() { return serialNumber; }
    public int getTimeRemaining() { return timeRemaining; }
    public int getBatteries() { return batteries; }
    public boolean hasParallelPort() { return parallelPort; }
    public boolean hasIndicator(String label) { return indicators.contains(label); }
    public List<String> getIndicators() { return indicators; }
}