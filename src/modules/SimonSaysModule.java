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

/**
 * Modulul "Simon Spune" (Simon Says).
 * Jucătorul trebuie să apese o secvență de butoane colorate care luminează.
 * Regulile de răspuns se schimbă în funcție de numărul de greșeli (strikes) 
 * și de prezența unei vocale în numărul de serie.
 */
public class SimonSaysModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<Integer> sequence;
    private List<Integer> inputSequence;
    private int stage = 0;
    private Color[] baseColors = {
        new Color(100, 0, 0),   // Roșu
        new Color(0, 0, 100),   // Albastru
        new Color(0, 100, 0),   // Verde
        new Color(100, 100, 0)  // Galben
    };
    private Color[] litColors = {
        new Color(255, 50, 50),
        new Color(50, 50, 255),
        new Color(50, 255, 50),
        new Color(255, 255, 50)
    };
    private boolean[] litState = new boolean[4];
    private Timer flashTimer;
    private int pressedButton = -1;

    public SimonSaysModule(Bomb bomb) {
        this.bomb = bomb;
        sequence = new ArrayList<>();
        inputSequence = new ArrayList<>();
        
        setupUI();
        addToSequence();
        startFlashing();
    }

    /**
     * Configurează interfața grafică cu cele 4 butoane colorate sub formă de cadrane.
     */
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

                // Desenare cele 4 butoane
                drawButton(g2, 0, cx - size/2 - gap/2, cy - size/2 - gap/2, size/2, size/2, 90);
                drawButton(g2, 1, cx + gap/2, cy - size/2 - gap/2, size/2, size/2, 0);
                drawButton(g2, 3, cx - size/2 - gap/2, cy + gap/2, size/2, size/2, 180);
                drawButton(g2, 2, cx + gap/2, cy + gap/2, size/2, size/2, 270);
                
                // Capacul central (decorativ)
                g2.setColor(new Color(20, 20, 20));
                g2.fillOval(cx - 25, cy - 25, 50, 50);
                g2.setColor(new Color(50, 50, 50));
                g2.drawOval(cx - 25, cy - 25, 50, 50);
                
                // Sigla centrală
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

    /**
     * Randează un buton individual cu efect de plastic și luciu.
     */
    private void drawButton(Graphics2D g2, int index, int x, int y, int w, int h, int startAngle) {
        Color c = litState[index] ? litColors[index] : baseColors[index];
        
        g2.setColor(c);
        g2.fillRoundRect(x, y, w, h, 20, 20);
        
        // Efect de strălucire/reflexie
        GradientPaint gp = new GradientPaint(x, y, new Color(255, 255, 255, 100), x, y + h/2, new Color(255, 255, 255, 0));
        g2.setPaint(gp);
        g2.fillRoundRect(x, y, w, h/2, 20, 20);
        
        // Contur buton
        g2.setColor(c.darker());
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, w, h, 20, 20);
    }

    /**
     * Identifică indexul butonului dintr-o anumită poziție (x, y).
     */
    private int getButtonAt(int x, int y) {
        int cx = panel.getWidth() / 2;
        int cy = panel.getHeight() / 2 + 10;
        int size = 140;
        int gap = 10;
        int half = size/2;
        
        // Sus-Stânga (Roșu - 0)
        if (x >= cx - half - gap/2 && x <= cx - gap/2 && y >= cy - half - gap/2 && y <= cy - gap/2) return 0;
        // Sus-Dreapta (Albastru - 1)
        if (x >= cx + gap/2 && x <= cx + half + gap/2 && y >= cy - half - gap/2 && y <= cy - gap/2) return 1;
        // Jos-Stânga (Galben - 3)
        if (x >= cx - half - gap/2 && x <= cx - gap/2 && y >= cy + gap/2 && y <= cy + half + gap/2) return 3;
        // Jos-Dreapta (Verde - 2)
        if (x >= cx + gap/2 && x <= cx + half + gap/2 && y >= cy + gap/2 && y <= cy + half + gap/2) return 2;
        
        return -1;
    }

    /**
     * Adaugă o culoare aleatorie la secvența curentă.
     */
    private void addToSequence() {
        Random rand = new Random();
        sequence.add(rand.nextInt(4));
    }

    /**
     * Pornește animația de iluminare a secvenței ce trebuie reținută.
     */
    private void startFlashing() {
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

                // Stingem toate butoanele
                for (int i = 0; i < 4; i++) litState[i] = false;

                if (flashIndex[0] >= sequence.size()) {
                    flashIndex[0] = 0;
                    panel.repaint();
                    return;
                }

                if (on) {
                    // Aprindem butonul corespunzător din secvență
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

    /**
     * Procesează apăsarea unui buton de către jucător și verifică corectitudinea.
     */
    private void handleInput(int index) {
        if (solved) return;
        
        int expectedFlash = sequence.get(inputSequence.size());
        int strikes = bomb.getStrikes();
        int requiredInput = getCorrectButton(expectedFlash, strikes);

        if (index == requiredInput) {
            inputSequence.add(index);
            if (inputSequence.size() == sequence.size()) {
                // Secvență completată corect pentru acest stagiu
                stage++;
                inputSequence.clear();
                if (stage >= 3) {
                    // Modul rezolvat după 3 stagii
                    solved = true;
                    flashTimer.stop();
                    for(int i=0; i<4; i++) litState[i] = true; // Toate aprinse la victorie
                    panel.repaint();
                    bomb.checkDefused();
                } else {
                    addToSequence();
                    // Pornește din nou iluminarea după o scurtă pauză
                    Timer t = new Timer(1000, e -> startFlashing());
                    t.setRepeats(false);
                    t.start();
                }
            }
        } else {
            // Greșeală (Strike) și resetarea progresului curent
            bomb.addStrike();
            inputSequence.clear();
            startFlashing();
        }
    }

    /**
     * Determină butonul corect ce trebuie apăsat conform tabelului din manual.
     * Regulile diferă dacă seria conține o vocală și în funcție de numărul de greșeli.
     */
    private int getCorrectButton(int flashIndex, int strikes) {
        boolean hasVowel = hasVowel(bomb.getSerialNumber());
        // 0:Roșu, 1:Albastru, 2:Verde, 3:Galben
        
        if (hasVowel) {
            switch (strikes) {
                case 0 -> {
                    switch (flashIndex) {
                        case 0 -> { return 1; } // Roșu -> Albastru
                        case 1 -> { return 0; } // Albastru -> Roșu
                        case 2 -> { return 3; } // Verde -> Galben
                        case 3 -> { return 2; } // Galben -> Verde
                    }
                }
                case 1 -> {
                    switch (flashIndex) {
                        case 0 -> { return 3; } // Roșu -> Galben
                        case 1 -> { return 2; } // Albastru -> Verde
                        case 2 -> { return 1; } // Verde -> Albastru
                        case 3 -> { return 0; } // Galben -> Roșu
                    }
                }
                default -> { // 2+ strikes
                    switch (flashIndex) {
                        case 0 -> { return 2; } // Roșu -> Verde
                        case 1 -> { return 0; } // Albastru -> Roșu
                        case 2 -> { return 3; } // Verde -> Galben
                        case 3 -> { return 1; } // Galben -> Albastru
                    }
                }
            }
        } else { // Nu are vocală în serie
            switch (strikes) {
                case 0 -> {
                    switch (flashIndex) {
                        case 0 -> { return 1; } // Roșu -> Albastru
                        case 1 -> { return 3; } // Albastru -> Galben
                        case 2 -> { return 2; } // Verde -> Verde
                        case 3 -> { return 0; } // Galben -> Roșu
                    }
                }
                case 1 -> {
                    switch (flashIndex) {
                        case 0 -> { return 0; } // Roșu -> Roșu
                        case 1 -> { return 1; } // Albastru -> Albastru
                        case 2 -> { return 3; } // Verde -> Galben
                        case 3 -> { return 2; } // Galben -> Verde
                    }
                }
                default -> { // 2+ strikes
                    switch (flashIndex) {
                        case 0 -> { return 3; } // Roșu -> Galben
                        case 1 -> { return 2; } // Albastru -> Verde
                        case 2 -> { return 1; } // Verde -> Albastru
                        case 3 -> { return 0; } // Galben -> Roșu
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Verifică dacă un șir de caractere conține vocale (A, E, I, O, U).
     */
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
