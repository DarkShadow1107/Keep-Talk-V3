package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemoryModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private int stage = 1;
    private JLabel displayLabel;
    private JButton[] buttons;
    private List<Integer> stageLabels = new ArrayList<>();
    private List<Integer> stagePositions = new ArrayList<>();
    
    // History of correct presses (label and position) for each stage
    // Index 0 = Stage 1, etc.
    private int[] correctLabels = new int[5];
    private int[] correctPositions = new int[5];

    public MemoryModule(Bomb bomb) {
        this.bomb = bomb;
        setupUI();
        startStage();
    }

    private void setupUI() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.PANEL_BG);

        displayLabel = new JLabel("1", SwingConstants.CENTER);
        displayLabel.setFont(Theme.FONT_DIGITAL.deriveFont(48f));
        displayLabel.setForeground(Theme.TEXT_PRIMARY);
        displayLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(displayLabel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        btnPanel.setBackground(Theme.PANEL_BG);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        buttons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton("");
            btn.setFont(Theme.FONT_BOLD.deriveFont(24f));
            btn.setBackground(Theme.PANEL_BG.brighter());
            btn.setForeground(Theme.TEXT_PRIMARY);
            final int pos = i;
            btn.addActionListener(e -> handlePress(pos));
            buttons[i] = btn;
            btnPanel.add(btn);
        }
        panel.add(btnPanel, BorderLayout.CENTER);
        
        // Stage indicators
        JPanel stagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        stagePanel.setBackground(Theme.PANEL_BG);
        for(int i=0; i<5; i++) {
            JPanel dot = new JPanel();
            dot.setPreferredSize(new Dimension(10, 10));
            dot.setBackground(Color.GRAY);
            stagePanel.add(dot);
        }
        panel.add(stagePanel, BorderLayout.SOUTH);
    }

    private void startStage() {
        Random rand = new Random();
        int displayVal = rand.nextInt(4) + 1;
        displayLabel.setText(String.valueOf(displayVal));
        
        List<Integer> labels = new ArrayList<>();
        for (int i = 1; i <= 4; i++) labels.add(i);
        java.util.Collections.shuffle(labels);
        
        for (int i = 0; i < 4; i++) {
            buttons[i].setText(String.valueOf(labels.get(i)));
        }
        
        // Determine correct answer logic
        int correctPos = -1;
        int correctLbl = -1;
        
        switch (stage) {
            case 1:
                if (displayVal == 1) correctPos = 1; // 2nd pos
                else if (displayVal == 2) correctPos = 1;
                else if (displayVal == 3) correctPos = 2; // 3rd pos
                else correctPos = 3; // 4th pos
                break;
            case 2:
                if (displayVal == 1) correctLbl = 4;
                else if (displayVal == 2) correctPos = correctPositions[0];
                else if (displayVal == 3) correctPos = 0;
                else correctPos = correctPositions[0];
                break;
            case 3:
                if (displayVal == 1) correctLbl = correctLabels[1];
                else if (displayVal == 2) correctLbl = correctLabels[0];
                else if (displayVal == 3) correctPos = 2;
                else correctLbl = 4;
                break;
            case 4:
                if (displayVal == 1) correctPos = correctPositions[0];
                else if (displayVal == 2) correctPos = 0;
                else if (displayVal == 3) correctPos = correctPositions[1];
                else correctPos = correctPositions[1];
                break;
            case 5:
                if (displayVal == 1) correctLbl = correctLabels[0];
                else if (displayVal == 2) correctLbl = correctLabels[1];
                else if (displayVal == 3) correctLbl = correctLabels[3];
                else correctLbl = correctLabels[2];
                break;
        }
        
        // Resolve Pos vs Label
        if (correctPos != -1) {
            correctLbl = Integer.parseInt(buttons[correctPos].getText());
        } else {
            // Find pos for label
            for(int i=0; i<4; i++) {
                if (Integer.parseInt(buttons[i].getText()) == correctLbl) {
                    correctPos = i;
                    break;
                }
            }
        }
        
        // Store correct answer for this stage (but don't save to history yet)
        // We save to history only on successful press.
        // Wait, we need to know what the correct answer IS to check it.
        // So we store it in a temp variable? No, we can just recalculate or store it in the class.
        // Let's store the expected correct values for validation.
        this.expectedPos = correctPos;
        this.expectedLbl = correctLbl;
    }
    
    private int expectedPos;
    private int expectedLbl;

    private void handlePress(int pos) {
        if (solved) return;
        
        int pressedLbl = Integer.parseInt(buttons[pos].getText());
        
        if (pos == expectedPos && pressedLbl == expectedLbl) {
            // Correct
            correctPositions[stage-1] = pos;
            correctLabels[stage-1] = pressedLbl;
            
            // Update stage lights
            JPanel stagePanel = (JPanel) panel.getComponent(2);
            stagePanel.getComponent(stage-1).setBackground(Theme.ACCENT_GREEN);
            
            stage++;
            if (stage > 5) {
                solved = true;
                panel.setBackground(Theme.ACCENT_GREEN);
                bomb.checkDefused();
            } else {
                startStage();
            }
        } else {
            // Wrong
            bomb.addStrike();
            stage = 1;
            // Reset lights
            JPanel stagePanel = (JPanel) panel.getComponent(2);
            for(Component c : stagePanel.getComponents()) c.setBackground(Color.GRAY);
            // Clear history? The game resets stage to 1 but keeps history? No, resets completely.
            correctPositions = new int[5];
            correctLabels = new int[5];
            startStage();
        }
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public boolean isSolved() {
        return solved;
    }

    @Override
    public void onStrike() {}

    @Override
    public void onSolve() {}

    @Override
    public String getName() {
        return "Memory";
    }
}
