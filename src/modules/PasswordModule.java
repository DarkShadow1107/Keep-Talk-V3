package modules;

import game.Bomb;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.*;

/**
 * Modulul "Parolă" (Password).
 * Jucătorul trebuie să găsească un cuvânt din 5 litere dintr-o listă limitată.
 * Fiecare coloană are 6 litere prin care se poate naviga folosind butoanele sus/jos.
 */
public class PasswordModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    
    /** Lista oficială de parole posibile conform manualului original. */
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

    /**
     * Alege o parolă țintă și generează seturi de litere aleatorii pentru fiecare coloană,
     * asigurându-se că parola țintă poate fi formată.
     */
    private void generatePuzzle() {
        Random rand = new Random();
        targetPassword = possiblePasswords.get(rand.nextInt(possiblePasswords.size()));
        columns = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            List<Character> col = new ArrayList<>();
            col.add(targetPassword.charAt(i)); // Literă corectă obligatorie în coloană
            while (col.size() < 6) {
                char c = (char) ('A' + rand.nextInt(26));
                if (!col.contains(c)) {
                    col.add(c);
                }
            }
            col.sort(Character::compareTo); // Sortăm literele alfabetic pentru jucător
            columns.add(col);
            currentIndices[i] = rand.nextInt(6); // Poziție de start aleatorie
        }
    }

    /**
     * Configurează interfața grafică cu butoane de navigare și afișajul literelor.
     */
    private void setupUI() {
        panel = new JPanel(new GridLayout(3, 5, 2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Săgeți Sus
        for (int i = 0; i < 5; i++) {
            final int colIndex = i;
            JButton upBtn = new JButton("▲");
            upBtn.setMargin(new Insets(0,0,0,0));
            upBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
            upBtn.addActionListener(e -> cycleColumn(colIndex, -1));
            panel.add(upBtn);
        }

        // Literele curente selectate
        for (int i = 0; i < 5; i++) {
            letterLabels[i] = new JLabel(String.valueOf(columns.get(i).get(currentIndices[i])), SwingConstants.CENTER);
            letterLabels[i].setFont(new Font("Monospaced", Font.BOLD, 24));
            letterLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(letterLabels[i]);
        }

        // Săgeți Jos
        for (int i = 0; i < 5; i++) {
            final int colIndex = i;
            JButton downBtn = new JButton("▼");
            downBtn.setMargin(new Insets(0,0,0,0));
            downBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
            downBtn.addActionListener(e -> cycleColumn(colIndex, 1));
            panel.add(downBtn);
        }
        
        // Înfășurăm panel-ul într-unul principal pentru a adăuga butonul SUBMIT
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        
        JButton submitBtn = new JButton("SUBMIT");
        submitBtn.addActionListener(e -> checkSolution());
        mainPanel.add(submitBtn, BorderLayout.SOUTH);
        
        panel = mainPanel;
        panel.setPreferredSize(new Dimension(180, 180));
    }

    /**
     * Schimbă litera afișată într-o coloană prin rotație.
     */
    private void cycleColumn(int col, int direction) {
        if (solved) return;
        currentIndices[col] = (currentIndices[col] + direction + 6) % 6;
        letterLabels[col].setText(String.valueOf(columns.get(col).get(currentIndices[col])));
    }

    /**
     * Verifică dacă cuvântul format din literele selectate este cel corect.
     */
    private void checkSolution() {
        if (solved) return;
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(columns.get(i).get(currentIndices[i]));
        }
        
        if (sb.toString().equals(targetPassword)) {
            // Rezolvat: colorăm fundalul în verde și dezactivăm butoanele
            solved = true;
            panel.setBackground(Color.GREEN);
            for(Component c : panel.getComponents()) c.setEnabled(false);
            bomb.checkDefused();
        } else {
            bomb.addStrike(); // Greșeală dacă cuvântul nu este cel corect
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
