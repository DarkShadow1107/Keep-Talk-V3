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
    private String stripColor;
    private boolean isPressed = false;
    private long pressTime = 0;

    public ButtonModule(Bomb bomb) {
        this.bomb = bomb;
        generateButton();
        setupUI();
    }

    private void generateButton() {
        String[] colors = {"Blue", "White", "Yellow", "Red"};
        String[] texts = {"Abort", "Detonate", "Hold", "Press"};
        Random rand = new Random();
        color = colors[rand.nextInt(colors.length)];
        text = texts[rand.nextInt(texts.length)];
        stripColor = colors[rand.nextInt(colors.length)];
    }

    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw Strip
                int stripX = getWidth() - 35;
                int stripY = 50;
                int stripW = 18;
                int stripH = 100;
                
                g2.setColor(new Color(20, 20, 20));
                g2.fillRoundRect(stripX, stripY, stripW, stripH, 5, 5);
                
                if (isPressed && shouldHold()) {
                    g2.setColor(getColor(stripColor));
                    g2.fillRoundRect(stripX + 2, stripY + 2, stripW - 4, stripH - 4, 3, 3);
                    
                    // Glow
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2.setColor(getColor(stripColor));
                    g2.fillRoundRect(stripX - 2, stripY - 2, stripW + 4, stripH + 4, 8, 8);
                    g2.setComposite(AlphaComposite.SrcOver);
                }

                // Draw Button Base
                int btnSize = 140;
                int btnX = (getWidth() - stripW - 20 - btnSize) / 2;
                int btnY = (getHeight() - btnSize) / 2 + 10;

                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillOval(btnX + 5, btnY + 5, btnSize, btnSize);

                // Draw Button
                Color btnColor = getColor(color);
                if (isPressed) btnColor = btnColor.darker();
                
                g2.setColor(btnColor);
                g2.fillOval(btnX, btnY, btnSize, btnSize);
                
                // Button Highlight/Shadow
                GradientPaint gp = new GradientPaint(
                    btnX, btnY, new Color(255, 255, 255, 50),
                    btnX, btnY + btnSize, new Color(0, 0, 0, 50)
                );
                g2.setPaint(gp);
                g2.fillOval(btnX, btnY, btnSize, btnSize);

                // Button Border
                g2.setColor(new Color(0, 0, 0, 50));
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(btnX, btnY, btnSize, btnSize);

                // Text
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
                
                // Check if click is within button circle
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

    private void handleRelease() {
        // Determine if it was a tap or a hold
        // In the original game, holding for any amount of time shows the strip.
        // If you release immediately (tap), you don't see the strip.
        // But "immediately" is subjective.
        // Logic:
        // If we are supposed to hold, and we release, we check the timer.
        // If we are supposed to press, and we release, it's solved.
        
        boolean ruleSaysHold = shouldHold();
        
        if (ruleSaysHold) {
            // We must release on a specific digit.
            if (checkReleaseTime()) {
                solve();
            } else {
                bomb.addStrike();
            }
        } else {
            // Rule says press and release.
            // If we held it long enough to see the strip, is that a strike?
            // In the original game, if you hold when you should press, you can still solve it by releasing on the strip rule?
            // Actually, if the rule says "Press and release", you just do it.
            // If you hold it, the strip appears, and then you are now in "Release a Held Button" territory.
            // So if you hold it, you MUST follow the strip rule.
            // If you tap it, you follow the "Press" rule.
            
            // Simplified logic:
            // If the user held the button (saw the strip), they must follow the strip rule.
            // If the user tapped the button (didn't see strip), they are following the initial rule.
            
            // Since we show the strip immediately on press in this implementation (for feedback),
            // let's say:
            // If the initial rule was "Press", and they release, it's correct.
            // UNLESS they waited for a specific digit? No, that's too complex.
            
            // Let's stick to:
            // If rule says Hold: You MUST release on digit.
            // If rule says Press: You MUST release immediately (any digit).
            // But if you release on a specific digit that matches the strip rule, maybe that's okay too?
            // Let's just enforce the primary rule.
            
            solve();
        }
    }

    private boolean shouldHold() {
        if (color.equals("Blue") && text.equals("Abort")) return true;
        if (bomb.getBatteries() > 1 && text.equals("Detonate")) return false;
        if (color.equals("White") && bomb.hasIndicator("CAR")) return true;
        if (bomb.getBatteries() > 2 && bomb.hasIndicator("FRK")) return false;
        if (color.equals("Yellow")) return true;
        if (color.equals("Red") && text.equals("Hold")) return false;
        return true;
    }

    private boolean checkReleaseTime() {
        int time = bomb.getTimeRemaining();
        int minutes = time / 60;
        int seconds = time % 60;
        String formatted = String.format("%02d%02d", minutes, seconds);
        
        int targetDigit;
        if (stripColor.equals("Blue")) targetDigit = 4;
        else if (stripColor.equals("White")) targetDigit = 1;
        else if (stripColor.equals("Yellow")) targetDigit = 5;
        else targetDigit = 1;

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
