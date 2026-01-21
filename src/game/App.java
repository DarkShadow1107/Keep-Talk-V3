package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class App extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ExplosionOverlay explosionOverlay;

    public App() {
        setTitle("Keep Talking and Nobody Explodes - Java Edition");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(Theme.createIcon());

        // Setup GlassPane for explosions
        explosionOverlay = new ExplosionOverlay();
        setGlassPane(explosionOverlay);
        explosionOverlay.setVisible(true);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new IntroScreen(this), "INTRO");
        mainPanel.add(new MainMenu(this), "MENU");
        mainPanel.add(new LevelSelect(this), "LEVEL_SELECT");
        mainPanel.add(new FreePlayMenu(this), "FREE_PLAY");
        mainPanel.add(new Manual(this), "MANUAL");
        mainPanel.add(new Credits(this), "CREDITS");

        add(mainPanel);
        
        cardLayout.show(mainPanel, "INTRO");
        setVisible(true);

        // Global Key Listener for Navigation
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                showMenu();
                return true;
            }
            return false;
        });
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    public void showLevelSelect() {
        cardLayout.show(mainPanel, "LEVEL_SELECT");
    }

    public void showFreePlay() {
        cardLayout.show(mainPanel, "FREE_PLAY");
    }

    public void showManual() {
        cardLayout.show(mainPanel, "MANUAL");
    }

    public void showCredits() {
        cardLayout.show(mainPanel, "CREDITS");
    }

    public void startGame(Level level) {
        // Remove existing game panel if it exists
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof Game) {
                mainPanel.remove(comp);
            }
        }
        
        // Create a new Game instance for the level
        Game gamePanel = new Game(this, level);
        mainPanel.add(gamePanel, "GAME");
        cardLayout.show(mainPanel, "GAME");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void endGame() {
        // Remove existing game panel to reset state next time
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof Game) {
                mainPanel.remove(comp);
            }
        }
        mainPanel.revalidate();
        mainPanel.repaint();
        showMenu();
    }

    public void triggerExplosion() {
        explosionOverlay.explode(getWidth() / 2, getHeight() / 2);
    }
}