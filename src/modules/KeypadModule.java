package modules;

import game.Bomb;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeypadModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<String> currentSymbols;
    private List<String> sortedSymbols;
    private int currentStage = 0;

    // Simplified symbol sets (using Unicode characters)
    private static final String[][] COLUMNS = {
        {"Ϙ", "Ѧ", "ƛ", "Ϟ", "Ѭ", "ϗ", "Ͽ"},
        {"Ӭ", "Ϙ", "Ͽ", "Ҩ", "☆", "ϗ", "¿"},
        {"©", "Ѽ", "Ҩ", "Ж", "R", "ƛ", "☆"},
        {"б", "¶", "b", "Ѭ", "Ж", "¿", "☺"}
    };

    public KeypadModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
    }

    private void generatePuzzle() {
        // Pick a random column
        int colIndex = (int) (Math.random() * COLUMNS.length);
        String[] column = COLUMNS[colIndex];

        // Pick 4 random symbols from that column
        List<String> colList = new ArrayList<>();
        Collections.addAll(colList, column);
        Collections.shuffle(colList);
        
        currentSymbols = new ArrayList<>(colList.subList(0, 4));
        
        // The correct order is the order they appear in the original column
        sortedSymbols = new ArrayList<>();
        for (String sym : column) {
            if (currentSymbols.contains(sym)) {
                sortedSymbols.add(sym);
            }
        }
    }

    private void setupUI() {
        panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (String symbol : currentSymbols) {
            JButton btn = new JButton(symbol);
            btn.setFont(new Font("Serif", Font.BOLD, 24));
            btn.setFocusable(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Add cursor pointer
            btn.addActionListener(e -> handlePress(symbol, btn));
            panel.add(btn);
        }
    }

    private void handlePress(String symbol, JButton btn) {
        if (solved) return;

        String expected = sortedSymbols.get(currentStage);
        if (symbol.equals(expected)) {
            btn.setEnabled(false);
            btn.setBackground(Color.GREEN);
            currentStage++;
            if (currentStage >= 4) {
                solved = true;
                panel.setBackground(Color.GREEN);
                bomb.checkDefused();
            }
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
        return "Keypad";
    }
}