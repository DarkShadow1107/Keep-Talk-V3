package game;

import java.awt.*;
import javax.swing.*;

/**
 * Ecranul de credite care afiseaza autorii jocului.
 */
public class Credits extends JPanel {
    public Credits(App app) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);

        // Titlul ecranului
        JLabel title = new JLabel(Localization.get("CREDITS_TITLE"), SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT_GREEN);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Zona de text pentru afisarea numelor si rolurilor
        JTextArea text = new JTextArea();
        text.setText("\n\n" + Localization.get("CREDITS_ORIGINAL") + "\n\n" +
                     Localization.get("CREDITS_JAVA") + "\n" +
                     "Alexandru Gabriel, Caldararu Denisa & Dumitru Claudia\n\n" +
                     Localization.get("CREDITS_PROGRAMMING") + "\n" +
                     "Alexandru Gabriel, Caldararu Denisa & Dumitru Claudia\n\n" +
                     Localization.get("CREDITS_DESIGN") + "\n" +
                     "Alexandru Gabriel, Caldararu Denisa & Dumitru Claudia\n\n" +
                     Localization.get("CREDITS_TESTING") + "\n" +
                     "Caldararu Denisa & Dumitru Claudia\n\n" +
                     Localization.get("CREDITS_THANKS"));
        text.setFont(Theme.FONT_MONO);
        text.setForeground(Theme.TEXT_PRIMARY);
        text.setBackground(Theme.BG_COLOR);
        text.setEditable(false);
        text.setMargin(new Insets(20, 50, 20, 50));
        
        add(text, BorderLayout.CENTER);

        // Butonul de intoarcere la meniul principal
        JButton backButton = Theme.createButton(Localization.get("BTN_BACK"));
        backButton.addActionListener(e -> app.showMenu());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Theme.BG_COLOR);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}