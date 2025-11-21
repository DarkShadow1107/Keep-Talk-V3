package modules;

import game.Bomb;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WiresModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<String> wireColors;
    private int correctWireIndex;

    public WiresModule(Bomb bomb) {
        this.bomb = bomb;
        this.wireColors = new ArrayList<>();
        generateWires();
        setupUI();
        determineCorrectWire();
    }

    private void generateWires() {
        String[] colors = {"Red", "Blue", "Yellow", "Black", "White"};
        Random rand = new Random();
        int numWires = rand.nextInt(4) + 3; // 3 to 6 wires

        for (int i = 0; i < numWires; i++) {
            wireColors.add(colors[rand.nextInt(colors.length)]);
        }
    }

    private void setupUI() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        for (int i = 0; i < wireColors.size(); i++) {
            String color = wireColors.get(i);
            JButton wireButton = new JButton("Cut " + color + " Wire");
            wireButton.setBackground(getColor(color));
            wireButton.setForeground(isDark(color) ? Color.WHITE : Color.BLACK);
            
            final int index = i;
            wireButton.addActionListener(e -> cutWire(index, wireButton));
            panel.add(wireButton);
        }
    }

    private Color getColor(String colorName) {
        switch (colorName) {
            case "Red": return Color.RED;
            case "Blue": return Color.BLUE;
            case "Yellow": return Color.YELLOW;
            case "Black": return Color.BLACK;
            case "White": return Color.WHITE;
            default: return Color.GRAY;
        }
    }

    private boolean isDark(String colorName) {
        return colorName.equals("Black") || colorName.equals("Blue") || colorName.equals("Red");
    }

    private void determineCorrectWire() {
        int redCount = Collections.frequency(wireColors, "Red");
        int blueCount = Collections.frequency(wireColors, "Blue");
        int yellowCount = Collections.frequency(wireColors, "Yellow");
        int blackCount = Collections.frequency(wireColors, "Black");
        int whiteCount = Collections.frequency(wireColors, "White");
        int count = wireColors.size();
        String lastWire = wireColors.get(count - 1);

        // Logic for 3 wires
        if (count == 3) {
            if (redCount == 0) correctWireIndex = 1; // Second wire
            else if (lastWire.equals("White")) correctWireIndex = 2; // Last wire
            else if (blueCount > 1) correctWireIndex = wireColors.lastIndexOf("Blue");
            else correctWireIndex = 2; // Last wire
        }
        // Logic for 4 wires
        else if (count == 4) {
            if (redCount > 1 && isOddSerialNumber()) correctWireIndex = wireColors.lastIndexOf("Red");
            else if (lastWire.equals("Yellow") && redCount == 0) correctWireIndex = 0; // First wire
            else if (blueCount == 1) correctWireIndex = 0; // First wire
            else if (yellowCount > 1) correctWireIndex = 3; // Last wire
            else correctWireIndex = 1; // Second wire
        }
        // Logic for 5 wires
        else if (count == 5) {
            if (lastWire.equals("Black") && isOddSerialNumber()) correctWireIndex = 3; // Fourth wire
            else if (redCount == 1 && yellowCount > 1) correctWireIndex = 0; // First wire
            else if (blackCount == 0) correctWireIndex = 1; // Second wire
            else correctWireIndex = 0; // First wire
        }
        // Logic for 6 wires
        else {
            if (yellowCount == 0 && isOddSerialNumber()) correctWireIndex = 2; // Third wire
            else if (yellowCount == 1 && whiteCount > 1) correctWireIndex = 3; // Fourth wire
            else if (redCount == 0) correctWireIndex = 5; // Last wire
            else correctWireIndex = 3; // Fourth wire
        }
    }

    private boolean isOddSerialNumber() {
        String sn = bomb.getSerialNumber();
        char lastChar = sn.charAt(sn.length() - 1);
        return Character.isDigit(lastChar) && (lastChar - '0') % 2 != 0;
    }

    private void cutWire(int index, JButton button) {
        if (solved) return;

        button.setEnabled(false);
        if (index == correctWireIndex) {
            solved = true;
            panel.setBackground(Color.GREEN);
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
        return "Wires";
    }
}