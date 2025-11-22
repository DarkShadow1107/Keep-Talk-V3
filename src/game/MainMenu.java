package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private App app;
    private JComboBox<String> langSelector;

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

        // Language Selector (Top Right)
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        langPanel.setOpaque(false);
        String[] langs = {"English", "Français", "Español", "Deutsch", "Română"};
        langSelector = new JComboBox<>(langs);
        langSelector.setSelectedIndex(Localization.getLanguage().ordinal());
        langSelector.setFont(Theme.FONT_REGULAR);
        langSelector.setBackground(Theme.PANEL_BG);
        langSelector.setForeground(Theme.TEXT_PRIMARY);
        langSelector.setFocusable(false);
        
        langSelector.addActionListener(e -> {
            int idx = langSelector.getSelectedIndex();
            Localization.setLanguage(Localization.Language.values()[idx]);
            setupUI(); // Rebuild UI with new language
            revalidate();
            repaint();
        });
        langPanel.add(langSelector);
        
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
        
        // Add Lang Selector at bottom
        add(langPanel, gbc);
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