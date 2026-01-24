package modules;

import game.Bomb;
import game.Theme;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.*;

/**
 * Modulul "Memorie" (Memory).
 * Jucătorul trebuie să treacă prin 5 stagii de memorare a pozițiilor și etichetelor butoanelor.
 * Regulile se schimbă la fiecare stagiu și depind de istoricul apăsărilor anterioare.
 */
public class MemoryModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private int stage = 1;
    private int displayVal;
    private List<Integer> buttonLabels = new ArrayList<>();
    private int[] correctLabels = new int[5];
    private int[] correctPositions = new int[5];
    private int expectedPos;
    private int expectedLbl;
    
    private Rectangle[] buttonRects = new Rectangle[4];
    private boolean[] buttonPressed = new boolean[4];

    public MemoryModule(Bomb bomb) {
        this.bomb = bomb;
        setupUI();
        startStage();
    }

    /**
     * Configurează interfața grafică a modulului de memorie.
     */
    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Desenare Afișaj Principal (Numărul mare de sus)
                int dispW = 80;
                int dispH = 60;
                int dispX = (getWidth() - dispW) / 2;
                int dispY = 30;

                g2.setColor(Color.BLACK);
                g2.fillRoundRect(dispX, dispY, dispW, dispH, 10, 10);
                g2.setColor(new Color(50, 50, 50));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(dispX, dispY, dispW, dispH, 10, 10);

                g2.setFont(Theme.FONT_DIGITAL.deriveFont(48f));
                FontMetrics fm = g2.getFontMetrics();
                String text = String.valueOf(displayVal);
                int textX = dispX + (dispW - fm.stringWidth(text)) / 2;
                int textY = dispY + (dispH + fm.getAscent()) / 2 - 5;
                g2.setColor(Theme.TEXT_DIGITAL);
                g2.drawString(text, textX, textY);

                // Desenare Indicatori de Stagiu (cele 5 puncte verzi)
                int stageY = dispY + dispH + 15;
                int dotSize = 10;
                int dotGap = 10;
                int totalDotW = (dotSize * 5) + (dotGap * 4);
                int startDotX = (getWidth() - totalDotW) / 2;

                for (int i = 0; i < 5; i++) {
                    g2.setColor(i < stage ? Theme.ACCENT_GREEN : new Color(50, 50, 50));
                    if (solved) g2.setColor(Theme.ACCENT_GREEN);
                    g2.fillOval(startDotX + i * (dotSize + dotGap), stageY, dotSize, dotSize);
                }

                // Desenare cele 4 Butoane cu cifre
                int btnW = 40;
                int btnH = 50;
                int btnGap = 10;
                int startBtnX = (getWidth() - (btnW * 4 + btnGap * 3)) / 2;
                int btnY = getHeight() - btnH - 20;

                for (int i = 0; i < 4; i++) {
                    int x = startBtnX + i * (btnW + btnGap);
                    buttonRects[i] = new Rectangle(x, btnY, btnW, btnH);
                    
                    // Corpul butonului cu efect 3D simplu
                    if (buttonPressed[i]) {
                        g2.setColor(new Color(180, 180, 180));
                        g2.fillRoundRect(x, btnY + 5, btnW, btnH - 5, 5, 5);
                    } else {
                        g2.setColor(new Color(200, 200, 200));
                        g2.fillRoundRect(x, btnY, btnW, btnH, 5, 5);
                        // Umbra butonului
                        g2.setColor(new Color(150, 150, 150));
                        g2.fillRoundRect(x, btnY + btnH - 5, btnW, 5, 5, 5);
                    }
                    
                    // Eticheta butonului (cifra)
                    g2.setColor(Color.BLACK);
                    g2.setFont(Theme.FONT_BOLD.deriveFont(24f));
                    String lbl = String.valueOf(buttonLabels.get(i));
                    fm = g2.getFontMetrics();
                    int lblX = x + (btnW - fm.stringWidth(lbl)) / 2;
                    int lblY = btnY + (btnH + fm.getAscent()) / 2 - 5;
                    if (buttonPressed[i]) lblY += 5;
                    g2.drawString(lbl, lblX, lblY);
                }
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(180, 180));
        Theme.applyCursor(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (solved) return;
                for (int i = 0; i < 4; i++) {
                    if (buttonRects[i].contains(e.getPoint())) {
                        buttonPressed[i] = true;
                        panel.repaint();
                        handlePress(i);
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (int i = 0; i < 4; i++) buttonPressed[i] = false;
                panel.repaint();
            }
        });
    }

    /**
     * Inițializează un nou stagiu cu cifre aleatorii și determină răspunsul corect
     * bazat pe regulile complexe din manual.
     */
    private void startStage() {
        Random rand = new Random();
        displayVal = rand.nextInt(4) + 1;
        
        buttonLabels.clear();
        for (int i = 1; i <= 4; i++) buttonLabels.add(i);
        Collections.shuffle(buttonLabels);
        
        // Logica de determinare a răspunsului corect (Poziție sau Etichetă)
        int correctPos = -1;
        int correctLbl = -1;
        
        switch (stage) {
            case 1 -> {
                // Stagiu 1: bazat doar pe afișaj
                switch (displayVal) {
                    case 1, 2 -> correctPos = 1; // Poziția 2
                    case 3 -> correctPos = 2;    // Poziția 3
                    default -> correctPos = 3;   // Poziția 4
                }
            }
            case 2 -> {
                // Stagiu 2: memorează eticheta de la stagiul 1
                switch (displayVal) {
                    case 1 -> correctLbl = 4;
                    case 3 -> correctPos = 0;
                    default -> correctPos = correctPositions[0];
                }
            }
            case 3 -> {
                // Stagiu 3: memorează etichetele de la stagiile anterioare
                switch (displayVal) {
                    case 1 -> correctLbl = correctLabels[1];
                    case 2 -> correctLbl = correctLabels[0];
                    case 3 -> correctPos = 2;
                    default -> correctLbl = 4;
                }
            }
            case 4 -> {
                // Stagiu 4: bazat pe poziții anterioare
                switch (displayVal) {
                    case 1 -> correctPos = correctPositions[0];
                    case 2 -> correctPos = 0;
                    default -> correctPos = correctPositions[1];
                }
            }
            case 5 -> {
                // Stagiu 5: verificare finală bazată pe etichete
                switch (displayVal) {
                    case 1 -> correctLbl = correctLabels[0];
                    case 2 -> correctLbl = correctLabels[1];
                    case 3 -> correctLbl = correctLabels[3];
                    default -> correctLbl = correctLabels[2];
                }
            }
        }
        
        // Rezolvăm legătura dintre poziție și etichetă (identificăm butonul fizic)
        if (correctPos != -1) {
            correctLbl = buttonLabels.get(correctPos);
        } else {
            // Căutăm poziția la care se află eticheta cerută
            for(int i=0; i<4; i++) {
                if (buttonLabels.get(i) == correctLbl) {
                    correctPos = i;
                    break;
                }
            }
        }
        
        this.expectedPos = correctPos;
        this.expectedLbl = correctLbl;
        if (panel != null) panel.repaint();
    }

    /**
     * Procesează apăsarea unui buton și verifică dacă este răspunsul corect.
     * Dacă e corect, trece la stagiul următor. Dacă greșește, resetează modulul la stagiul 1.
     */
    private void handlePress(int pos) {
        if (solved) return;
        
        int pressedLbl = buttonLabels.get(pos);
        
        if (pos == expectedPos && pressedLbl == expectedLbl) {
            // Răspuns corect: memorăm poziția și eticheta
            correctPositions[stage-1] = pos;
            correctLabels[stage-1] = pressedLbl;
            
            stage++;
            if (stage > 5) {
                // Modul rezolvat după cele 5 stagii
                solved = true;
                bomb.checkDefused();
            } else {
                // Trecere la stagiul următor după o scurtă întârziere (vizibilitate click)
                Timer t = new Timer(200, e -> startStage());
                t.setRepeats(false);
                t.start();
            }
        } else {
            // Greșeală: se adaugă strike și se resetează tot progresul modului
            bomb.addStrike();
            stage = 1;
            correctPositions = new int[5];
            correctLabels = new int[5];
            Timer t = new Timer(200, e -> startStage());
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
        return game.Localization.get("MOD_MEMORY");
    }
}
