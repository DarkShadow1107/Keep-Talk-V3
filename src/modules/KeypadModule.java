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
    private List<String> solution = new ArrayList<>(); // Ordinea corectă a simbolurilor
    private int currentStep = 0; // Pasul curent în secvență

    // Coloanele oficiale de simboluri din jocul KTANE
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

    /**
     * Generează puzzle-ul alegând o coloană și 4 simboluri din ea, 
     * păstrând ordinea lor verticală pentru soluție.
     */
    private void generatePuzzle() {
        Random rand = new Random();
        
        // 1. Alege o coloană aleatorie
        String[] column = COLUMNS[rand.nextInt(COLUMNS.length)];
        
        // 2. Alege 4 simboluri unice din acea coloană
        List<String> chosen = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        while (indices.size() < 4) {
            int idx = rand.nextInt(column.length);
            if (!indices.contains(idx)) {
                indices.add(idx);
            }
        }
        
        // 3. Soluția este formată din aceste 4 simboluri, ordonate după cum apar în coloană (după index)
        indices.sort(Integer::compareTo);
        solution = new ArrayList<>();
        for (int idx : indices) {
            String s = column[idx];
            solution.add(s);
            chosen.add(s); 
        }
        
        // 4. Amestecă tastele pentru afișarea pe modul (pentru a nu fi deja în ordine)
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

    /**
     * Configurează UI-ul modulului cu cele 4 butoane cu simboluri.
     */
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

                    // Randare Fundal Tastă
                    if (key.correct) {
                        g2.setColor(new Color(40, 100, 40)); // Verde dacă e apăsat corect
                    } else if (key.pressed) {
                        g2.setColor(new Color(100, 40, 40)); // Roșu scurt la eroare
                    } else {
                        g2.setColor(new Color(200, 190, 180)); // Aspect metalic/piatră
                    }
                    
                    RoundRectangle2D rect = new RoundRectangle2D.Float(x, y, width, height, 10, 10);
                    g2.fill(rect);

                    // Contur și umbră taste
                    g2.setStroke(new BasicStroke(3));
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.draw(rect);
                    
                    // Randare Simbol Ocult
                    g2.setFont(Theme.FONT_SYMBOL);
                    FontMetrics fm = g2.getFontMetrics();
                    int textX = x + (width - fm.stringWidth(key.symbol)) / 2;
                    int textY = y + (height + fm.getAscent()) / 2 - 5;
                    
                    g2.setColor(new Color(50, 40, 30)); // Look gravat închis
                    g2.drawString(key.symbol, textX, textY);
                    
                    // Reflexie pentru efectul gravat
                    g2.setColor(new Color(255, 255, 255, 100));
                    g2.drawString(key.symbol, textX + 1, textY + 1);
                    
                    // Afișare număr pentru suport tastatură
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.setFont(Theme.FONT_MONO.deriveFont(12f));
                    g2.drawString(String.valueOf(i + 1), x + 5, y + 15);
                }
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(180, 180));
        Theme.applyCursor(panel);
        panel.setFocusable(true);

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
                int index = -1;
                if (k >= KeyEvent.VK_1 && k <= KeyEvent.VK_4) index = k - KeyEvent.VK_1;
                if (k >= KeyEvent.VK_NUMPAD1 && k <= KeyEvent.VK_NUMPAD4) index = k - KeyEvent.VK_NUMPAD1;
                
                if (index >= 0 && index < keys.size()) {
                    handlePress(keys.get(index));
                }
            }
        });
    }

    /**
     * Procesează apăsarea unei taste și verifică dacă este în ordinea corectă.
     */
    private void handlePress(Key key) {
        if (key.correct) return; // Deja apăsată corect

        // Verificăm dacă simbolul apăsat corespunde cu pasul curent din soluție
        if (key.symbol.equals(solution.get(currentStep))) {
            key.correct = true;
            currentStep++;
            if (currentStep >= solution.size()) {
                solved = true;
                bomb.checkDefused();
            }
        } else {
            // Eroare (Strike) dacă ordinea este greșită
            bomb.addStrike();
            key.pressed = true; // Iluminare roșie temporară
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
