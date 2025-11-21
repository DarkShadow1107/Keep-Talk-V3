package modules;

import game.Bomb;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimonSaysModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<Integer> sequence;
    private List<Integer> inputSequence;
    private int stage = 0;
    private JButton[] buttons;
    private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
    private Color[] flashColors = {new Color(255, 100, 100), new Color(100, 100, 255), new Color(100, 255, 100), new Color(255, 255, 100)};
    private Timer flashTimer;
    private boolean showingSequence = false;

    public SimonSaysModule(Bomb bomb) {
        this.bomb = bomb;
        sequence = new ArrayList<>();
        inputSequence = new ArrayList<>();
        buttons = new JButton[4];
        
        setupUI();
        addToSequence();
        startFlashing();
    }

    private void setupUI() {
        panel = new JPanel(new GridLayout(2, 2, 5, 5));
        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton();
            btn.setBackground(colors[i].darker().darker());
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Add cursor pointer
            final int index = i;
            btn.addActionListener(e -> handleInput(index));
            buttons[i] = btn;
            panel.add(btn);
        }
    }

    private void addToSequence() {
        Random rand = new Random();
        sequence.add(rand.nextInt(4));
    }

    private void startFlashing() {
        showingSequence = true;
        final int[] flashIndex = {0};
        
        if (flashTimer != null && flashTimer.isRunning()) flashTimer.stop();

        flashTimer = new Timer(800, new ActionListener() {
            boolean on = true;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (solved) {
                    ((Timer)e.getSource()).stop();
                    return;
                }

                // Reset all to dark
                for (int i = 0; i < 4; i++) buttons[i].setBackground(colors[i].darker().darker());

                if (flashIndex[0] >= sequence.size()) {
                    flashIndex[0] = 0; // Loop sequence
                    // Add a pause
                    return;
                }

                if (on) {
                    int btnIndex = sequence.get(flashIndex[0]);
                    buttons[btnIndex].setBackground(flashColors[btnIndex]);
                    on = false;
                } else {
                    on = true;
                    flashIndex[0]++;
                }
            }
        });
        flashTimer.start();
    }

    private void handleInput(int index) {
        if (solved) return;
        
        // Visual feedback
        buttons[index].setBackground(flashColors[index]);
        Timer reset = new Timer(200, e -> buttons[index].setBackground(colors[index].darker().darker()));
        reset.setRepeats(false);
        reset.start();

        int expectedFlash = sequence.get(inputSequence.size());
        int strikes = bomb.getStrikes();
        int requiredInput = getCorrectButton(expectedFlash, strikes);

        if (index == requiredInput) {
            inputSequence.add(index);
            if (inputSequence.size() == sequence.size()) {
                // Stage complete
                stage++;
                inputSequence.clear();
                if (stage >= 3) { // 3 stages to win
                    solved = true;
                    flashTimer.stop();
                    for(JButton b : buttons) b.setBackground(Color.GREEN);
                    bomb.checkDefused();
                } else {
                    addToSequence();
                }
            }
        } else {
            bomb.addStrike();
            inputSequence.clear();
            // Sequence continues flashing
        }
    }

    private int getCorrectButton(int flashIndex, int strikes) {
        boolean hasVowel = hasVowel(bomb.getSerialNumber());
        // 0:Red, 1:Blue, 2:Green, 3:Yellow
        
        if (hasVowel) {
            if (strikes == 0) {
                switch (flashIndex) {
                    case 0: return 1; // Red -> Blue
                    case 1: return 0; // Blue -> Red
                    case 2: return 3; // Green -> Yellow
                    case 3: return 2; // Yellow -> Green
                }
            } else if (strikes == 1) {
                switch (flashIndex) {
                    case 0: return 3; // Red -> Yellow
                    case 1: return 2; // Blue -> Green
                    case 2: return 1; // Green -> Blue
                    case 3: return 0; // Yellow -> Red
                }
            } else { // 2+ strikes
                switch (flashIndex) {
                    case 0: return 2; // Red -> Green
                    case 1: return 0; // Blue -> Red
                    case 2: return 3; // Green -> Yellow
                    case 3: return 1; // Yellow -> Blue
                }
            }
        } else { // No Vowel
            if (strikes == 0) {
                switch (flashIndex) {
                    case 0: return 1; // Red -> Blue
                    case 1: return 3; // Blue -> Yellow
                    case 2: return 2; // Green -> Green
                    case 3: return 0; // Yellow -> Red
                }
            } else if (strikes == 1) {
                switch (flashIndex) {
                    case 0: return 0; // Red -> Red
                    case 1: return 1; // Blue -> Blue
                    case 2: return 3; // Green -> Yellow
                    case 3: return 2; // Yellow -> Green
                }
            } else { // 2+ strikes
                switch (flashIndex) {
                    case 0: return 3; // Red -> Yellow
                    case 1: return 2; // Blue -> Green
                    case 2: return 1; // Green -> Blue
                    case 3: return 0; // Yellow -> Red
                }
            }
        }
        return flashIndex; // Fallback
    }

    private boolean hasVowel(String serial) {
        String vowels = "AEIOU";
        for (char c : serial.toUpperCase().toCharArray()) {
            if (vowels.indexOf(c) >= 0) return true;
        }
        return false;
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
        return "Simon Says";
    }
}