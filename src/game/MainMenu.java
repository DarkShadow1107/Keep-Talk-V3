package game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class MainMenu extends JPanel {
    public MainMenu(App app) {
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title Panel
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // No background, transparent
            }
        };
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("KEEP TALKING", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE.deriveFont(64f));
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("and Nobody Explodes", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_SUBTITLE.deriveFont(32f));
        subtitle.setForeground(Theme.ACCENT_ORANGE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlePanel.add(subtitle);

        add(titlePanel, gbc);

        add(Box.createRigidArea(new Dimension(0, 50)), gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 0, 20));
        buttonPanel.setOpaque(false);
        
        addButton(buttonPanel, "START MISSION", e -> app.showLevelSelect());
        addButton(buttonPanel, "FREE PLAY", e -> app.showFreePlay());
        addButton(buttonPanel, "TRAINING MANUAL", e -> app.showManual());
        addButton(buttonPanel, "CREDITS", e -> app.showCredits());
        addButton(buttonPanel, "EXIT GAME", e -> System.exit(0));

        add(buttonPanel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw subtle background pattern (diagonal stripes)
        g2.setColor(new Color(30, 30, 35));
        for (int i = -getHeight(); i < getWidth(); i += 20) {
            g2.drawLine(i, 0, i + getHeight(), getHeight());
        }
        
        // Vignette effect
        RadialGradientPaint rgp = new RadialGradientPaint(
            getWidth() / 2, getHeight() / 2, Math.max(getWidth(), getHeight()),
            new float[]{0.0f, 1.0f},
            new Color[]{new Color(0,0,0,0), new Color(0,0,0,200)}
        );
        g2.setPaint(rgp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener action) {
        JButton button = Theme.createButton(text);
        button.addActionListener(action);
        button.setPreferredSize(new Dimension(400, 60));
        panel.add(button);
    }
}