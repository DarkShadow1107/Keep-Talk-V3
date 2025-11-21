package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WiresModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<Wire> wires;
    private int correctWireIndex;

    private class Wire {
        String color;
        boolean cut;
        Shape shape;
        Rectangle clickArea;

        Wire(String color) {
            this.color = color;
            this.cut = false;
        }
    }

    public WiresModule(Bomb bomb) {
        this.bomb = bomb;
        this.wires = new ArrayList<>();
        generateWires();
        setupUI();
        determineCorrectWire();
    }

    private void generateWires() {
        String[] colors = {"Red", "Blue", "Yellow", "Black", "White"};
        Random rand = new Random();
        int numWires = rand.nextInt(4) + 3; // 3 to 6 wires

        for (int i = 0; i < numWires; i++) {
            wires.add(new Wire(colors[rand.nextInt(colors.length)]));
        }
    }

    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw LED
                Theme.drawLed(g2, getWidth() - 30, 20, solved, true);

                // Draw Wires
                int startY = 40;
                int gap = 30;
                int width = getWidth();

                for (int i = 0; i < wires.size(); i++) {
                    Wire wire = wires.get(i);
                    int y = startY + (i * gap);
                    
                    // Define wire path
                    CubicCurve2D curve = new CubicCurve2D.Float(
                        20, y, 
                        width / 3, y + 20, 
                        2 * width / 3, y - 20, 
                        width - 20, y
                    );
                    
                    wire.shape = curve;
                    wire.clickArea = new Rectangle(20, y - 10, width - 40, 20);

                    // Draw Shadow
                    g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.translate(2, 2);
                    if (!wire.cut) g2.draw(curve);
                    else {
                        // Draw cut wire (two parts)
                        CubicCurve2D left = new CubicCurve2D.Float(20, y, width/3, y+20, width/2-10, y, width/2-5, y+10);
                        CubicCurve2D right = new CubicCurve2D.Float(width/2+5, y+10, 2*width/3, y-20, width-20, y, width-20, y);
                        g2.draw(left);
                        g2.draw(right);
                    }
                    g2.translate(-2, -2);

                    // Draw Wire
                    g2.setColor(getColor(wire.color));
                    g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    
                    if (!wire.cut) {
                        g2.draw(curve);
                        // Highlight
                        g2.setStroke(new BasicStroke(2));
                        g2.setColor(new Color(255, 255, 255, 100));
                        g2.draw(curve);
                    } else {
                        // Draw cut wire
                        CubicCurve2D left = new CubicCurve2D.Float(20, y, width/3, y+20, width/2-10, y, width/2-5, y+10);
                        CubicCurve2D right = new CubicCurve2D.Float(width/2+5, y+10, 2*width/3, y-20, width-20, y, width-20, y);
                        
                        g2.setColor(getColor(wire.color));
                        g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.draw(left);
                        g2.draw(right);
                        
                        // Copper ends
                        g2.setColor(new Color(184, 115, 51));
                        g2.fillOval(width/2-8, y+7, 6, 6);
                        g2.fillOval(width/2+2, y+7, 6, 6);
                    }
                }
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(200, 200));
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (solved) return;
                
                for (int i = 0; i < wires.size(); i++) {
                    Wire wire = wires.get(i);
                    if (!wire.cut && wire.clickArea.contains(e.getPoint())) {
                        cutWire(i);
                        panel.repaint();
                        break;
                    }
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (solved) {
                    panel.setCursor(Cursor.getDefaultCursor());
                    return;
                }
                
                boolean hover = false;
                for (Wire wire : wires) {
                    if (!wire.cut && wire.clickArea.contains(e.getPoint())) {
                        hover = true;
                        break;
                    }
                }
                panel.setCursor(hover ? new Cursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            }
        });
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

    private void determineCorrectWire() {
        List<String> wireColors = new ArrayList<>();
        for(Wire w : wires) wireColors.add(w.color);
        
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

    private void cutWire(int index) {
        wires.get(index).cut = true;
        
        if (index == correctWireIndex) {
            solved = true;
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