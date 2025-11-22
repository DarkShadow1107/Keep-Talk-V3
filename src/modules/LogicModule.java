package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.Random;

public class LogicModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    
    private enum GateType { AND, OR, XOR, NAND, NOR }
    private GateType currentGate;
    private boolean inputA;
    private boolean inputB;
    private boolean userOutput = false;
    
    private int stage = 0;
    private final int TOTAL_STAGES = 3;
    
    private Rectangle toggleRect;

    public LogicModule(Bomb bomb) {
        this.bomb = bomb;
        nextStage();
        setupUI();
    }

    private void nextStage() {
        Random rand = new Random();
        currentGate = GateType.values()[rand.nextInt(GateType.values().length)];
        inputA = rand.nextBoolean();
        inputB = rand.nextBoolean();
        userOutput = false; // Reset user output
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
                
                // Draw Stage LEDs
                int stageX = 20;
                for(int i=0; i<TOTAL_STAGES; i++) {
                    Theme.drawLed(g2, stageX + i*20, 15, i < stage, true);
                }

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;

                // Draw Inputs
                drawInput(g2, cx - 60, cy - 40, "A", inputA);
                drawInput(g2, cx - 60, cy + 40, "B", inputB);

                // Draw Gate Symbol
                drawGate(g2, cx, cy, currentGate);

                // Draw Output Toggle
                int tx = cx + 60;
                int ty = cy;
                toggleRect = new Rectangle(tx - 20, ty - 20, 40, 40);
                
                g2.setColor(new Color(40, 40, 40));
                g2.fillRoundRect(tx - 20, ty - 20, 40, 40, 5, 5);
                
                if (userOutput) {
                    g2.setColor(Theme.ACCENT_GREEN);
                    g2.fillOval(tx - 15, ty - 15, 30, 30);
                    // Glow
                    g2.setColor(new Color(46, 204, 113, 100));
                    g2.fillOval(tx - 18, ty - 18, 36, 36);
                } else {
                    g2.setColor(new Color(20, 20, 20));
                    g2.fillOval(tx - 15, ty - 15, 30, 30);
                }
                
                g2.setColor(Color.GRAY);
                g2.setFont(Theme.FONT_BOLD.deriveFont(12f));
                g2.drawString("OUT", tx - 12, ty + 35);
                
                // Submit Button
                g2.setColor(Theme.ACCENT_ORANGE);
                g2.fillRoundRect(cx - 40, getHeight() - 40, 80, 30, 10, 10);
                g2.setColor(Color.WHITE);
                g2.drawString("CHECK", cx - 20, getHeight() - 20);
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(200, 200));
        Theme.applyCursor(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (solved) return;
                
                // Toggle Output
                if (toggleRect.contains(e.getPoint())) {
                    userOutput = !userOutput;
                    panel.repaint();
                }
                
                // Check Button
                int cx = panel.getWidth() / 2;
                Rectangle checkRect = new Rectangle(cx - 40, panel.getHeight() - 40, 80, 30);
                if (checkRect.contains(e.getPoint())) {
                    checkSolution();
                }
            }
        });
    }
    
    private void drawInput(Graphics2D g2, int x, int y, String label, boolean on) {
        g2.setColor(Color.GRAY);
        g2.drawString(label, x - 15, y + 5);
        Theme.drawLed(g2, x, y - 6, on, false); // Red inputs
    }
    
    private void drawGate(Graphics2D g2, int x, int y, GateType type) {
        g2.setColor(Theme.TEXT_PRIMARY);
        g2.setStroke(new BasicStroke(2));
        
        // Simplified symbols (Box with text)
        g2.drawRect(x - 25, y - 25, 50, 50);
        g2.setFont(Theme.FONT_BOLD.deriveFont(14f));
        FontMetrics fm = g2.getFontMetrics();
        String text = type.name();
        g2.drawString(text, x - fm.stringWidth(text)/2, y + 5);
        
        // Lines connecting
        g2.drawLine(x - 60 + 12, y - 40, x - 25, y - 10); // A to Gate
        g2.drawLine(x - 60 + 12, y + 40, x - 25, y + 10); // B to Gate
        g2.drawLine(x + 25, y, x + 60 - 20, y); // Gate to Out
    }

    private void checkSolution() {
        boolean expected = false;
        switch (currentGate) {
            case AND: expected = inputA && inputB; break;
            case OR: expected = inputA || inputB; break;
            case XOR: expected = inputA ^ inputB; break;
            case NAND: expected = !(inputA && inputB); break;
            case NOR: expected = !(inputA || inputB); break;
        }
        
        if (userOutput == expected) {
            stage++;
            if (stage >= TOTAL_STAGES) {
                solved = true;
                bomb.checkDefused();
            } else {
                nextStage();
            }
        } else {
            bomb.addStrike();
            // Reset stage or keep same? Keep same but randomize inputs
            nextStage(); 
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
        return "Logic";
    }
}
