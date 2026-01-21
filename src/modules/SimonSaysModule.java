package modules;

import game.Bomb;
import game.Theme;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class SimonSaysModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<Integer> sequence;
    private List<Integer> inputSequence;
    private int stage = 0;
    private Color[] baseColors = {
        new Color(100, 0, 0),   // Red
        new Color(0, 0, 100),   // Blue
        new Color(0, 100, 0),   // Green
        new Color(100, 100, 0)  // Yellow
    };
    private Color[] litColors = {
        new Color(255, 50, 50),
        new Color(50, 50, 255),
        new Color(50, 255, 50),
        new Color(255, 255, 50)
    };
    private boolean[] litState = new boolean[4];
    private Timer flashTimer;
    private boolean showingSequence = false;
    private int pressedButton = -1;

    public SimonSaysModule(Bomb bomb) {
        this.bomb = bomb;
        sequence = new ArrayList<>();
        inputSequence = new ArrayList<>();
        
        setupUI();
        addToSequence();
        startFlashing();
    }

    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2 + 10;
                int size = 140;
                int gap = 10;

                // Draw 4 quadrants
                drawButton(g2, 0, cx - size/2 - gap/2, cy - size/2 - gap/2, size/2, size/2, 90);
                drawButton(g2, 1, cx + gap/2, cy - size/2 - gap/2, size/2, size/2, 0);
                drawButton(g2, 3, cx - size/2 - gap/2, cy + gap/2, size/2, size/2, 180);
                drawButton(g2, 2, cx + gap/2, cy + gap/2, size/2, size/2, 270);
                
                // Center cap
                g2.setColor(new Color(20, 20, 20));
                g2.fillOval(cx - 25, cy - 25, 50, 50);
                g2.setColor(new Color(50, 50, 50));
                g2.drawOval(cx - 25, cy - 25, 50, 50);
                
                // Logo
                g2.setColor(Color.WHITE);
                g2.setFont(Theme.FONT_BOLD.deriveFont(10f));
                int w = g2.getFontMetrics().stringWidth("SIMON");
                g2.drawString("SIMON", cx - w/2, cy + 4);
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(180, 180));
        Theme.applyCursor(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (solved) return;
                int btn = getButtonAt(e.getX(), e.getY());
                if (btn != -1) {
                    pressedButton = btn;
                    litState[btn] = true;
                    panel.repaint();
                    handleInput(btn);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (pressedButton != -1) {
                    litState[pressedButton] = false;
                    pressedButton = -1;
                    panel.repaint();
                }
            }
        });
    }

    private void drawButton(Graphics2D g2, int index, int x, int y, int w, int h, int startAngle) {
        Color c = litState[index] ? litColors[index] : baseColors[index];
        
        // We draw a rectangle but clip it or just draw a filled arc/rect?
        // Actually, Simon buttons are usually sectors of a circle.
        // Let's draw rounded rectangles for a modern look or arcs for classic.
        // Let's do rounded rects arranged in a grid for simplicity and robustness, 
        // or rotated arcs.
        
        // Let's stick to the grid layout but make them look like plastic pads.
        g2.setColor(c);
        g2.fillRoundRect(x, y, w, h, 20, 20);
        
        // Shine/Gloss
        GradientPaint gp = new GradientPaint(x, y, new Color(255, 255, 255, 100), x, y + h/2, new Color(255, 255, 255, 0));
        g2.setPaint(gp);
        g2.fillRoundRect(x, y, w, h/2, 20, 20);
        
        // Border
        g2.setColor(c.darker());
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, w, h, 20, 20);
    }

    private int getButtonAt(int x, int y) {
        int cx = panel.getWidth() / 2;
        int cy = panel.getHeight() / 2 + 10;
        int size = 140;
        int gap = 10;
        int half = size/2;
        
        // Top Left (Red - 0)
        if (x >= cx - half - gap/2 && x <= cx - gap/2 && y >= cy - half - gap/2 && y <= cy - gap/2) return 0;
        // Top Right (Blue - 1)
        if (x >= cx + gap/2 && x <= cx + half + gap/2 && y >= cy - half - gap/2 && y <= cy - gap/2) return 1;
        // Bottom Left (Yellow - 3)
        if (x >= cx - half - gap/2 && x <= cx - gap/2 && y >= cy + gap/2 && y <= cy + half + gap/2) return 3;
        // Bottom Right (Green - 2)
        if (x >= cx + gap/2 && x <= cx + half + gap/2 && y >= cy + gap/2 && y <= cy + half + gap/2) return 2;
        
        return -1;
    }

    private void addToSequence() {
        Random rand = new Random();
        sequence.add(rand.nextInt(4));
    }

    private void startFlashing() {
        showingSequence = true;
        final int[] flashIndex = {0};
        
        if (flashTimer != null && flashTimer.isRunning()) flashTimer.stop();

        flashTimer = new Timer(800, new ActionListener() {
            boolean on = true;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (solved) {
                    ((Timer)e.getSource()).stop();
                    return;
                }

                // Reset all
                for (int i = 0; i < 4; i++) litState[i] = false;

                if (flashIndex[0] >= sequence.size()) {
                    flashIndex[0] = 0;
                    panel.repaint();
                    return;
                }

                if (on) {
                    int btnIndex = sequence.get(flashIndex[0]);
                    litState[btnIndex] = true;
                    on = false;
                } else {
                    on = true;
                    flashIndex[0]++;
                }
                panel.repaint();
            }
        });
        flashTimer.start();
    }

    private void handleInput(int index) {
        if (solved) return;
        
        // Visual feedback is handled by mouse press/release
        
        int expectedFlash = sequence.get(inputSequence.size());
        int strikes = bomb.getStrikes();
        int requiredInput = getCorrectButton(expectedFlash, strikes);

        if (index == requiredInput) {
            inputSequence.add(index);
            if (inputSequence.size() == sequence.size()) {
                stage++;
                inputSequence.clear();
                if (stage >= 3) {
                    solved = true;
                    flashTimer.stop();
                    for(int i=0; i<4; i++) litState[i] = true; // All lit on win
                    panel.repaint();
                    bomb.checkDefused();
                } else {
                    addToSequence();
                    // Restart flashing after a short delay
                    Timer t = new Timer(1000, e -> startFlashing());
                    t.setRepeats(false);
                    t.start();
                }
            }
        } else {
            bomb.addStrike();
            inputSequence.clear();
            startFlashing();
        }
    }

    private int getCorrectButton(int flashIndex, int strikes) {
        boolean hasVowel = hasVowel(bomb.getSerialNumber());
        // 0:Red, 1:Blue, 2:Green, 3:Yellow
        
        if (hasVowel) {
            if (strikes == 0) {
                switch (flashIndex) {
                    case 0: return 1; // Red -> Blue
                    case 1: return 0; // Blue -> Red
                    case 2: return 3; // Green -> Yellow
                    case 3: return 2; // Yellow -> Green
                }
            } else if (strikes == 1) {
                switch (flashIndex) {
                    case 0: return 3; // Red -> Yellow
                    case 1: return 2; // Blue -> Green
                    case 2: return 1; // Green -> Blue
                    case 3: return 0; // Yellow -> Red
                }
            } else { // 2+ strikes
                switch (flashIndex) {
                    case 0: return 2; // Red -> Green
                    case 1: return 0; // Blue -> Red
                    case 2: return 3; // Green -> Yellow
                    case 3: return 1; // Yellow -> Blue
                }
            }
        } else { // No Vowel
            if (strikes == 0) {
                switch (flashIndex) {
                    case 0: return 1; // Red -> Blue
                    case 1: return 3; // Blue -> Yellow
                    case 2: return 2; // Green -> Green
                    case 3: return 0; // Yellow -> Red
                }
            } else if (strikes == 1) {
                switch (flashIndex) {
                    case 0: return 0; // Red -> Red
                    case 1: return 1; // Blue -> Blue
                    case 2: return 3; // Green -> Yellow
                    case 3: return 2; // Yellow -> Green
                }
            } else { // 2+ strikes
                switch (flashIndex) {
                    case 0: return 3; // Red -> Yellow
                    case 1: return 2; // Blue -> Green
                    case 2: return 1; // Green -> Blue
                    case 3: return 0; // Yellow -> Red
                }
            }
        }
        return flashIndex;
    }

    private boolean hasVowel(String serial) {
        String vowels = "AEIOU";
        for (char c : serial.toUpperCase().toCharArray()) {
            if (vowels.indexOf(c) >= 0) return true;
        }
        return false;
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
        return game.Localization.get("MOD_SIMON");
    }
}
