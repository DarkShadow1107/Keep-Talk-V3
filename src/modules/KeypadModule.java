package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeypadModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<Key> keys;
    private List<String> sortedSymbols;
    private int currentStage = 0;

    // Simplified symbol sets (using Unicode characters)
    private static final String[][] COLUMNS = {
        {"Ϙ", "Ѧ", "ƛ", "Ϟ", "Ѭ", "ϗ", "Ͽ"},
        {"Ӭ", "Ϙ", "Ͽ", "Ҩ", "☆", "ϗ", "¿"},
        {"©", "Ѽ", "Ҩ", "Ж", "R", "ƛ", "☆"},
        {"б", "¶", "b", "Ѭ", "Ж", "¿", "☺"}
    };

    private class Key {
        String symbol;
        boolean pressed;
        boolean correct;
        Rectangle bounds;

        Key(String symbol) {
            this.symbol = symbol;
            this.pressed = false;
            this.correct = false;
        }
    }

    public KeypadModule(Bomb bomb) {
        this.bomb = bomb;
        this.keys = new ArrayList<>();
        generatePuzzle();
        setupUI();
    }

    private void generatePuzzle() {
        // Pick a random column
        int colIndex = (int) (Math.random() * COLUMNS.length);
        String[] column = COLUMNS[colIndex];

        // Pick 4 random symbols from that column
        List<String> colList = new ArrayList<>();
        Collections.addAll(colList, column);
        Collections.shuffle(colList);
        
        List<String> selectedSymbols = new ArrayList<>(colList.subList(0, 4));
        
        for (String sym : selectedSymbols) {
            keys.add(new Key(sym));
        }
        
        // The correct order is the order they appear in the original column
        sortedSymbols = new ArrayList<>();
        for (String sym : column) {
            if (selectedSymbols.contains(sym)) {
                sortedSymbols.add(sym);
            }
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
                }
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(200, 200));
        Theme.applyCursor(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (solved) return;
                
                for (Key key : keys) {
                    if (key.bounds.contains(e.getPoint()) && !key.correct) {
                        handlePress(key);
                        panel.repaint();
                        break;
                    }
                }
            }
        });
    }

    private void handlePress(Key key) {
        String expected = sortedSymbols.get(currentStage);
        if (key.symbol.equals(expected)) {
            key.correct = true;
            currentStage++;
            if (currentStage >= 4) {
                solved = true;
                bomb.checkDefused();
            }
        } else {
            bomb.addStrike();
            // Flash red briefly?
            key.pressed = true;
            Timer t = new Timer(500, e -> {
                key.pressed = false;
                panel.repaint();
            });
            t.setRepeats(false);
            t.start();
        }
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
        return "Keypad";
    }
}