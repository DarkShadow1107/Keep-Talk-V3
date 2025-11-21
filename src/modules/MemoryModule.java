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
import java.util.Random;

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

    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw Status LED
                Theme.drawLed(g2, getWidth() - 25, 15, solved, true);

                // Draw Main Display
                int dispW = 80;
                int dispH = 60;
                int dispX = (getWidth() - dispW) / 2;
                int dispY = 40;

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

                // Draw Stage Indicators
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

                // Draw Buttons
                int btnW = 40;
                int btnH = 50;
                int btnGap = 10;
                int startBtnX = (getWidth() - (btnW * 4 + btnGap * 3)) / 2;
                int btnY = getHeight() - btnH - 20;

                for (int i = 0; i < 4; i++) {
                    int x = startBtnX + i * (btnW + btnGap);
                    buttonRects[i] = new Rectangle(x, btnY, btnW, btnH);
                    
                    // Button Body
                    if (buttonPressed[i]) {
                        g2.setColor(new Color(180, 180, 180));
                        g2.fillRoundRect(x, btnY + 5, btnW, btnH - 5, 5, 5);
                    } else {
                        g2.setColor(new Color(200, 200, 200));
                        g2.fillRoundRect(x, btnY, btnW, btnH, 5, 5);
                        // Shadow
                        g2.setColor(new Color(150, 150, 150));
                        g2.fillRoundRect(x, btnY + btnH - 5, btnW, 5, 5, 5);
                    }
                    
                    // Label
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
        panel.setPreferredSize(new Dimension(200, 200));
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

    private void startStage() {
        Random rand = new Random();
        displayVal = rand.nextInt(4) + 1;
        
        buttonLabels.clear();
        for (int i = 1; i <= 4; i++) buttonLabels.add(i);
        Collections.shuffle(buttonLabels);
        
        // Determine correct answer logic
        int correctPos = -1;
        int correctLbl = -1;
        
        switch (stage) {
            case 1:
                if (displayVal == 1) correctPos = 1; // 2nd pos
                else if (displayVal == 2) correctPos = 1;
                else if (displayVal == 3) correctPos = 2; // 3rd pos
                else correctPos = 3; // 4th pos
                break;
            case 2:
                if (displayVal == 1) correctLbl = 4;
                else if (displayVal == 2) correctPos = correctPositions[0];
                else if (displayVal == 3) correctPos = 0;
                else correctPos = correctPositions[0];
                break;
            case 3:
                if (displayVal == 1) correctLbl = correctLabels[1];
                else if (displayVal == 2) correctLbl = correctLabels[0];
                else if (displayVal == 3) correctPos = 2;
                else correctLbl = 4;
                break;
            case 4:
                if (displayVal == 1) correctPos = correctPositions[0];
                else if (displayVal == 2) correctPos = 0;
                else if (displayVal == 3) correctPos = correctPositions[1];
                else correctPos = correctPositions[1];
                break;
            case 5:
                if (displayVal == 1) correctLbl = correctLabels[0];
                else if (displayVal == 2) correctLbl = correctLabels[1];
                else if (displayVal == 3) correctLbl = correctLabels[3];
                else correctLbl = correctLabels[2];
                break;
        }
        
        // Resolve Pos vs Label
        if (correctPos != -1) {
            correctLbl = buttonLabels.get(correctPos);
        } else {
            // Find pos for label
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

    private void handlePress(int pos) {
        if (solved) return;
        
        int pressedLbl = buttonLabels.get(pos);
        
        if (pos == expectedPos && pressedLbl == expectedLbl) {
            // Correct
            correctPositions[stage-1] = pos;
            correctLabels[stage-1] = pressedLbl;
            
            stage++;
            if (stage > 5) {
                solved = true;
                bomb.checkDefused();
            } else {
                // Delay slightly to show press
                Timer t = new Timer(200, e -> startStage());
                t.setRepeats(false);
                t.start();
            }
        } else {
            // Wrong
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
        return "Memory";
    }
}
