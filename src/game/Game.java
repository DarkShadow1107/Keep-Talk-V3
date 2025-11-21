package game;

import modules.*;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JPanel {
    private JLabel timerLabel;
    private JLabel strikesLabel;
    private Bomb bomb;
    private App app;

    public Game(App app, Level level) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);

        // Initialize Bomb first so we can display its properties
        bomb = new Bomb(level.getTime(), level.getMaxStrikes(), this);

        // Top Panel for Timer and Strikes
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        timerLabel = new JLabel("00:00", SwingConstants.CENTER);
        timerLabel.setFont(Theme.FONT_DIGITAL);
        timerLabel.setForeground(Theme.ACCENT_RED);
        topPanel.add(timerLabel, BorderLayout.CENTER);

        strikesLabel = new JLabel("X ".repeat(0), SwingConstants.RIGHT); // Will update
        strikesLabel.setFont(Theme.FONT_TITLE);
        strikesLabel.setForeground(Theme.ACCENT_RED);
        topPanel.add(strikesLabel, BorderLayout.EAST);

        JButton abortButton = Theme.createButton("GIVE UP");
        abortButton.setBackground(Theme.ACCENT_RED);
        abortButton.setPreferredSize(new Dimension(120, 40));
        abortButton.addActionListener(e -> onExplode("Mission Aborted"));
        topPanel.add(abortButton, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // Side Panel for Bomb Info
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Theme.PANEL_BG);
        sidePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, Theme.TEXT_SECONDARY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        sidePanel.setPreferredSize(new Dimension(200, 0));

        addInfoLabel(sidePanel, "SERIAL #", bomb.getSerialNumber());
        addInfoLabel(sidePanel, "BATTERIES", String.valueOf(bomb.getBatteries()));
        addInfoLabel(sidePanel, "PARALLEL", bomb.hasParallelPort() ? "YES" : "NO");
        
        List<String> indicators = bomb.getIndicators();
        if (!indicators.isEmpty()) {
            addInfoLabel(sidePanel, "INDICATORS", String.join(", ", indicators));
        } else {
            addInfoLabel(sidePanel, "INDICATORS", "NONE");
        }
        
        add(sidePanel, BorderLayout.EAST);

        // Modules Panel
        JPanel modulesPanel = new JPanel(new GridLayout(0, 3, 15, 15)); // Auto rows, 3 cols
        modulesPanel.setBackground(Theme.PANEL_BG);
        modulesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(modulesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Add Modules dynamically based on level count
        Random rand = new Random();
        for (int i = 0; i < level.getModuleCount(); i++) {
            int type = rand.nextInt(10); // Increased range to 10
            BombModule module;
            switch (type) {
                case 0: module = new WiresModule(bomb); break;
                case 1: module = new ButtonModule(bomb); break;
                case 2: module = new KeypadModule(bomb); break;
                case 3: module = new SimonSaysModule(bomb); break;
                case 4: module = new PasswordModule(bomb); break;
                case 5: module = new MazeModule(bomb); break;
                case 6: module = new MorseCodeModule(bomb); break;
                case 7: module = new MemoryModule(bomb); break;
                case 8: module = new WhosOnFirstModule(bomb); break;
                case 9: module = new ComplicatedWiresModule(bomb); break;
                default: module = new WiresModule(bomb); break;
            }
            addModuleToGame(module, modulesPanel);
        }

        updateStrikes(0); // Init label
        bomb.start();
    }

    private void addModuleToGame(BombModule module, JPanel container) {
        bomb.addModule(module);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Theme.TEXT_SECONDARY), 
            module.getName(),
            0, 0, Theme.FONT_BOLD, Theme.TEXT_PRIMARY
        ));
        wrapper.setBackground(Theme.BG_COLOR);
        wrapper.add(module.getPanel(), BorderLayout.CENTER);
        wrapper.setPreferredSize(new Dimension(220, 220));
        container.add(wrapper);
    }

    public void updateTimer(int secondsRemaining) {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        if (secondsRemaining < 60) {
            timerLabel.setForeground(secondsRemaining % 2 == 0 ? Theme.ACCENT_RED : Color.WHITE);
        }
    }

    public void updateStrikes(int strikes) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<strikes; i++) sb.append("X ");
        for(int i=strikes; i<bomb.getMaxStrikes(); i++) sb.append("- ");
        strikesLabel.setText(sb.toString());
    }

    public void onExplode(String reason) {
        // Stop the bomb timer
        bomb.stop();
        
        // Trigger visual explosion
        app.triggerExplosion();

        // Explosion Effect
        Timer explosionTimer = new Timer(40, new ActionListener() {
            int count = 0;
            boolean red = true;
            Point originalLoc = app.getLocation();
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count > 40) { // Longer explosion
                    ((Timer)e.getSource()).stop();
                    app.setLocation(originalLoc); // Reset location
                    setBackground(Theme.BG_COLOR); // Reset background
                    showGameOverScreen(reason, false);
                    return;
                }
                
                // Flash background
                setBackground(red ? Theme.DANGER_RED : Color.BLACK);
                red = !red;
                
                // Shake window
                int intensity = 30; // More intense
                int xOffset = (int)(Math.random() * intensity - intensity/2);
                int yOffset = (int)(Math.random() * intensity - intensity/2);
                app.setLocation(originalLoc.x + xOffset, originalLoc.y + yOffset);
                
                count++;
            }
        });
        explosionTimer.start();
    }

    public void onDefused() {
        showGameOverScreen("Bomb Defused!", true);
    }

    private void showGameOverScreen(String message, boolean won) {
        removeAll();
        setLayout(new GridBagLayout());
        setBackground(won ? new Color(39, 174, 96) : new Color(192, 57, 43));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 0, 20, 0);
        
        JLabel statusLabel = new JLabel(won ? "MISSION ACCOMPLISHED" : "MISSION FAILED");
        statusLabel.setFont(Theme.FONT_TITLE);
        statusLabel.setForeground(Color.WHITE);
        add(statusLabel, gbc);
        
        JLabel reasonLabel = new JLabel(message);
        reasonLabel.setFont(Theme.FONT_SUBTITLE);
        reasonLabel.setForeground(Color.WHITE);
        add(reasonLabel, gbc);
        
        if (won) {
            JLabel timeLabel = new JLabel("Time Remaining: " + timerLabel.getText());
            timeLabel.setFont(Theme.FONT_MONO);
            timeLabel.setForeground(Color.WHITE);
            add(timeLabel, gbc);
        }
        
        JButton backButton = Theme.createButton("RETURN TO MENU");
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(won ? new Color(39, 174, 96) : new Color(192, 57, 43));
        backButton.addActionListener(e -> app.endGame());
        add(backButton, gbc);
        
        revalidate();
        repaint();
    }

    private void addInfoLabel(JPanel panel, String title, String value) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_BOLD.deriveFont(12f));
        titleLabel.setForeground(Theme.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(Theme.FONT_DIGITAL.deriveFont(20f));
        valueLabel.setForeground(Theme.ACCENT_ORANGE);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(valueLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }
}