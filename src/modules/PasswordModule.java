package modules;

import game.Bomb;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PasswordModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<String> possiblePasswords = Arrays.asList(
        "ABOUT", "AFTER", "AGAIN", "BELOW", "COULD", "EVERY", "FIRST", "FOUND", "GREAT", "HOUSE",
        "LARGE", "LEARN", "NEVER", "OTHER", "PLACE", "PLANT", "POINT", "RIGHT", "SMALL", "SOUND",
        "SPELL", "STILL", "STUDY", "THEIR", "THERE", "THESE", "THING", "THINK", "THREE", "WATER",
        "WHERE", "WHICH", "WORLD", "WOULD", "WRITE"
    );
    private String targetPassword;
    private List<List<Character>> columns;
    private int[] currentIndices = new int[5];
    private JLabel[] letterLabels = new JLabel[5];

    public PasswordModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
    }

    private void generatePuzzle() {
        Random rand = new Random();
        targetPassword = possiblePasswords.get(rand.nextInt(possiblePasswords.size()));
        columns = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            List<Character> col = new ArrayList<>();
            col.add(targetPassword.charAt(i)); // Ensure correct letter is present
            while (col.size() < 6) {
                char c = (char) ('A' + rand.nextInt(26));
                if (!col.contains(c)) {
                    col.add(c);
                }
            }
            col.sort(Character::compareTo);
            columns.add(col);
            currentIndices[i] = rand.nextInt(6); // Start at random position
        }
    }

    private void setupUI() {
        panel = new JPanel(new GridLayout(3, 5, 2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Top arrows
        for (int i = 0; i < 5; i++) {
            final int colIndex = i;
            JButton upBtn = new JButton("▲");
            upBtn.setMargin(new Insets(0,0,0,0));
            upBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
            upBtn.addActionListener(e -> cycleColumn(colIndex, -1));
            panel.add(upBtn);
        }

        // Letters
        for (int i = 0; i < 5; i++) {
            letterLabels[i] = new JLabel(String.valueOf(columns.get(i).get(currentIndices[i])), SwingConstants.CENTER);
            letterLabels[i].setFont(new Font("Monospaced", Font.BOLD, 24));
            letterLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(letterLabels[i]);
        }

        // Bottom arrows
        for (int i = 0; i < 5; i++) {
            final int colIndex = i;
            JButton downBtn = new JButton("▼");
            downBtn.setMargin(new Insets(0,0,0,0));
            downBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
            downBtn.addActionListener(e -> cycleColumn(colIndex, 1));
            panel.add(downBtn);
        }
        
        // Submit button (added to bottom of module wrapper usually, but here we can just check on change or add a button)
        // The real game has a submit button below the letters.
        // Let's change layout to BorderLayout to accommodate a submit button.
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        
        JButton submitBtn = new JButton("SUBMIT");
        submitBtn.addActionListener(e -> checkSolution());
        mainPanel.add(submitBtn, BorderLayout.SOUTH);
        
        // Re-assign panel to this wrapper
        panel = mainPanel;
    }

    private void cycleColumn(int col, int direction) {
        if (solved) return;
        currentIndices[col] = (currentIndices[col] + direction + 6) % 6;
        letterLabels[col].setText(String.valueOf(columns.get(col).get(currentIndices[col])));
    }

    private void checkSolution() {
        if (solved) return;
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(columns.get(i).get(currentIndices[i]));
        }
        
        if (sb.toString().equals(targetPassword)) {
            solved = true;
            panel.setBackground(Color.GREEN);
            for(Component c : panel.getComponents()) c.setEnabled(false);
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
        return game.Localization.get("MOD_PASSWORD");
    }
}