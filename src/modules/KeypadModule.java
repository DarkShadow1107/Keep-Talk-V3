package modules;

import game.Bomb;
import game.Theme;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class KeypadModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<Key> keys = new ArrayList<>();
    private List<String> solution = new ArrayList<>();
    private int currentStep = 0;

    // Official KTANE Keypad Columns
    public static final String[][] COLUMNS = {
        {"\u03D8", "\u0466", "\u03BB", "\u03DE", "\u046C", "\u03D7", "\u03F6"}, // Ϙ, Ѧ, λ, Ϟ, Ѭ, ϗ, ϶
        {"\u04EC", "\u03D8", "\u03F6", "\u0480", "\u2606", "\u03D7", "\u00BF"}, // Ӭ, Ϙ, ϶, Ҁ, ☆, ϗ, ¿
        {"\u00A9", "\u047C", "\u0480", "\u0496", "\u0506", "\u03BB", "\u2606"}, // ©, Ѽ, Ҁ, Җ, Ԇ, λ, ☆
        {"\u03EC", "\u00B6", "\u0462", "\u046C", "\u0496", "\u00BF", "\u263A"}, // Ϭ, ¶, Ѣ, Ѭ, Җ, ¿, ☺
        {"\u03A8", "\u263A", "\u0462", "\u03FE", "\u00B6", "\u046E", "\u2605"}, // Ψ, ☺, Ѣ, Ͼ, ¶, Ѯ, ★
        {"\u03EC", "\u04EC", "\u0482", "\u00E6", "\u03A8", "\u048A", "\u03A9"}  // Ϭ, Ӭ, ҂, æ, Ψ, Ҋ, Ω
    };

    public KeypadModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
    }

    private void generatePuzzle() {
        Random rand = new Random();
        
        // 1. Pick a random column
        String[] column = COLUMNS[rand.nextInt(COLUMNS.length)];
        
        // 2. Pick 4 random symbols from that column
        List<String> chosen = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        while (indices.size() < 4) {
            int idx = rand.nextInt(column.length);
            if (!indices.contains(idx)) {
                indices.add(idx);
            }
        }
        
        // 3. The solution is these 4 symbols, ordered as they appear in the column (by their index)
        indices.sort(Integer::compareTo);
        solution = new ArrayList<>();
        for (int idx : indices) {
            String s = column[idx];
            solution.add(s);
            chosen.add(s); // chosen list for shuffling
        }
        
        // 4. Shuffle keys for display
        keys.clear();
        List<String> displayOrder = new ArrayList<>(chosen);
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
                    g2.setFont(Theme.FONT_SYMBOL);
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
        panel.setPreferredSize(new Dimension(180, 180));
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
