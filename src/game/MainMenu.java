package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel {
    private App app;

    public MainMenu(App app) {
        this.app = app;
        setLayout(new BorderLayout());
        setupUI();
    }

    private void setupUI() {
        removeAll();
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel(Localization.get("TITLE_MAIN"), SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE.deriveFont(64f));
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel(Localization.get("TITLE_SUB"), SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_SUBTITLE.deriveFont(32f));
        subtitle.setForeground(Theme.ACCENT_ORANGE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlePanel.add(subtitle);

        add(titlePanel, gbc);

        add(Box.createRigidArea(new Dimension(0, 30)), gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 0, 20));
        buttonPanel.setOpaque(false);
        
        addButton(buttonPanel, Localization.get("BTN_START"), e -> app.showLevelSelect());
        addButton(buttonPanel, Localization.get("BTN_FREEPLAY"), e -> app.showFreePlay());
        addButton(buttonPanel, Localization.get("BTN_MANUAL"), e -> app.showManual());
        addButton(buttonPanel, Localization.get("BTN_CREDITS"), e -> app.showCredits());
        addButton(buttonPanel, Localization.get("BTN_EXIT"), e -> System.exit(0));

        add(buttonPanel, gbc);
        
        // Language Selector Panel (Redesigned)
        JPanel langPanel = createLanguageSelector();
        add(langPanel, gbc);
    }

    private JPanel createLanguageSelector() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BorderLayout());
        
        JLabel langLabel = new JLabel(Localization.get("LBL_LANGUAGE") + ": ");
        langLabel.setFont(Theme.FONT_BOLD);
        langLabel.setForeground(Theme.TEXT_SECONDARY);
        
        // Create a styled dropdown panel
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        selectorPanel.setOpaque(false);
        
        Localization.Language currentLang = Localization.getLanguage();
        
        // Create a custom styled button that shows current language
        JButton langButton = new JButton(currentLang.getDisplayName()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean rollover = getModel().isRollover();
                
                // Background gradient
                GradientPaint gp = new GradientPaint(0, 0, 
                    rollover ? new Color(70, 70, 80) : new Color(50, 50, 60), 
                    0, getHeight(), 
                    rollover ? new Color(50, 50, 60) : new Color(35, 35, 45));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Border
                g2.setColor(Theme.ACCENT_ORANGE);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 15, 15);
                
                // Text
                g2.setColor(Theme.TEXT_PRIMARY);
                g2.setFont(Theme.FONT_BOLD.deriveFont(16f));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 3;
                g2.drawString(getText(), x, y);
                
                // Arrow indicator
                g2.setColor(Theme.ACCENT_ORANGE);
                int arrowX = getWidth() - 25;
                int arrowY = getHeight() / 2 - 3;
                g2.fillPolygon(new int[]{arrowX, arrowX + 10, arrowX + 5}, 
                               new int[]{arrowY, arrowY, arrowY + 8}, 3);
                
                g2.dispose();
            }
        };
        langButton.setPreferredSize(new Dimension(200, 45));
        langButton.setFont(Theme.FONT_BOLD);
        langButton.setForeground(Theme.TEXT_PRIMARY);
        langButton.setFocusPainted(false);
        langButton.setBorderPainted(false);
        langButton.setContentAreaFilled(false);
        langButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Create popup menu for languages
        JPopupMenu langMenu = new JPopupMenu();
        langMenu.setBackground(new Color(40, 40, 50));
        langMenu.setBorder(BorderFactory.createLineBorder(Theme.ACCENT_ORANGE, 2));
        
        for (Localization.Language lang : Localization.Language.values()) {
            JMenuItem item = new JMenuItem(lang.getDisplayName()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (getModel().isArmed()) {
                        g2.setColor(Theme.ACCENT_ORANGE.darker());
                    } else {
                        g2.setColor(new Color(40, 40, 50));
                    }
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Highlight current language
                    if (lang == Localization.getLanguage()) {
                        g2.setColor(Theme.ACCENT_GREEN);
                        g2.fillRect(0, 0, 4, getHeight());
                    }
                    
                    g2.setColor(Theme.TEXT_PRIMARY);
                    g2.setFont(Theme.FONT_REGULAR.deriveFont(14f));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(getText(), 15, (getHeight() + fm.getAscent()) / 2 - 2);
                    
                    g2.dispose();
                }
            };
            item.setPreferredSize(new Dimension(180, 35));
            item.setFont(Theme.FONT_REGULAR);
            item.setForeground(Theme.TEXT_PRIMARY);
            item.setBackground(new Color(40, 40, 50));
            item.setBorderPainted(false);
            item.addActionListener(e -> {
                Localization.setLanguage(lang);
                langButton.setText(lang.getDisplayName());
                setupUI();
                revalidate();
                repaint();
            });
            langMenu.add(item);
        }
        
        langButton.addActionListener(e -> {
            langMenu.show(langButton, 0, langButton.getHeight());
        });
        
        selectorPanel.add(langLabel);
        selectorPanel.add(langButton);
        container.add(selectorPanel, BorderLayout.CENTER);
        
        return container;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw dark background
        g2.setColor(Theme.BG_COLOR);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Draw Hex Pattern
        drawHexPattern(g2);
        
        // Vignette effect
        RadialGradientPaint rgp = new RadialGradientPaint(
            getWidth() / 2, getHeight() / 2, Math.max(getWidth(), getHeight()),
            new float[]{0.0f, 1.0f},
            new Color[]{new Color(0,0,0,0), new Color(0,0,0,200)}
        );
        g2.setPaint(rgp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawHexPattern(Graphics2D g2) {
        g2.setColor(new Color(30, 30, 35));
        g2.setStroke(new BasicStroke(1));
        
        int r = 30; // radius
        int w = (int)(Math.sqrt(3) * r);
        int h = 2 * r;
        
        for (int y = -h; y < getHeight() + h; y += h * 0.75) {
            for (int x = -w; x < getWidth() + w; x += w) {
                int cx = x;
                int cy = y;
                if ((y / (h * 0.75)) % 2 != 0) {
                    cx += w / 2;
                }
                drawHex(g2, cx, cy, r);
            }
        }
    }

    private void drawHex(Graphics2D g2, int x, int y, int r) {
        Polygon p = new Polygon();
        for (int i = 0; i < 6; i++) {
            p.addPoint(
                (int)(x + r * Math.cos(i * Math.PI / 3)),
                (int)(y + r * Math.sin(i * Math.PI / 3))
            );
        }
        g2.drawPolygon(p);
    }

    private void addButton(JPanel panel, String text, ActionListener action) {
        JButton button = Theme.createButton(text);
        button.addActionListener(action);
        button.setPreferredSize(new Dimension(400, 60));
        panel.add(button);
    }
}