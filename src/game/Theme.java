package game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.GradientPaint;

public class Theme {
    // Colors
    public static final Color BG_COLOR = new Color(25, 25, 30);
    public static final Color PANEL_BG = new Color(40, 40, 45);
    public static final Color TEXT_PRIMARY = new Color(240, 240, 240);
    public static final Color TEXT_SECONDARY = new Color(180, 180, 180);
    public static final Color ACCENT_RED = new Color(231, 76, 60);
    public static final Color ACCENT_GREEN = new Color(46, 204, 113);
    public static final Color ACCENT_BLUE = new Color(52, 152, 219);
    public static final Color ACCENT_YELLOW = new Color(241, 196, 15);
    public static final Color ACCENT_ORANGE = new Color(230, 126, 34);
    public static final Color ACCENT_PURPLE = new Color(155, 89, 182);
    public static final Color DANGER_RED = new Color(192, 57, 43);
    public static final Color MANUAL_BG = new Color(250, 248, 239); // Paper color

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 48);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 24);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 18);
    public static final Font FONT_DIGITAL = new Font("Monospaced", Font.BOLD, 48);

    public static JButton createButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean pressed = getModel().isPressed();
                boolean rollover = getModel().isRollover();

                Color bg = getBackground();
                if (pressed) bg = bg.darker();
                else if (rollover) bg = bg.brighter();

                // Gradient
                GradientPaint gp = new GradientPaint(0, 0, bg.brighter(), 0, getHeight(), bg.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Border
                g2.setColor(new Color(255, 255, 255, 50));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);

                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        btn.setFont(FONT_BOLD.deriveFont(20f)); // Bigger font
        btn.setBackground(PANEL_BG.brighter());
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(250, 60)); // Bigger default size
        return btn;
    }

    public static void applyCursor(JComponent comp) {
        comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void apply(JComponent comp) {
        comp.setBackground(BG_COLOR);
        comp.setForeground(TEXT_PRIMARY);
    }
    
    public static Image createIcon() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2.setColor(Color.BLACK);
        g2.fillOval(2, 2, 60, 60);
        
        // Fuse
        g2.setColor(new Color(139, 69, 19));
        g2.setStroke(new BasicStroke(3));
        g2.drawArc(32, 5, 20, 20, 90, 90);
        
        // Spark
        g2.setColor(Color.ORANGE);
        // Star polygon
        int[] xPoints = {52, 54, 60, 55, 56, 52, 48, 49, 44, 50};
        int[] yPoints = {5, 10, 10, 14, 20, 16, 20, 14, 10, 10};
        g2.fillPolygon(xPoints, yPoints, 10);

        // Shine
        g2.setColor(new Color(255, 255, 255, 50));
        g2.fillOval(15, 15, 20, 20);
        
        g2.dispose();
        return image;
    }
    
    // Helper for fillStar if needed, but polygon above works.
}