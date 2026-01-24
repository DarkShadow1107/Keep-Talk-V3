package modules;

import game.Bomb;
import game.Theme;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import javax.swing.*;

public class ButtonModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private String color;
    private String text;
    private String stripColor; // Culoarea benzii care apare la apăsare prelungită
    private boolean isPressed = false;
    private long pressTime;

    public ButtonModule(Bomb bomb) {
        this.bomb = bomb;
        generateButton();
        setupUI();
    }

    /**
     * Generează aleatoriu proprietățile butonului (culoare, text, culoarea benzii).
     */
    private void generateButton() {
        String[] colors = {"Blue", "White", "Yellow", "Red"};
        String[] texts = {"Abort", "Detonate", "Hold", "Press"};
        Random rand = new Random();
        color = colors[rand.nextInt(colors.length)];
        text = texts[rand.nextInt(texts.length)];
        stripColor = colors[rand.nextInt(colors.length)];
    }

    /**
     * Configurează interfața grafică a butonului și a benzii indicatoare.
     */
    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Randare Bandă (Strip) - apare în dreapta când butonul este ținut apăsat
                int stripX = getWidth() - 35;
                int stripY = 50;
                int stripW = 18;
                int stripH = 100;
                
                // Fundalul benzii (oprită)
                g2.setColor(new Color(20, 20, 20));
                g2.fillRoundRect(stripX, stripY, stripW, stripH, 5, 5);
                
                // Dacă butonul este apăsat și regula cere "Hold", aprindem banda
                if (isPressed && shouldHold()) {
                    g2.setColor(getColor(stripColor));
                    g2.fillRoundRect(stripX + 2, stripY + 2, stripW - 4, stripH - 4, 3, 3);
                    
                    // Efect de strălucire (Glow) pentru bandă
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2.setColor(getColor(stripColor));
                    g2.fillRoundRect(stripX - 2, stripY - 2, stripW + 4, stripH + 4, 8, 8);
                    g2.setComposite(AlphaComposite.SrcOver);
                }

                // Randare umbră buton
                int btnSize = 140;
                int btnX = (getWidth() - stripW - 20 - btnSize) / 2;
                int btnY = (getHeight() - btnSize) / 2 + 10;

                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillOval(btnX + 5, btnY + 5, btnSize, btnSize);

                // Randare corp buton
                Color btnColor = getColor(color);
                if (isPressed) btnColor = btnColor.darker();
                
                g2.setColor(btnColor);
                g2.fillOval(btnX, btnY, btnSize, btnSize);
                
                // Efect 3D (Gradient pentru reflexie)
                GradientPaint gp = new GradientPaint(
                    btnX, btnY, new Color(255, 255, 255, 50),
                    btnX, btnY + btnSize, new Color(0, 0, 0, 50)
                );
                g2.setPaint(gp);
                g2.fillOval(btnX, btnY, btnSize, btnSize);

                // Contur buton
                g2.setColor(new Color(0, 0, 0, 50));
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(btnX, btnY, btnSize, btnSize);

                // Randare text pe buton
                g2.setColor(isDark(color) ? Color.WHITE : Color.BLACK);
                g2.setFont(Theme.FONT_BOLD.deriveFont(24f));
                FontMetrics fm = g2.getFontMetrics();
                int textX = btnX + (btnSize - fm.stringWidth(text)) / 2;
                int textY = btnY + (btnSize + fm.getAscent()) / 2 - 5;
                g2.drawString(text, textX, textY);
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(180, 180));

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (solved) return;
                
                // Verificăm dacă click-ul a fost în interiorul cercului butonului
                int btnSize = 140;
                int stripW = 20;
                int btnX = (panel.getWidth() - stripW - 20 - btnSize) / 2;
                int btnY = (panel.getHeight() - btnSize) / 2 + 10;
                
                Ellipse2D buttonShape = new Ellipse2D.Float(btnX, btnY, btnSize, btnSize);
                
                if (buttonShape.contains(e.getPoint())) {
                    isPressed = true;
                    pressTime = System.currentTimeMillis();
                    panel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isPressed) return;
                
                isPressed = false;
                panel.repaint();
                handleRelease();
            }
        });
    }

    /**
     * Logică apelată la eliberarea butonului. Verifică dacă regulile au fost respectate.
     */
    private void handleRelease() {
        boolean ruleSaysHold = shouldHold();
        
        if (ruleSaysHold) {
            // Dacă regula a fost să ținem apăsat, verificăm timpul la care a fost eliberat.
            if (checkReleaseTime()) {
                solve();
            } else {
                bomb.addStrike();
            }
        } else {
            // Dacă regula a fost un simplu "apasă și eliberează".
            solve();
        }
    }

    /**
     * Determină dacă butonul trebuie ținut apăsat (Hold) sau eliberat imediat (Press).
     * @return true dacă trebuie ținut apăsat, false altfel.
     */
    private boolean shouldHold() {
        if (color.equals("Blue") && text.equals("Abort")) return true;
        if (bomb.getBatteries() > 1 && text.equals("Detonate")) return false;
        if (color.equals("White") && bomb.hasIndicator("CAR")) return true;
        if (bomb.getBatteries() > 2 && bomb.hasIndicator("FRK")) return false;
        if (color.equals("Yellow")) return true;
        return !(color.equals("Red") && text.equals("Hold"));
    }

    /**
     * Verifică dacă timpul de pe cronometru conține cifra cerută de culoarea benzii.
     */
    private boolean checkReleaseTime() {
        int time = bomb.getTimeRemaining();
        int minutes = time / 60;
        int seconds = time % 60;
        String formatted = String.format("%02d%02d", minutes, seconds);
        
        int targetDigit;
        if (stripColor.equals("Blue")) targetDigit = 4;
        else if (stripColor.equals("White")) targetDigit = 1;
        else if (stripColor.equals("Yellow")) targetDigit = 5;
        else targetDigit = 1; // Default pentru Red sau altele

        return formatted.contains(String.valueOf(targetDigit));
    }

    private void solve() {
        solved = true;
        panel.repaint();
        bomb.checkDefused();
    }

    private Color getColor(String colorName) {
        switch (colorName) {
            case "Red": return Color.RED;
            case "Blue": return Color.BLUE;
            case "Yellow": return Color.YELLOW;
            case "White": return Color.WHITE;
            default: return Color.GRAY;
        }
    }

    private boolean isDark(String colorName) {
        return colorName.equals("Blue") || colorName.equals("Red");
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
        return game.Localization.get("MOD_BUTTON");
    }
}
