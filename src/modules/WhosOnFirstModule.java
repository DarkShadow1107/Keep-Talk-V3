package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WhosOnFirstModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private int stage = 0;
    private JLabel displayLabel;
    private JButton[] buttons;
    
    // Data
    private static final String[] DISPLAY_WORDS = {
        "YES", "FIRST", "DISPLAY", "OKAY", "SAYS", "NOTHING", "", "BLANK", "NO", "LED", "LEAD", "READ", "RED", "REED", "LEED", "HOLD ON", "YOU", "YOU ARE", "YOUR", "YOU'RE", "UR", "THERE", "THEY'RE", "THEIR", "THEY ARE", "SEE", "C", "CEE"
    };
    
    private static final Map<String, Integer> DISPLAY_MAP = new HashMap<>();
    static {
        // Map display word to button index to look at (0=TL, 1=TR, 2=ML, 3=MR, 4=BL, 5=BR)
        DISPLAY_MAP.put("YES", 2); DISPLAY_MAP.put("FIRST", 1); DISPLAY_MAP.put("DISPLAY", 5);
        DISPLAY_MAP.put("OKAY", 1); DISPLAY_MAP.put("SAYS", 5); DISPLAY_MAP.put("NOTHING", 2);
        DISPLAY_MAP.put("", 4); DISPLAY_MAP.put("BLANK", 3); DISPLAY_MAP.put("NO", 5);
        DISPLAY_MAP.put("LED", 2); DISPLAY_MAP.put("LEAD", 5); DISPLAY_MAP.put("READ", 3);
        DISPLAY_MAP.put("RED", 3); DISPLAY_MAP.put("REED", 4); DISPLAY_MAP.put("LEED", 4);
        DISPLAY_MAP.put("HOLD ON", 5); DISPLAY_MAP.put("YOU", 3); DISPLAY_MAP.put("YOU ARE", 5);
        DISPLAY_MAP.put("YOUR", 3); DISPLAY_MAP.put("YOU'RE", 3); DISPLAY_MAP.put("UR", 0);
        DISPLAY_MAP.put("THERE", 5); DISPLAY_MAP.put("THEY'RE", 4); DISPLAY_MAP.put("THEIR", 3);
        DISPLAY_MAP.put("THEY ARE", 2); DISPLAY_MAP.put("SEE", 5); DISPLAY_MAP.put("C", 1);
        DISPLAY_MAP.put("CEE", 5);
    }
    
    private static final Map<String, List<String>> WORD_LISTS = new HashMap<>();
    static {
        WORD_LISTS.put("READY", Arrays.asList("YES", "OKAY", "WHAT", "MIDDLE", "LEFT", "PRESS", "RIGHT", "BLANK", "READY"));
        WORD_LISTS.put("FIRST", Arrays.asList("LEFT", "OKAY", "YES", "MIDDLE", "NO", "RIGHT", "NOTHING", "UHHH", "WAIT", "READY", "BLANK", "WHAT", "PRESS", "FIRST"));
        WORD_LISTS.put("NO", Arrays.asList("BLANK", "UHHH", "WAIT", "FIRST", "WHAT", "READY", "RIGHT", "YES", "NOTHING", "LEFT", "PRESS", "OKAY", "NO"));
        WORD_LISTS.put("BLANK", Arrays.asList("WAIT", "RIGHT", "OKAY", "MIDDLE", "BLANK"));
        WORD_LISTS.put("NOTHING", Arrays.asList("UHHH", "RIGHT", "OKAY", "MIDDLE", "YES", "BLANK", "NO", "PRESS", "LEFT", "WHAT", "WAIT", "FIRST", "NOTHING"));
        WORD_LISTS.put("YES", Arrays.asList("OKAY", "RIGHT", "UHHH", "MIDDLE", "FIRST", "WHAT", "PRESS", "READY", "NOTHING", "YES"));
        WORD_LISTS.put("WHAT", Arrays.asList("UHHH", "WHAT"));
        WORD_LISTS.put("UHHH", Arrays.asList("READY", "NOTHING", "LEFT", "WHAT", "OKAY", "YES", "RIGHT", "NO", "PRESS", "BLANK", "UHHH"));
        WORD_LISTS.put("LEFT", Arrays.asList("RIGHT", "LEFT"));
        WORD_LISTS.put("RIGHT", Arrays.asList("YES", "NOTHING", "READY", "PRESS", "NO", "WAIT", "WHAT", "RIGHT"));
        WORD_LISTS.put("MIDDLE", Arrays.asList("BLANK", "READY", "OKAY", "WHAT", "NOTHING", "PRESS", "NO", "WAIT", "LEFT", "MIDDLE"));
        WORD_LISTS.put("OKAY", Arrays.asList("MIDDLE", "NO", "FIRST", "YES", "UHHH", "NOTHING", "WAIT", "OKAY"));
        WORD_LISTS.put("WAIT", Arrays.asList("UHHH", "NO", "BLANK", "OKAY", "YES", "LEFT", "FIRST", "PRESS", "WHAT", "WAIT"));
        WORD_LISTS.put("PRESS", Arrays.asList("RIGHT", "MIDDLE", "YES", "READY", "PRESS"));
        WORD_LISTS.put("YOU", Arrays.asList("SURE", "YOU ARE", "YOUR", "YOU'RE", "NEXT", "UH HUH", "UR", "HOLD", "WHAT?", "YOU"));
        WORD_LISTS.put("YOU ARE", Arrays.asList("YOUR", "NEXT", "LIKE", "HUH", "WHAT?", "DONE", "UH UH", "HOLD", "YOU", "U", "YOU'RE", "SURE", "UR", "YOU ARE"));
        WORD_LISTS.put("YOUR", Arrays.asList("UH UH", "YOU ARE", "UH HUH", "YOUR"));
        WORD_LISTS.put("YOU'RE", Arrays.asList("YOU", "YOU'RE"));
        WORD_LISTS.put("UR", Arrays.asList("DONE", "U", "UR"));
        WORD_LISTS.put("U", Arrays.asList("UH HUH", "SURE", "NEXT", "WHAT?", "YOU'RE", "UR", "UH UH", "DONE", "U"));
        WORD_LISTS.put("UH HUH", Arrays.asList("UH HUH"));
        WORD_LISTS.put("UH UH", Arrays.asList("UR", "U", "YOU ARE", "YOU'RE", "NEXT", "UH UH"));
        WORD_LISTS.put("WHAT?", Arrays.asList("YOU", "HOLD", "YOU'RE", "YOUR", "U", "DONE", "UH UH", "LIKE", "YOU ARE", "UH HUH", "UR", "NEXT", "WHAT?"));
        WORD_LISTS.put("DONE", Arrays.asList("SURE", "UH HUH", "NEXT", "WHAT?", "YOUR", "UR", "YOU'RE", "HOLD", "LIKE", "YOU", "U", "YOU ARE", "UH UH", "DONE"));
        WORD_LISTS.put("NEXT", Arrays.asList("WHAT?", "UH HUH", "UH UH", "YOUR", "HOLD", "SURE", "NEXT"));
        WORD_LISTS.put("HOLD", Arrays.asList("YOU ARE", "U", "DONE", "UH UH", "YOU", "UR", "SURE", "WHAT?", "HOLD"));
        WORD_LISTS.put("SURE", Arrays.asList("YOU ARE", "DONE", "LIKE", "YOU'RE", "YOU", "HOLD", "UH HUH", "UR", "SURE"));
        WORD_LISTS.put("LIKE", Arrays.asList("YOU'RE", "NEXT", "U", "UR", "HOLD", "DONE", "UH UH", "WHAT?", "UH HUH", "YOU", "LIKE"));
    }

    public WhosOnFirstModule(Bomb bomb) {
        this.bomb = bomb;
        setupUI();
        startStage();
    }

    private void setupUI() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.PANEL_BG);

        displayLabel = new JLabel("", SwingConstants.CENTER);
        displayLabel.setFont(Theme.FONT_BOLD.deriveFont(24f));
        displayLabel.setForeground(Theme.TEXT_PRIMARY);
        displayLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(displayLabel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        btnPanel.setBackground(Theme.PANEL_BG);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        buttons = new JButton[6];
        for (int i = 0; i < 6; i++) {
            JButton btn = new JButton("");
            btn.setFont(Theme.FONT_BOLD.deriveFont(14f));
            btn.setBackground(Theme.PANEL_BG.brighter());
            btn.setForeground(Theme.TEXT_PRIMARY);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Add cursor pointer
            final int index = i;
            btn.addActionListener(e -> handlePress(index));
            buttons[i] = btn;
            btnPanel.add(btn);
        }
        panel.add(btnPanel, BorderLayout.CENTER);
    }

    private void startStage() {
        Random rand = new Random();
        String display = DISPLAY_WORDS[rand.nextInt(DISPLAY_WORDS.length)];
        displayLabel.setText(display);
        
        // Get 6 random words for buttons, ensuring one is correct
        List<String> allWords = new ArrayList<>(WORD_LISTS.keySet());
        List<String> buttonWords = new ArrayList<>();
        
        // Determine the word we need to look up
        int lookIndex = DISPLAY_MAP.getOrDefault(display, 0);
        // But wait, we don't know the button words yet.
        // The logic is: Look at display -> Get index -> Look at button at that index -> That is the key word.
        // So we need to generate button words first.
        
        for(int i=0; i<6; i++) {
            buttonWords.add(allWords.get(rand.nextInt(allWords.size())));
        }
        
        // Set button text
        for(int i=0; i<6; i++) {
            buttons[i].setText(buttonWords.get(i));
        }
        
        // Now determine correct answer
        String keyWord = buttonWords.get(lookIndex);
        List<String> sequence = WORD_LISTS.get(keyWord);
        
        // Find the first word in sequence that appears on the buttons
        String correctWord = null;
        if (sequence != null) {
            for (String s : sequence) {
                if (buttonWords.contains(s)) {
                    correctWord = s;
                    break;
                }
            }
        }
        
        // If no valid word found (shouldn't happen if lists are complete, but just in case), regenerate
        if (correctWord == null) {
            startStage();
            return;
        }
        
        this.targetWord = correctWord;
    }
    
    private String targetWord;

    private void handlePress(int index) {
        if (solved) return;
        
        String pressed = buttons[index].getText();
        if (pressed.equals(targetWord)) {
            stage++;
            if (stage >= 3) {
                solved = true;
                panel.setBackground(Theme.ACCENT_GREEN);
                bomb.checkDefused();
            } else {
                startStage();
            }
        } else {
            bomb.addStrike();
            startStage(); // Reset stage on strike? Or just continue? Game usually resets stage.
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
        return game.Localization.get("MOD_WHOSONFIRST");
    }
}
