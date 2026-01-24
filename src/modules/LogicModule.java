package modules;

import game.Bomb;
import game.Theme;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;

/**
 * Modulul "Logică" (Logic).
 * Jucătorul trebuie să rezolve ecuații cu porți logice (AND, OR, XOR, NAND, NOR).
 * Modulul are 3 stagii care trebuie completate consecutiv.
 */
public class LogicModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    
    /** Tipuri de porți logice disponibile în joc. */
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

    /**
     * Generează o nouă poartă și valori de intrare aleatorii pentru stagiul curent.
     */
    private void nextStage() {
        Random rand = new Random();
        currentGate = GateType.values()[rand.nextInt(GateType.values().length)];
        inputA = rand.nextBoolean();
        inputB = rand.nextBoolean();
        userOutput = false; // Resetăm ieșirea aleasă de utilizator
    }

    /**
     * Configurează interfața vizuală a porții logice.
     */
    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Desenare LED-uri de progres pentru stagii
                int stageX = 20;
                for(int i=0; i<TOTAL_STAGES; i++) {
                    Theme.drawLed(g2, stageX + i*20, 15, i < stage, true);
                }

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;

                // Desenare intrări A și B (LED-uri roșii/stânse)
                drawInput(g2, cx - 60, cy - 40, "A", inputA);
                drawInput(g2, cx - 60, cy + 40, "B", inputB);

                // Desenare simbol poartă logică
                drawGate(g2, cx, cy, currentGate);

                // Desenare comutator de ieșire (Toggle)
                int tx = cx + 60;
                int ty = cy;
                toggleRect = new Rectangle(tx - 20, ty - 20, 40, 40);
                
                g2.setColor(new Color(40, 40, 40));
                g2.fillRoundRect(tx - 20, ty - 20, 40, 40, 5, 5);
                
                if (userOutput) {
                    g2.setColor(Theme.ACCENT_GREEN);
                    g2.fillOval(tx - 15, ty - 15, 30, 30);
                    // Efect de strălucire pentru ieșirea activă (1)
                    g2.setColor(new Color(46, 204, 113, 100));
                    g2.fillOval(tx - 18, ty - 18, 36, 36);
                } else {
                    g2.setColor(new Color(20, 20, 20));
                    g2.fillOval(tx - 15, ty - 15, 30, 30);
                }
                
                g2.setColor(Color.GRAY);
                g2.setFont(Theme.FONT_BOLD.deriveFont(12f));
                g2.drawString("OUT", tx - 12, ty + 35);
                
                // Butonul de verificare (Check)
                g2.setColor(Theme.ACCENT_ORANGE);
                g2.fillRoundRect(cx - 40, getHeight() - 40, 80, 30, 10, 10);
                g2.setColor(Color.WHITE);
                g2.drawString("CHECK", cx - 20, getHeight() - 20);
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(180, 180));
        Theme.applyCursor(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (solved) return;
                
                // Comută valoarea de ieșire (0 sau 1) la click
                if (toggleRect.contains(e.getPoint())) {
                    userOutput = !userOutput;
                    panel.repaint();
                }
                
                // Verificare soluție la click pe butonul portocaliu
                int cx = panel.getWidth() / 2;
                Rectangle checkRect = new Rectangle(cx - 40, panel.getHeight() - 40, 80, 30);
                if (checkRect.contains(e.getPoint())) {
                    checkSolution();
                }
            }
        });
    }
    
    /**
     * Randează o intrare a porții (etichetă și LED).
     */
    private void drawInput(Graphics2D g2, int x, int y, String label, boolean on) {
        g2.setColor(Color.GRAY);
        g2.drawString(label, x - 15, y + 5);
        Theme.drawLed(g2, x, y - 6, on, false); // Intrări de culoare roșie
    }
    
    /**
     * Randează simbolul grafic al porții logice și firele de conexiune.
     */
    private void drawGate(Graphics2D g2, int x, int y, GateType type) {
        g2.setColor(Theme.TEXT_PRIMARY);
        g2.setStroke(new BasicStroke(2));
        
        // Simboluri simplificate (Cutie cu textul tipului de poartă)
        g2.drawRect(x - 25, y - 25, 50, 50);
        g2.setFont(Theme.FONT_BOLD.deriveFont(14f));
        FontMetrics fm = g2.getFontMetrics();
        String text = type.name();
        g2.drawString(text, x - fm.stringWidth(text)/2, y + 5);
        
        // Linii de conexiune între intrări, poartă și ieșire
        g2.drawLine(x - 60 + 12, y - 40, x - 25, y - 10); // A către Poartă
        g2.drawLine(x - 60 + 12, y + 40, x - 25, y + 10); // B către Poartă
        g2.drawLine(x + 25, y, x + 60 - 20, y); // Poartă către Out
    }

    /**
     * Verifică dacă ieșirea aleasă de jucător respectă algebra booleană pentru poarta curentă.
     */
    private void checkSolution() {
        boolean expected = false;
        switch (currentGate) {
            case AND:  expected = inputA && inputB; break;
            case OR:   expected = inputA || inputB; break;
            case XOR:  expected = inputA ^ inputB; break;
            case NAND: expected = !(inputA && inputB); break;
            case NOR:  expected = !(inputA || inputB); break;
        }
        
        if (userOutput == expected) {
            stage++;
            if (stage >= TOTAL_STAGES) {
                solved = true; // Modul rezolvat după toate stagiile
                bomb.checkDefused();
            } else {
                nextStage(); // Trecem la următoarea ecuație
            }
        } else {
            bomb.addStrike(); // Greșeală dacă logica e incorectă
            nextStage(); // Resetăm stagiul curent cu date noi
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
        return game.Localization.get("MOD_LOGIC");
    }
}
