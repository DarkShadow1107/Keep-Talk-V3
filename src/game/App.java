package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * Clasa principala a aplicatiei care gestioneaza fereastra (JFrame) si navigarea intre ecrane.
 */
public class App extends JFrame {
    private CardLayout cardLayout; // Layoutul folosit pentru a schimba ecranele
    private JPanel mainPanel; // Panelul principal care contine toate ecranele
    private ExplosionOverlay explosionOverlay; // Overlay-ul pentru efectul de explozie

    public App() {
        setTitle("Keep Talking and Nobody Explodes - Java Edition");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centreaza fereastra pe ecran
        setIconImage(Theme.createIcon()); // Seteaza iconita ferestrei

        // Configurarea GlassPane pentru efecte vizuale de explozie care apar deasupra continutului
        explosionOverlay = new ExplosionOverlay();
        setGlassPane(explosionOverlay);
        explosionOverlay.setVisible(true);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Adaugarea ecranelor in managerul de layout
        mainPanel.add(new IntroScreen(this), "INTRO");
        mainPanel.add(new MainMenu(this), "MENU");
        mainPanel.add(new LevelSelect(this), "LEVEL_SELECT");
        mainPanel.add(new FreePlayMenu(this), "FREE_PLAY");
        mainPanel.add(new Manual(this), "MANUAL");
        mainPanel.add(new Credits(this), "CREDITS");

        add(mainPanel);
        
        // Afisarea initiala a ecranului de introducere
        cardLayout.show(mainPanel, "INTRO");
        setVisible(true);

        // Listener global pentru tastatura pentru a permite intoarcerea la meniu prin tasta ESC
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                showMenu();
                return true;
            }
            return false;
        });
    }

    /** Afiseaza meniul principal. */
    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    /** Afiseaza ecranul de selectie a nivelului. */
    public void showLevelSelect() {
        cardLayout.show(mainPanel, "LEVEL_SELECT");
    }

    /** Afiseaza ecranul pentru modul Free Play. */
    public void showFreePlay() {
        cardLayout.show(mainPanel, "FREE_PLAY");
    }

    /** Afiseaza ecranul cu manualul jocului. */
    public void showManual() {
        cardLayout.show(mainPanel, "MANUAL");
    }

    /** Afiseaza ecranul cu credite. */
    public void showCredits() {
        cardLayout.show(mainPanel, "CREDITS");
    }

    /**
     * Incepe un joc nou cu nivelul specificat.
     * @param level Nivelul care urmeaza sa fie jucat.
     */
    public void startGame(Level level) {
        // Elimina instanta anterioara a jocului daca exista
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof Game) {
                mainPanel.remove(comp);
            }
        }
        
        // Creaza o instanta noua a jocului pentru nivelul ales
        Game gamePanel = new Game(this, level);
        mainPanel.add(gamePanel, "GAME");
        cardLayout.show(mainPanel, "GAME");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /** Termina sesiunea curenta de joc si revine la meniu. */
    public void endGame() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof Game) {
                mainPanel.remove(comp);
            }
        }
        mainPanel.revalidate();
        mainPanel.repaint();
        showMenu();
    }

    /** Declanșează animația de explozie în centrul ferestrei. */
    public void triggerExplosion() {
        explosionOverlay.explode(getWidth() / 2, getHeight() / 2);
    }
}