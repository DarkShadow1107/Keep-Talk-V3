package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComplicatedWiresModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<Wire> wires;

    private static class Wire {
        boolean red;
        boolean blue;
        boolean star;
        boolean led;
        boolean cut = false;
        boolean shouldCut;

        public Color getColor() {
            if (red && blue) return new Color(128, 0, 128); // Purple
            if (red) return Color.RED;
            if (blue) return Color.BLUE;
            return Color.WHITE;
        }
    }

    public ComplicatedWiresModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
    }

    private void generatePuzzle() {
        wires = new ArrayList<>();
        Random rand = new Random();
        int count = 4 + rand.nextInt(3); // 4 to 6 wires

        for (int i = 0; i < count; i++) {
            Wire w = new Wire();
            w.red = rand.nextBoolean();
            w.blue = rand.nextBoolean();
            w.star = rand.nextBoolean();
            w.led = rand.nextBoolean();
            w.shouldCut = determineShouldCut(w);
            wires.add(w);
        }
    }

    private boolean determineShouldCut(Wire w) {
        // Venn Diagram Logic
        // C = Cut, D = Don't, S = Serial Even, P = Parallel, B = Batteries >= 2
        
        boolean r = w.red;
        boolean b = w.blue;
        boolean s = w.star;
        boolean l = w.led;

        // Logic Table (Simplified implementation of the Venn Diagram)
        if (r && b && s && l) return false; // D
        if (r && b && s && !l) return bomb.hasParallelPort(); // P
        if (r && b && !s && l) return bomb.getSerialNumber().charAt(bomb.getSerialNumber().length()-1) % 2 == 0; // S
        if (r && b && !s && !l) return bomb.getSerialNumber().charAt(bomb.getSerialNumber().length()-1) % 2 == 0; // S
        
        if (r && !b && s && l) return bomb.getBatteries() >= 2; // B
        if (r && !b && s && !l) return true; // C
        if (r && !b && !s && l) return bomb.getBatteries() >= 2; // B
        if (r && !b && !s && !l) return bomb.getSerialNumber().charAt(bomb.getSerialNumber().length()-1) % 2 == 0; // S
        
        if (!r && b && s && l) return bomb.hasParallelPort(); // P
        if (!r && b && s && !l) return false; // D
        if (!r && b && !s && l) return bomb.hasParallelPort(); // P
        if (!r && b && !s && !l) return bomb.getSerialNumber().charAt(bomb.getSerialNumber().length()-1) % 2 == 0; // S
        
        if (!r && !b && s && l) return bomb.getBatteries() >= 2; // B
        if (!r && !b && s && !l) return true; // C
        if (!r && !b && !s && l) return false; // D
        if (!r && !b && !s && !l) return true; // C

        return false;
    }

    private void setupUI() {
        panel = new JPanel(new GridLayout(1, wires.size(), 5, 5));
        panel.setBackground(Theme.PANEL_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Wire w : wires) {
            JPanel wirePanel = new JPanel(new BorderLayout());
            wirePanel.setBackground(Theme.PANEL_BG);
            wirePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            // LED
            JPanel ledPanel = new JPanel();
            ledPanel.setBackground(Theme.PANEL_BG);
            JLabel led = new JLabel("●");
            led.setFont(new Font("SansSerif", Font.BOLD, 20));
            led.setForeground(w.led ? Color.WHITE : new Color(50, 50, 50)); // Lit or dark
            if (w.led) led.setForeground(Color.CYAN); // Lit LED color
            ledPanel.add(led);
            
            // Wire
            JPanel centerPanel = new JPanel(new GridLayout(2, 1));
            centerPanel.setBackground(Theme.PANEL_BG);
            
            JLabel starLabel = new JLabel(w.star ? "★" : "");
            starLabel.setHorizontalAlignment(SwingConstants.CENTER);
            starLabel.setForeground(Theme.TEXT_PRIMARY);
            
            JPanel wireLine = new JPanel();
            wireLine.setBackground(w.getColor());
            wireLine.setPreferredSize(new Dimension(10, 40));
            
            centerPanel.add(starLabel);
            centerPanel.add(wireLine);

            // Cut Button
            JButton cutBtn = new JButton("✂");
            cutBtn.setMargin(new Insets(0,0,0,0));
            cutBtn.setBackground(Theme.PANEL_BG.brighter());
            cutBtn.setForeground(Theme.TEXT_PRIMARY);
            cutBtn.setFocusable(false);
            cutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            cutBtn.addActionListener(e -> {
                if (solved || w.cut) return;
                w.cut = true;
                cutBtn.setEnabled(false);
                wireLine.setBackground(Theme.BG_COLOR); // "Cut" visual
                
                if (w.shouldCut) {
                    checkSolved();
                } else {
                    bomb.addStrike();
                }
            });

            wirePanel.add(ledPanel, BorderLayout.NORTH);
            wirePanel.add(centerPanel, BorderLayout.CENTER);
            wirePanel.add(cutBtn, BorderLayout.SOUTH);
            
            panel.add(wirePanel);
        }
    }

    private void checkSolved() {
        boolean allCorrect = true;
        for (Wire w : wires) {
            if (w.shouldCut && !w.cut) {
                allCorrect = false;
                break;
            }
        }
        
        if (allCorrect) {
            solved = true;
            panel.setBackground(Theme.ACCENT_GREEN);
            bomb.checkDefused();
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
        return "Comp. Wires";
    }
}
