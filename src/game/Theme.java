package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Clasa utilitara care defineste identitatea vizuala a jocului (Culori, Fonturi, Componente).
 * Gestioneaza tema intunecata (Dark Theme) si stilul industrial al jocului.
 */
public class Theme {
    // Definirea paletei de culori principale
    public static final Color BG_COLOR = new Color(10, 10, 15); // Fundal foarte inchis
    public static final Color PANEL_BG = new Color(35, 35, 40); // Fundal pentru panouri/module
    public static final Color PANEL_BORDER = new Color(60, 60, 65); // Bordura metalica
    public static final Color TEXT_PRIMARY = new Color(240, 240, 240); // Text principal albicios
    public static final Color TEXT_SECONDARY = new Color(180, 180, 180); // Text secundar gri
    public static final Color TEXT_DIGITAL = new Color(255, 0, 0); // Rosul segmentat de timer
    
    // Culori de accent pentru diverse elemente UI
    public static final Color ACCENT_RED = new Color(231, 76, 60);
    public static final Color ACCENT_GREEN = new Color(46, 204, 113);
    public static final Color ACCENT_BLUE = new Color(52, 152, 219);
    public static final Color ACCENT_YELLOW = new Color(241, 196, 15);
    public static final Color ACCENT_ORANGE = new Color(230, 126, 34);
    public static final Color ACCENT_PURPLE = new Color(155, 89, 182);
    
    // Culori specifice pentru LED-uri si stari de pericol
    public static final Color DANGER_RED = new Color(192, 57, 43);
    public static final Color LED_RED_ON = new Color(255, 50, 50);
    public static final Color LED_RED_OFF = new Color(80, 0, 0);
    public static final Color LED_GREEN_ON = new Color(50, 255, 50);
    public static final Color LED_GREEN_OFF = new Color(0, 80, 0);
    
    public static final Color MANUAL_BG = new Color(250, 248, 239); // Culoare de hartie veche

    // Fonts - Using Unicode-compatible fonts for international character support
    public static final Font FONT_TITLE = createUnicodeFont(Font.BOLD, 48);
    public static final Font FONT_SUBTITLE = createUnicodeFont(Font.PLAIN, 24);
    public static final Font FONT_REGULAR = createUnicodeFont(Font.PLAIN, 16);
    public static final Font FONT_BOLD = createUnicodeFont(Font.BOLD, 16);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 18);
    public static final Font FONT_SYMBOL = createUnicodeFont(Font.BOLD, 36);
    public static final Font FONT_DIGITAL = new Font("Monospaced", Font.BOLD, 48);
    
    private static Font createUnicodeFont(int style, int size) {
        // Try fonts in order of preference for Unicode support
        // Segoe UI Historic and Symbol are standard on Windows 10/11 and cover these ranges well
        String[] fontNames = {
            "Segoe UI Symbol", "Segoe UI Historic", "Arial Unicode MS", 
            "Cambria Math", "Malgun Gothic", "MS Gothic", "Dialog"
        };
        
        // Comprehensive test string containing all characters used in Keypad columns
        String testChars = "\u03D8\u0466\u03BB\u03DE\u046C\u03D7\u03F6\u04EC\u0480\u2606\u00BF\u00A9\u047C\u0480\u0496\u0506\u03EC\u00B6\u0462\u263A\u03A8\u263A\u03FE\u00B6\u046E\u2605\u0482\u00E6\u048A\u03A9";
        
        for (String name : fontNames) {
            Font f = new Font(name, style, size);
            // If the font can display at least the first character of each range, it's a good candidate
            if (f.canDisplayUpTo(testChars) == -1) {
                return f;
            }
        }
        
        // If no perfect match, return Dialog which has best OS-level fallback
        return new Font("Dialog", style, size);
    }

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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Border
                g2.setColor(new Color(255, 255, 255, 50));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);

                // Glow effect on hover
                if (rollover && !pressed) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }

                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        btn.setFont(FONT_BOLD.deriveFont(18f));
        btn.setBackground(new Color(60, 60, 70));
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 50));
        return btn;
    }

    public static void applyCursor(JComponent comp) {
        comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void apply(JComponent comp) {
        comp.setBackground(BG_COLOR);
        comp.setForeground(TEXT_PRIMARY);
    }
    
    public static void customizeScrollBar(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(80, 80, 90);
                this.trackColor = new Color(30, 30, 35);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
            
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(100, 100, 110), thumbBounds.width, 0, new Color(60, 60, 70)));
                g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
                g2.dispose();
            }
            
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                g.setColor(new Color(20, 20, 25));
                g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }
        });
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
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
        int[] xPoints = {52, 54, 60, 55, 56, 52, 48, 49, 44, 50};
        int[] yPoints = {5, 10, 10, 14, 20, 16, 20, 14, 10, 10};
        g2.fillPolygon(xPoints, yPoints, 10);

        // Shine
        g2.setColor(new Color(255, 255, 255, 50));
        g2.fillOval(15, 15, 20, 20);
        
        g2.dispose();
        return image;
    }

    public static void drawScrew(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(150, 150, 150));
        g2.fillOval(x, y, 10, 10);
        g2.setColor(new Color(100, 100, 100));
        g2.drawOval(x, y, 10, 10);
        g2.drawLine(x + 2, y + 5, x + 8, y + 5);
        g2.drawLine(x + 5, y + 2, x + 5, y + 8);
    }

    public static void drawLed(Graphics2D g2, int x, int y, boolean on, boolean isGreen) {
        Color cOn = isGreen ? LED_GREEN_ON : LED_RED_ON;
        Color cOff = isGreen ? LED_GREEN_OFF : LED_RED_OFF;
        
        g2.setColor(on ? cOn : cOff);
        g2.fillOval(x, y, 12, 12);
        
        // Shine
        if (on) {
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fillOval(x + 3, y + 3, 4, 4);
        }
        
        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawOval(x, y, 12, 12);
    }
}