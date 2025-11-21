package game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LevelSelect extends JPanel {
    private App app;

    public LevelSelect(App app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);

        JLabel title = new JLabel("SELECT MISSION", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT_ORANGE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.FONT_BOLD);
        tabbedPane.setBackground(Theme.PANEL_BG);
        tabbedPane.setForeground(Theme.TEXT_PRIMARY);
        
        tabbedPane.addTab("ALL", createLevelList(null));
        tabbedPane.addTab("EASY", createLevelList("Easy"));
        tabbedPane.addTab("MEDIUM", createLevelList("Medium"));
        tabbedPane.addTab("HARD", createLevelList("Hard"));
        tabbedPane.addTab("EXPERT", createLevelList("Expert"));

        add(tabbedPane, BorderLayout.CENTER);

        JButton backButton = Theme.createButton("ABORT");
        backButton.setBackground(Theme.BG_COLOR);
        backButton.setForeground(Theme.ACCENT_RED);
        backButton.addActionListener(e -> app.showMenu());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Theme.BG_COLOR);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JScrollPane createLevelList(String difficultyFilter) {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Theme.BG_COLOR);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        List<Level> levels = Level.getLevels();
        for (Level level : levels) {
            if (difficultyFilter != null && !level.getDifficulty().equalsIgnoreCase(difficultyFilter)) {
                continue;
            }

            JPanel levelPanel = new JPanel(new BorderLayout());
            levelPanel.setBackground(Theme.PANEL_BG);
            levelPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 5, 0),
                BorderFactory.createLineBorder(Theme.TEXT_SECONDARY, 1)
            ));
            levelPanel.setMaximumSize(new Dimension(800, 100));

            JLabel nameLabel = new JLabel(level.getName() + " [" + level.getDifficulty() + "]");
            nameLabel.setFont(Theme.FONT_BOLD);
            nameLabel.setForeground(Theme.TEXT_PRIMARY);
            nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
            
            JLabel descLabel = new JLabel(level.getDescription());
            descLabel.setFont(Theme.FONT_REGULAR);
            descLabel.setForeground(Theme.TEXT_SECONDARY);
            descLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 0));

            JButton playButton = Theme.createButton("DEPLOY");
            playButton.setBackground(Theme.ACCENT_RED);
            playButton.setPreferredSize(new Dimension(120, 40));
            playButton.addActionListener(e -> app.startGame(level));
            
            JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonWrapper.setBackground(Theme.PANEL_BG);
            buttonWrapper.add(playButton);

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.setBackground(Theme.PANEL_BG);
            textPanel.add(nameLabel);
            textPanel.add(descLabel);

            levelPanel.add(textPanel, BorderLayout.CENTER);
            levelPanel.add(buttonWrapper, BorderLayout.EAST);

            listPanel.add(levelPanel);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }
}