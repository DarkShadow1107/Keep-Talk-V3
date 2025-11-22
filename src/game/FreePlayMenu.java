package game;

import javax.swing.*;
import java.awt.*;

public class FreePlayMenu extends JPanel {
    private App app;
    private JSpinner timeSpinner;
    private JSpinner modulesSpinner;
    private JSpinner strikesSpinner;

    public FreePlayMenu(App app) {
        this.app = app;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("FREE PLAY SETUP", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT_BLUE);
        add(title, gbc);

        add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        // Time Setup
        addLabel("Time (Seconds):", gbc);
        timeSpinner = new JSpinner(new SpinnerNumberModel(300, 30, 3600, 30));
        styleSpinner(timeSpinner);
        add(timeSpinner, gbc);

        // Modules Setup
        addLabel("Modules Count:", gbc);
        modulesSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));
        styleSpinner(modulesSpinner);
        add(modulesSpinner, gbc);

        // Strikes Setup
        addLabel("Max Strikes:", gbc);
        strikesSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        styleSpinner(strikesSpinner);
        add(strikesSpinner, gbc);

        add(Box.createRigidArea(new Dimension(0, 30)), gbc);

        JButton startButton = Theme.createButton("START MISSION");
        startButton.setBackground(Theme.ACCENT_GREEN);
        startButton.addActionListener(e -> startFreePlay());
        add(startButton, gbc);

        JButton backButton = Theme.createButton("BACK");
        backButton.addActionListener(e -> app.showMenu());
        add(backButton, gbc);
    }

    private void addLabel(String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_BOLD);
        label.setForeground(Theme.TEXT_PRIMARY);
        add(label, gbc);
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(Theme.FONT_MONO);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor)editor).getTextField().setBackground(Theme.PANEL_BG);
            ((JSpinner.DefaultEditor)editor).getTextField().setForeground(Theme.TEXT_PRIMARY);
        }
        spinner.setBorder(BorderFactory.createLineBorder(Theme.TEXT_SECONDARY));
        spinner.setPreferredSize(new Dimension(200, 40));
    }

    private void startFreePlay() {
        int time = (int) timeSpinner.getValue();
        int modules = (int) modulesSpinner.getValue();
        int strikes = (int) strikesSpinner.getValue();
        
        Level customLevel = new Level("Free Play", time, modules, strikes);
        app.startGame(customLevel);
    }
}
