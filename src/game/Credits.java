package game;

import javax.swing.*;
import java.awt.*;

public class Credits extends JPanel {
    public Credits(App app) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);

        JLabel title = new JLabel(Localization.get("CREDITS_TITLE"), SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT_GREEN);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JTextArea text = new JTextArea();
        text.setText("\n\n" + Localization.get("CREDITS_ORIGINAL") + "\n\n" +
                     Localization.get("CREDITS_JAVA") + "\n" +
                     "Alexandru Gabriel, Caldararu Denisa & Dumitru Claudia\n\n" +
                     Localization.get("CREDITS_PROGRAMMING") + "\n" +
                     "Alexandru Gabriel, Caldararu Denisa & Dumitru Claudia\n\n" +
                     Localization.get("CREDITS_DESIGN") + "\n" +
                     "Alexandru Gabriel\n\n" +
                     Localization.get("CREDITS_TESTING") + "\n" +
                     "Caldararu Denisa & Dumitru Claudia\n\n" +
                     Localization.get("CREDITS_THANKS"));
        text.setFont(Theme.FONT_MONO);
        text.setForeground(Theme.TEXT_PRIMARY);
        text.setBackground(Theme.BG_COLOR);
        text.setEditable(false);
        text.setMargin(new Insets(20, 50, 20, 50));
        
        add(text, BorderLayout.CENTER);

        JButton backButton = Theme.createButton(Localization.get("BTN_BACK"));
        backButton.addActionListener(e -> app.showMenu());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Theme.BG_COLOR);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}