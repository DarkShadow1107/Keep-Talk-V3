package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class BinaryModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private int targetValue;
    private boolean[] bits = new boolean[5]; // 5 bits = 0-31
    private Rectangle[] bitRects = new Rectangle[5];
    private Rectangle submitBtnRect;
    private boolean submitPressed = false;

    public BinaryModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
    }

    private void generatePuzzle() {
        Random rand = new Random();
        targetValue = rand.nextInt(32); // 0 to 31
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

                // Draw Target Number Display
                g2.setColor(new Color(20, 20, 20));
                g2.fillRoundRect(20, 40, getWidth() - 40, 50, 10, 10);
                g2.setColor(new Color(50, 50, 50));
                g2.drawRoundRect(20, 40, getWidth() - 40, 50, 10, 10);

                g2.setFont(Theme.FONT_DIGITAL.deriveFont(40f));
                g2.setColor(Theme.ACCENT_BLUE);
                String text = String.valueOf(targetValue);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(text)) / 2;
                int ty = 40 + (50 + fm.getAscent()) / 2 - 5;
                g2.drawString(text, tx, ty);

                // Draw Bits (LEDs/Switches)
                int startX = (getWidth() - (5 * 35)) / 2;
                int bitY = 110;
                
                for (int i = 0; i < 5; i++) {
                    int x = startX + i * 35;
                    bitRects[i] = new Rectangle(x, bitY, 25, 40);
                    
                    // Switch body
                    g2.setColor(new Color(40, 40, 40));
                    g2.fillRoundRect(x, bitY, 25, 40, 5, 5);
                    
                    // LED/Light
                    if (bits[4 - i]) { // MSB is left (index 0 visually, but bit 4 logic?) 
                        // Actually let's make index 0 be 16 (2^4), index 4 be 1 (2^0)
                        // So bits array: [16, 8, 4, 2, 1]
                        g2.setColor(Theme.ACCENT_GREEN);
                        g2.fillRoundRect(x + 2, bitY + 2, 21, 18, 3, 3);
                        
                        // Glow
                        g2.setColor(new Color(46, 204, 113, 100));
                        g2.fillRoundRect(x - 2, bitY - 2, 29, 24, 8, 8);
                    } else {
                        g2.setColor(new Color(20, 20, 20));
                        g2.fillRoundRect(x + 2, bitY + 20, 21, 18, 3, 3);
                    }
                    
                    // Label (power of 2)
                    g2.setColor(Color.GRAY);
                    g2.setFont(Theme.FONT_MONO.deriveFont(10f));
                    String label = String.valueOf((int)Math.pow(2, 4-i));
                    g2.drawString(label, x + 8, bitY + 52);
                }

                // Submit Button
                submitBtnRect = new Rectangle((getWidth() - 100) / 2, 170, 100, 30);
                g2.setColor(submitPressed ? Theme.ACCENT_BLUE.darker() : Theme.ACCENT_BLUE);
                g2.fillRoundRect(submitBtnRect.x, submitBtnRect.y, submitBtnRect.width, submitBtnRect.height, 10, 10);
                
                g2.setColor(Color.WHITE);
                g2.setFont(Theme.FONT_BOLD.deriveFont(14f));
                String subText = "SUBMIT";
                fm = g2.getFontMetrics();
                g2.drawString(subText, submitBtnRect.x + (submitBtnRect.width - fm.stringWidth(subText))/2, submitBtnRect.y + 20);
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(200, 240));
        Theme.applyCursor(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (solved) return;
                
                // Check bits
                for (int i = 0; i < 5; i++) {
                    if (bitRects[i].contains(e.getPoint())) {
                        bits[4-i] = !bits[4-i]; // Toggle bit
                        panel.repaint();
                        return;
                    }
                }
                
                // Check submit
                if (submitBtnRect.contains(e.getPoint())) {
                    submitPressed = true;
                    panel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (solved) return;
                
                if (submitPressed) {
                    submitPressed = false;
                    if (submitBtnRect.contains(e.getPoint())) {
                        checkSolution();
                    }
                    panel.repaint();
                }
            }
        });
    }

    private void checkSolution() {
        int currentValue = 0;
        for (int i = 0; i < 5; i++) {
            if (bits[i]) {
                currentValue += Math.pow(2, i);
            }
        }

        if (currentValue == targetValue) {
            solved = true;
            bomb.checkDefused();
        } else {
            bomb.addStrike();
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
        return game.Localization.get("MOD_BINARY");
    }
}
