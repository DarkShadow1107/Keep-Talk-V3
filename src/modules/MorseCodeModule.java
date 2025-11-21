package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MorseCodeModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private String targetWord;
    private int currentFreqIndex = 0;
    private JLabel freqLabel;
    private JPanel lightPanel;
    private Timer flashTimer;
    
    private static final String[] WORDS = {"SHELL", "HALLS", "SLICK", "TRICK", "BOXES", "LEAKS", "STROBE", "BISTRO", "FLICK", "BOMBS", "BREAK", "BRICK", "STEAK", "STING", "VECTOR", "BEATS"};
    private static final Map<String, String> MORSE_CODE = new HashMap<>();
    private static final Map<String, Double> FREQUENCIES = new HashMap<>();

    static {
        MORSE_CODE.put("A", ".-"); MORSE_CODE.put("B", "-..."); MORSE_CODE.put("C", "-.-.");
        MORSE_CODE.put("D", "-.."); MORSE_CODE.put("E", "."); MORSE_CODE.put("F", "..-.");
        MORSE_CODE.put("G", "--."); MORSE_CODE.put("H", "...."); MORSE_CODE.put("I", "..");
        MORSE_CODE.put("J", ".---"); MORSE_CODE.put("K", "-.-"); MORSE_CODE.put("L", ".-..");
        MORSE_CODE.put("M", "--"); MORSE_CODE.put("N", "-."); MORSE_CODE.put("O", "---");
        MORSE_CODE.put("P", ".--."); MORSE_CODE.put("Q", "--.-"); MORSE_CODE.put("R", ".-.");
        MORSE_CODE.put("S", "..."); MORSE_CODE.put("T", "-"); MORSE_CODE.put("U", "..-");
        MORSE_CODE.put("V", "...-"); MORSE_CODE.put("W", ".--"); MORSE_CODE.put("X", "-..-");
        MORSE_CODE.put("Y", "-.--"); MORSE_CODE.put("Z", "--..");

        FREQUENCIES.put("SHELL", 3.505); FREQUENCIES.put("HALLS", 3.515);
        FREQUENCIES.put("SLICK", 3.522); FREQUENCIES.put("TRICK", 3.532);
        FREQUENCIES.put("BOXES", 3.535); FREQUENCIES.put("LEAKS", 3.542);
        FREQUENCIES.put("STROBE", 3.545); FREQUENCIES.put("BISTRO", 3.552);
        FREQUENCIES.put("FLICK", 3.555); FREQUENCIES.put("BOMBS", 3.565);
        FREQUENCIES.put("BREAK", 3.572); FREQUENCIES.put("BRICK", 3.575);
        FREQUENCIES.put("STEAK", 3.582); FREQUENCIES.put("STING", 3.592);
        FREQUENCIES.put("VECTOR", 3.595); FREQUENCIES.put("BEATS", 3.600);
    }

    public MorseCodeModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
        startFlashing();
    }

    private void generatePuzzle() {
        Random rand = new Random();
        targetWord = WORDS[rand.nextInt(WORDS.length)];
    }

    private void setupUI() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.PANEL_BG);

        lightPanel = new JPanel();
        lightPanel.setPreferredSize(new Dimension(50, 50));
        lightPanel.setBackground(Color.BLACK);
        lightPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBackground(Theme.PANEL_BG);
        centerPanel.add(lightPanel);
        
        panel.add(centerPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(Theme.PANEL_BG);

        freqLabel = new JLabel("3.505 MHz", SwingConstants.CENTER);
        freqLabel.setFont(Theme.FONT_DIGITAL.deriveFont(24f));
        freqLabel.setForeground(Theme.ACCENT_ORANGE);
        
        JButton leftBtn = new JButton("<");
        leftBtn.addActionListener(e -> changeFreq(-1));
        
        JButton rightBtn = new JButton(">");
        rightBtn.addActionListener(e -> changeFreq(1));
        
        JButton txBtn = new JButton("TX");
        txBtn.setBackground(Theme.ACCENT_ORANGE);
        txBtn.addActionListener(e -> checkSolution());

        controlPanel.add(leftBtn, BorderLayout.WEST);
        controlPanel.add(freqLabel, BorderLayout.CENTER);
        controlPanel.add(rightBtn, BorderLayout.EAST);
        controlPanel.add(txBtn, BorderLayout.SOUTH);

        panel.add(controlPanel, BorderLayout.CENTER);
    }

    private void changeFreq(int dir) {
        // Simplified frequency list for UI
        // In real game, it cycles through valid frequencies.
        // Here we just cycle through the WORDS array indices to show frequencies.
        currentFreqIndex = (currentFreqIndex + dir + WORDS.length) % WORDS.length;
        String word = WORDS[currentFreqIndex];
        freqLabel.setText(String.format("%.3f MHz", FREQUENCIES.get(word)));
    }

    private void startFlashing() {
        StringBuilder sequence = new StringBuilder();
        for (char c : targetWord.toCharArray()) {
            sequence.append(MORSE_CODE.get(String.valueOf(c))).append(" ");
        }
        
        // Convert to timing sequence: . = 1 unit on, 1 unit off. - = 3 units on, 1 unit off. Space = 3 units off.
        // Simplified: We will use a tick based system.
        // Let's just flash the light.
        
        final int DOT = 200;
        
        new Thread(() -> {
            while (!solved) {
                for (char c : targetWord.toCharArray()) {
                    String code = MORSE_CODE.get(String.valueOf(c));
                    for (char s : code.toCharArray()) {
                        if (solved) return;
                        lightPanel.setBackground(Theme.ACCENT_ORANGE);
                        try { Thread.sleep(s == '.' ? DOT : DOT * 3); } catch (InterruptedException e) {}
                        lightPanel.setBackground(Color.BLACK);
                        try { Thread.sleep(DOT); } catch (InterruptedException e) {}
                    }
                    try { Thread.sleep(DOT * 3); } catch (InterruptedException e) {} // Letter gap
                }
                try { Thread.sleep(DOT * 7); } catch (InterruptedException e) {} // Word gap
            }
        }).start();
    }

    private void checkSolution() {
        if (solved) return;
        
        String selectedWord = WORDS[currentFreqIndex];
        if (selectedWord.equals(targetWord)) {
            solved = true;
            panel.setBackground(Theme.ACCENT_GREEN);
            bomb.checkDefused();
        } else {
            bomb.addStrike();
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
        return "Morse Code";
    }
}