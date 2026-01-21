package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KeypadModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<Key> keys = new ArrayList<>();
    private List<String> solution = new ArrayList<>();
    private int currentStep = 0;

    // Simplified symbols (using Unicode)
    private static final String[] SYMBOLS = {
        "\u03A9", "\u03A8", "\u03A6", "\u039E", // Greek
        "\u0416", "\u0429", "\u0424", "\u0414", // Cyrillic
        "\u2605", "\u2606", "\u2660", "\u2663", // Shapes
        "\u00A9", "\u00AE", "\u00B6", "\u00BF"  // Misc
    };

    public KeypadModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
    }

    private void generatePuzzle() {
        Random rand = new Random();
        // Pick 4 random symbols
        List<String> chosen = new ArrayList<>();
        while (chosen.size() < 4) {
            String s = SYMBOLS[rand.nextInt(SYMBOLS.length)];
            if (!chosen.contains(s)) {
                chosen.add(s);
            }
        }
        
        // For this simplified version, the solution is just alphabetical order of the symbols
        // In the real game, it's column-based logic.
        solution = new ArrayList<>(chosen);
        solution.sort(String::compareTo);
        
        // Shuffle keys for display
        keys.clear();
        List<String> displayOrder = new ArrayList<>(chosen);
        // Don't shuffle for now so we can debug easily, or shuffle?
        // Let's shuffle to make it a puzzle
        for (int i = 0; i < displayOrder.size(); i++) {
            int swap = rand.nextInt(displayOrder.size());
            String temp = displayOrder.get(i);
            displayOrder.set(i, displayOrder.get(swap));
            displayOrder.set(swap, temp);
        }

        for (String s : displayOrder) {
            keys.add(new Key(s));
        }
    }

    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw Status LED
                Theme.drawLed(g2, getWidth() - 25, 15, solved, true);

                int padding = 15;
                int gap = 15;
                int cols = 2;
                int rows = 2;
                int width = (getWidth() - (padding * 2) - gap) / cols;
                int height = (getHeight() - (padding * 2) - gap) / rows;

                for (int i = 0; i < keys.size(); i++) {
                    Key key = keys.get(i);
                    int col = i % cols;
                    int row = i / cols;
                    int x = padding + col * (width + gap);
                    int y = padding + row * (height + gap);
                    
                    key.bounds = new Rectangle(x, y, width, height);

                    // Draw Key Background
                    if (key.correct) {
                        g2.setColor(new Color(40, 100, 40)); // Greenish lit
                    } else if (key.pressed) {
                        g2.setColor(new Color(100, 40, 40)); // Reddish lit (strike)
                    } else {
                        g2.setColor(new Color(200, 190, 180)); // Stone/Metal color
                    }
                    
                    RoundRectangle2D rect = new RoundRectangle2D.Float(x, y, width, height, 10, 10);
                    g2.fill(rect);

                    // Bevel/Shadow
                    g2.setStroke(new BasicStroke(3));
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.draw(rect);
                    
                    // Symbol
                    g2.setFont(new Font("Segoe UI Symbol", Font.BOLD, 36));
                    FontMetrics fm = g2.getFontMetrics();
                    int textX = x + (width - fm.stringWidth(key.symbol)) / 2;
                    int textY = y + (height + fm.getAscent()) / 2 - 5;
                    
                    g2.setColor(new Color(50, 40, 30)); // Dark engraved look
                    g2.drawString(key.symbol, textX, textY);
                    
                    // Inner highlight for engraved look
                    g2.setColor(new Color(255, 255, 255, 100));
                    g2.drawString(key.symbol, textX + 1, textY + 1);
                    
                    // Draw Key Number (for keyboard support)
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.setFont(Theme.FONT_MONO.deriveFont(12f));
                    g2.drawString(String.valueOf(i + 1), x + 5, y + 15);
                }
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(200, 200));
        Theme.applyCursor(panel);
        panel.setFocusable(true); // Enable focus for keyboard

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocusInWindow();
                if (solved) return;
                for (Key key : keys) {
                    if (key.bounds.contains(e.getPoint())) {
                        handlePress(key);
                        break;
                    }
                }
            }
        });
        
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (solved) return;
                int k = e.getKeyCode();
                // Map 1-4 and Numpad 1-4
                int index = -1;
                if (k >= KeyEvent.VK_1 && k <= KeyEvent.VK_4) index = k - KeyEvent.VK_1;
                if (k >= KeyEvent.VK_NUMPAD1 && k <= KeyEvent.VK_NUMPAD4) index = k - KeyEvent.VK_NUMPAD1;
                
                if (index >= 0 && index < keys.size()) {
                    handlePress(keys.get(index));
                }
            }
        });
    }

    private void handlePress(Key key) {
        if (key.correct) return; // Already pressed correctly

        if (key.symbol.equals(solution.get(currentStep))) {
            key.correct = true;
            currentStep++;
            if (currentStep >= solution.size()) {
                solved = true;
                bomb.checkDefused();
            }
        } else {
            bomb.addStrike();
            key.pressed = true; // Flash red
            Timer t = new Timer(500, e -> {
                key.pressed = false;
                panel.repaint();
            });
            t.setRepeats(false);
            t.start();
        }
        panel.repaint();
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public boolean isSolved() {
        return solved;
    }

    @Override
    public void onStrike() {}

    @Override
    public void onSolve() {}

    @Override
    public String getName() {
        return game.Localization.get("MOD_KEYPAD");
    }

    private class Key {
        String symbol;
        Rectangle bounds;
        boolean correct = false;
        boolean pressed = false;

        Key(String s) {
            this.symbol = s;
        }
    }
}