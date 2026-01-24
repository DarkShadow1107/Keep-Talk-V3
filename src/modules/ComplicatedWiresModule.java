package modules;

import game.Bomb;
import game.Theme;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

/**
 * Modulul "Fire Complicate" (Complicated Wires).
 * Acest modul prezintă un set de fire, fiecare putând avea o combinație de:
 * culoare roșie, culoare albastră, o stea și un LED aprins.
 * Decizia de a tăia un fir se bazează pe o diagramă Venn complexă care folosește 
 * atributele firului și starea bombei (baterii, port paralel, seria).
 */
public class ComplicatedWiresModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<Wire> wires;

    /**
     * Clasă internă pentru reprezentarea unui fir individual.
     */
    private static class Wire {
        boolean red;
        boolean blue;
        boolean star;
        boolean led;
        boolean cut = false;
        boolean shouldCut;

        /**
         * Returnează culoarea vizuală a firului.
         */
        public Color getColor() {
            if (red && blue) return new Color(128, 0, 128); // Mov (Roșu + Albastru)
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

    /**
     * Generează între 4 și 6 fire cu atribute aleatorii.
     */
    private void generatePuzzle() {
        wires = new ArrayList<>();
        Random rand = new Random();
        int count = 4 + rand.nextInt(3); // 4 la 6 fire

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

    /**
     * Determină dacă un fir trebuie tăiat bazat pe logica Diagramelor Venn.
     * C = Tăiați (Cut), D = Nu tăiați (Don't), S = Ultimul număr din serie e par, 
     * P = Bomba are port paralel, B = Bomba are 2 sau mai multe baterii.
     */
    private boolean determineShouldCut(Wire w) {
        // Logica diagramei Venn
        boolean r = w.red;
        boolean b = w.blue;
        boolean s = w.star;
        boolean l = w.led;

        // Tabelul de logică (implementare simplificată a diagramei)
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

    /**
     * Configurează interfața grafică a modulului.
     */
    private void setupUI() {
        panel = new JPanel(new GridLayout(1, wires.size(), 5, 5));
        panel.setBackground(Theme.PANEL_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Wire w : wires) {
            JPanel wirePanel = new JPanel(new BorderLayout());
            wirePanel.setBackground(Theme.PANEL_BG);
            wirePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            // LED-ul de deasupra firului (aprins dacă led=true)
            JPanel ledPanel = new JPanel();
            ledPanel.setBackground(Theme.PANEL_BG);
            JLabel led = new JLabel("●");
            led.setFont(new Font("SansSerif", Font.BOLD, 20));
            led.setForeground(w.led ? Color.WHITE : new Color(50, 50, 50)); 
            if (w.led) led.setForeground(Color.CYAN); // Culoare LED aprins
            ledPanel.add(led);
            
            // Reprezentarea firului și a stelei
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

            // Butonul de tăiere
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
                wireLine.setBackground(Theme.BG_COLOR); // Efect vizual de tăiere
                
                if (w.shouldCut) {
                    checkSolved();
                } else {
                    bomb.addStrike(); // Greșeală dacă firul nu trebuia tăiat
                }
            });

            wirePanel.add(ledPanel, BorderLayout.NORTH);
            wirePanel.add(centerPanel, BorderLayout.CENTER);
            wirePanel.add(cutBtn, BorderLayout.SOUTH);
            
            panel.add(wirePanel);
        }
        panel.setPreferredSize(new Dimension(180, 180));
    }

    /**
     * Verifică dacă toate firele care trebuiau tăiate au fost tăiate.
     */
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
        return game.Localization.get("MOD_COMPWIRES");
    }
}
