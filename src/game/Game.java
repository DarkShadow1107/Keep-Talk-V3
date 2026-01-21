package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import modules.*;

public class Game extends JPanel {
    private JLabel timerLabel;
    private JPanel strikesPanel;
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
        topPanel.setBackground(new Color(10, 10, 10));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(50, 50, 50)),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        // Timer
        timerLabel = new JLabel("00:00", SwingConstants.CENTER);
        timerLabel.setFont(Theme.FONT_DIGITAL.deriveFont(56f));
        timerLabel.setForeground(Theme.ACCENT_RED);
        timerLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 0, 0), 2),
            BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));
        timerLabel.setBackground(new Color(20, 0, 0));
        timerLabel.setOpaque(true);
        topPanel.add(timerLabel, BorderLayout.CENTER);

        // Strikes
        strikesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        strikesPanel.setBackground(new Color(10, 10, 10));
        updateStrikes(0);
        topPanel.add(strikesPanel, BorderLayout.EAST);

        // Abort Button
        JButton abortButton = Theme.createButton(Localization.get("GAME_ABORT"));
        abortButton.setBackground(Theme.DANGER_RED);
        abortButton.setPreferredSize(new Dimension(140, 50));
        abortButton.addActionListener(e -> onExplode(Localization.get("GAME_ABORTED")));
        topPanel.add(abortButton, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // Side Panel for Bomb Info
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Theme.PANEL_BG);
        sidePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 2, 0, 0, Theme.PANEL_BORDER),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        sidePanel.setPreferredSize(new Dimension(240, 0));

        addInfoLabel(sidePanel, Localization.get("GAME_SERIAL"), bomb.getSerialNumber());
        addInfoLabel(sidePanel, Localization.get("GAME_BATTERIES"), String.valueOf(bomb.getBatteries()));
        addInfoLabel(sidePanel, Localization.get("GAME_PARALLEL"), bomb.hasParallelPort() ? Localization.get("GAME_YES") : Localization.get("GAME_NO"));
        
        List<String> indicators = bomb.getIndicators();
        if (!indicators.isEmpty()) {
            addInfoLabel(sidePanel, Localization.get("GAME_INDICATORS"), String.join(", ", indicators));
        } else {
            addInfoLabel(sidePanel, Localization.get("GAME_INDICATORS"), Localization.get("GAME_NONE"));
        }
        
        add(sidePanel, BorderLayout.EAST);

        // Modules Panel
        JPanel modulesPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // Auto rows, 3 cols
        modulesPanel.setBackground(Theme.BG_COLOR);
        modulesPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JScrollPane scrollPane = new JScrollPane(modulesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(Theme.BG_COLOR);
        Theme.customizeScrollBar(scrollPane);
        add(scrollPane, BorderLayout.CENTER);

        // Add Modules dynamically based on level count
        Random rand = new Random();
        for (int i = 0; i < level.getModuleCount(); i++) {
            int type = rand.nextInt(12); // Increased range for new modules
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
                case 10: module = new BinaryModule(bomb); break;
                case 11: module = new LogicModule(bomb); break;
                default: module = new WiresModule(bomb); break;
            }
            addModuleToGame(module, modulesPanel);
        }

        bomb.start();
    }

    private void addModuleToGame(BombModule module, JPanel container) {
        bomb.addModule(module);
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw metallic border
                g2.setColor(Theme.PANEL_BORDER);
                g2.setStroke(new BasicStroke(4));
                g2.drawRect(2, 2, getWidth()-4, getHeight()-4);
                
                // Draw screws
                Theme.drawScrew(g2, 8, 8);
                Theme.drawScrew(g2, getWidth()-18, 8);
                Theme.drawScrew(g2, 8, getHeight()-18);
                Theme.drawScrew(g2, getWidth()-18, getHeight()-18);

                // Draw Status LED in the wrapper (Top Right, slightly left of screw)
                Theme.drawLed(g2, getWidth() - 45, 12, module.isSolved(), true);
            }
        };
        wrapper.setBackground(Theme.PANEL_BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Module Title
        JLabel title = new JLabel(module.getName());
        title.setFont(Theme.FONT_BOLD.deriveFont(14f));
        title.setForeground(Theme.TEXT_SECONDARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        wrapper.add(title, BorderLayout.NORTH);
        
        JPanel modulePanel = module.getPanel();
        modulePanel.setPreferredSize(new Dimension(180, 180));
        modulePanel.setMinimumSize(new Dimension(180, 180));
        modulePanel.setMaximumSize(new Dimension(180, 180));
        
        wrapper.add(modulePanel, BorderLayout.CENTER);
        wrapper.setPreferredSize(new Dimension(220, 260)); // Fixed identical size
        container.add(wrapper);
    }

    public void updateTimer(int secondsRemaining) {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        if (secondsRemaining < 60) {
            timerLabel.setForeground(secondsRemaining % 2 == 0 ? Theme.ACCENT_RED : new Color(100, 0, 0));
        }
    }

    public void updateStrikes(int strikes) {
        strikesPanel.removeAll();
        for(int i=0; i<bomb.getMaxStrikes(); i++) {
            JLabel xLabel = new JLabel("X");
            xLabel.setFont(Theme.FONT_TITLE);
            if (i < strikes) {
                xLabel.setForeground(Theme.ACCENT_RED);
            } else {
                xLabel.setForeground(new Color(50, 0, 0));
            }
            strikesPanel.add(xLabel);
        }
        strikesPanel.revalidate();
        strikesPanel.repaint();
        this.repaint();
    }

    public void onExplode(String reason) {
        bomb.stop();
        app.triggerExplosion();

        Timer explosionTimer = new Timer(40, new ActionListener() {
            int count = 0;
            boolean red = true;
            Point originalLoc = app.getLocation();
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count > 50) {
                    ((Timer)e.getSource()).stop();
                    app.setLocation(originalLoc);
                    setBackground(Theme.BG_COLOR);
                    showGameOverScreen(reason, false);
                    return;
                }
                
                setBackground(red ? Theme.DANGER_RED : Color.BLACK);
                red = !red;
                
                int intensity = 40;
                int xOffset = (int)(Math.random() * intensity - intensity/2);
                int yOffset = (int)(Math.random() * intensity - intensity/2);
                app.setLocation(originalLoc.x + xOffset, originalLoc.y + yOffset);
                
                count++;
            }
        });
        explosionTimer.start();
    }

    public void onDefused() {
        showGameOverScreen(Localization.get("GAME_DEFUSED"), true);
    }

    private void showGameOverScreen(String message, boolean won) {
        removeAll();
        setLayout(new GridBagLayout());
        setBackground(won ? new Color(39, 174, 96) : new Color(192, 57, 43));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 0, 20, 0);
        
        JLabel statusLabel = new JLabel(won ? Localization.get("GAME_WIN") : Localization.get("GAME_LOSE"));
        statusLabel.setFont(Theme.FONT_TITLE);
        statusLabel.setForeground(Color.WHITE);
        add(statusLabel, gbc);
        
        JLabel reasonLabel = new JLabel(message);
        reasonLabel.setFont(Theme.FONT_SUBTITLE);
        reasonLabel.setForeground(Color.WHITE);
        add(reasonLabel, gbc);
        
        if (won) {
            JLabel timeLabel = new JLabel(Localization.get("LBL_TIME_REMAINING") + timerLabel.getText());
            timeLabel.setFont(Theme.FONT_MONO);
            timeLabel.setForeground(Color.WHITE);
            add(timeLabel, gbc);
        }
        
        JButton backButton = Theme.createButton(Localization.get("BTN_RETURN"));
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
        valueLabel.setFont(Theme.FONT_DIGITAL.deriveFont(24f));
        valueLabel.setForeground(Theme.ACCENT_ORANGE);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(valueLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }
}