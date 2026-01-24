package modules;

import game.Bomb;
import game.Theme;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;

/**
 * Modulul "Binar" (Binary).
 * Jucătorul vede un număr zecimal pe un afișaj digital și trebuie să îl 
 * convertească în binar folosind 5 switch-uri (biți).
 * Fiecare bit reprezintă o putere a lui 2 (16, 8, 4, 2, 1).
 */
public class BinaryModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private int targetValue;
    private boolean[] bits = new boolean[5]; // 5 biți = valori între 0 și 31
    private Rectangle[] bitRects = new Rectangle[5];
    private Rectangle submitBtnRect;
    private boolean submitPressed = false;

    public BinaryModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
    }

    /**
     * Generează o valoare țintă aleatorie între 0 și 31.
     */
    private void generatePuzzle() {
        Random rand = new Random();
        targetValue = rand.nextInt(32); // 0 la 31
    }

    /**
     * Configurează interfața grafică a modulului.
     */
    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Desenare afișaj număr țintă (Target Number)
                g2.setColor(new Color(20, 20, 20));
                g2.fillRoundRect(20, 30, getWidth() - 40, 50, 10, 10);
                g2.setColor(new Color(50, 50, 50));
                g2.drawRoundRect(20, 30, getWidth() - 40, 50, 10, 10);

                g2.setFont(Theme.FONT_DIGITAL.deriveFont(40f));
                g2.setColor(Theme.ACCENT_BLUE);
                String text = String.valueOf(targetValue);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(text)) / 2;
                int ty = 30 + (50 + fm.getAscent()) / 2 - 7;
                g2.drawString(text, tx, ty);

                // Desenare biți (LED-uri/Switch-uri)
                int startX = (getWidth() - (5 * 35)) / 2;
                int bitY = 90;
                
                for (int i = 0; i < 5; i++) {
                    int x = startX + i * 35;
                    bitRects[i] = new Rectangle(x, bitY, 25, 40);
                    
                    // Corpul comutatorului
                    g2.setColor(new Color(40, 40, 40));
                    g2.fillRoundRect(x, bitY, 25, 40, 5, 5);
                    
                    // LED aprins dacă bitul este activ
                    if (bits[4 - i]) { 
                        // Bitul cel mai semnificativ (MSB) este în stânga
                        g2.setColor(Theme.ACCENT_GREEN);
                        g2.fillRoundRect(x + 2, bitY + 2, 21, 18, 3, 3);
                        
                        // Efect de strălucire (glow)
                        g2.setColor(new Color(46, 204, 113, 100));
                        g2.fillRoundRect(x - 2, bitY - 2, 29, 24, 8, 8);
                    } else {
                        // LED stins
                        g2.setColor(new Color(20, 20, 20));
                        g2.fillRoundRect(x + 2, bitY + 20, 21, 18, 3, 3);
                    }
                    
                    // Etichetă (puterea lui 2 corespunzătoare)
                    g2.setColor(Color.GRAY);
                    g2.setFont(Theme.FONT_MONO.deriveFont(10f));
                    String label = String.valueOf((int)Math.pow(2, 4-i));
                    g2.drawString(label, x + 8, bitY + 52);
                }

                // Butonul Submit
                submitBtnRect = new Rectangle((getWidth() - 100) / 2, 145, 100, 30);
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
        panel.setPreferredSize(new Dimension(180, 180));
        Theme.applyCursor(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (solved) return;
                
                // Verificare click pe biți
                for (int i = 0; i < 5; i++) {
                    if (bitRects[i].contains(e.getPoint())) {
                        bits[4-i] = !bits[4-i]; // Schimbă starea bitului
                        panel.repaint();
                        return;
                    }
                }
                
                // Verificare click pe butonul de trimis
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

    /**
     * Calculează valoarea curentă a biților și o compară cu ținta.
     */
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
            bomb.addStrike(); // Greșeală dacă valoarea binară e incorectă
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
