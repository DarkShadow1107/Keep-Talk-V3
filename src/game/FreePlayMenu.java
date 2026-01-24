package game;

import java.awt.*;
import javax.swing.*;

/**
 * Ecranul pentru modul "Free Play", unde jucatorul poate configura parametrii bombei.
 */
public class FreePlayMenu extends JPanel {
    private App app;
    private JSpinner timeSpinner; // Selector pentru timpul jocului
    private JSpinner modulesSpinner; // Selector pentru numarul de module
    private JSpinner strikesSpinner; // Selector pentru numarul de greseli permise

    public FreePlayMenu(App app) {
        this.app = app;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Titlul ecranului
        JLabel title = new JLabel(Localization.get("FREEPLAY_TITLE"), SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT_BLUE);
        add(title, gbc);

        add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        // Configurarea Timpului
        addLabel(Localization.get("FREEPLAY_TIME"), gbc);
        timeSpinner = new JSpinner(new SpinnerNumberModel(300, 30, 3600, 30));
        styleSpinner(timeSpinner);
        add(timeSpinner, gbc);

        // Configurarea Modulelor
        addLabel(Localization.get("FREEPLAY_MODULES"), gbc);
        modulesSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));
        styleSpinner(modulesSpinner);
        add(modulesSpinner, gbc);

        // Configurarea Greseli permise
        addLabel(Localization.get("FREEPLAY_STRIKES"), gbc);
        strikesSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        styleSpinner(strikesSpinner);
        add(strikesSpinner, gbc);

        add(Box.createRigidArea(new Dimension(0, 30)), gbc);

        // Butonul de pornire a jocului cu setarile custom
        JButton startButton = Theme.createButton(Localization.get("FREEPLAY_START"));
        startButton.setBackground(Theme.ACCENT_GREEN);
        startButton.addActionListener(e -> startFreePlay());
        add(startButton, gbc);

        // Intre la meniu
        JButton backButton = Theme.createButton(Localization.get("BTN_BACK"));
        backButton.addActionListener(e -> app.showMenu());
        add(backButton, gbc);
    }

    /** Adauga o eticheta text (label) in UI. */
    private void addLabel(String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_BOLD);
        label.setForeground(Theme.TEXT_PRIMARY);
        add(label, gbc);
    }

    /** Aplica un stil vizual consistent selectorului numeric (spinner). */
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

    /** Preia valorile din selectoare si porneste jocul intr-un nivel custom. */
    private void startFreePlay() {
        int time = ((Number)timeSpinner.getValue()).intValue();
        int modules = ((Number)modulesSpinner.getValue()).intValue();
        int strikes = ((Number)strikesSpinner.getValue()).intValue();
        
        Level customLevel = new Level(Localization.get("FREEPLAY_TITLE"), time, modules, strikes);
        app.startGame(customLevel);
    }
}
