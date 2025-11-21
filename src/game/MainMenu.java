package game;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    public MainMenu(App app) {
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel title = new JLabel("KEEP TALKING", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        add(title, gbc);

        JLabel subtitle = new JLabel("and Nobody Explodes", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_SUBTITLE);
        subtitle.setForeground(Theme.ACCENT_ORANGE);
        add(subtitle, gbc);

        add(Box.createRigidArea(new Dimension(0, 40)), gbc);

        // Button Panel for better organization
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 0, 15)); // 1 column, 15px gap
        buttonPanel.setBackground(Theme.BG_COLOR);
        
        addButton(buttonPanel, "START MISSION", e -> app.showLevelSelect());
        addButton(buttonPanel, "FREE PLAY", e -> app.showFreePlay());
        addButton(buttonPanel, "TRAINING", e -> app.startGame(Level.getTrainingLevel()));
        addButton(buttonPanel, "BOMB MANUAL", e -> app.showManual());
        addButton(buttonPanel, "CREDENTIALS", e -> app.showCredits());
        addButton(buttonPanel, "EXIT", e -> System.exit(0));

        add(buttonPanel, gbc);
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener action) {
        JButton button = Theme.createButton(text);
        button.addActionListener(action);
        button.setPreferredSize(new Dimension(400, 70)); // Bigger buttons
        panel.add(button);
    }
}