package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WiresModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private List<Wire> wires = new ArrayList<>();
    private int wireToCutIndex;

    public WiresModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
    }

    private void generatePuzzle() {
        Random rand = new Random();
        int numWires = rand.nextInt(4) + 3; // 3 to 6 wires
        
        for (int i = 0; i < numWires; i++) {
            Color color;
            int c = rand.nextInt(7);
            switch (c) {
                case 0: color = Color.RED; break;
                case 1: color = Color.BLUE; break;
                case 2: color = Color.YELLOW; break;
                case 3: color = Color.BLACK; break;
                case 4: color = Color.WHITE; break;
                case 5: color = Color.ORANGE; break;
                case 6: color = Color.GREEN; break;
                default: color = Color.WHITE; break;
            }
            wires.add(new Wire(color));
        }

        // Logic (Simplified for demo)
        wireToCutIndex = wires.size() - 1;
    }

    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw Status LED
                Theme.drawLed(g2, getWidth() - 20, 20, solved, true);

                int startY = 40;
                int gap = 30;
                int width = getWidth();
                
                for (int i = 0; i < wires.size(); i++) {
                    Wire wire = wires.get(i);
                    int y = startY + i * gap;
                    
                    // Create Curve Shape
                    wire.shape = new CubicCurve2D.Float(20, y, 20 + 50, y + 30, width - 70, y + 30, width - 20, y);
                    
                    // Draw wire
                    if (wire.cut) {
                        drawCutWire(g2, wire.shape, wire.color);
                    } else {
                        drawWire(g2, wire.shape, wire.color);
                    }
                    
                    // Draw Number (for keyboard)
                    g2.setColor(Color.GRAY);
                    g2.setFont(Theme.FONT_MONO.deriveFont(10f));
                    g2.drawString(String.valueOf(i + 1), 5, y + 5);
                }
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(200, 200));
        panel.setFocusable(true); // Enable focus
        Theme.applyCursor(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocusInWindow();
                if (solved) return;
                for (int i = 0; i < wires.size(); i++) {
                    Wire wire = wires.get(i);
                    if (!wire.cut && isPointOnWire(e.getPoint(), wire.shape)) {
                        cutWire(i);
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
                    if (!wire.cut && isPointOnWire(e.getPoint(), wire.shape)) {
                        hover = true;
                        break;
                    }
                }
                panel.setCursor(hover ? new Cursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            }
        });
        
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (solved) return;
                int k = e.getKeyCode();
                int index = -1;
                if (k >= KeyEvent.VK_1 && k <= KeyEvent.VK_6) index = k - KeyEvent.VK_1;
                
                if (index >= 0 && index < wires.size()) {
                    cutWire(index);
                }
            }
        });
    }
    
    private boolean isPointOnWire(Point p, Shape shape) {
        BasicStroke stroke = new BasicStroke(15); // Hitbox width
        return stroke.createStrokedShape(shape).contains(p);
    }

    private void cutWire(int index) {
        Wire wire = wires.get(index);
        if (wire.cut) return;
        
        wire.cut = true;
        panel.repaint();

        if (index == wireToCutIndex) {
            solved = true;
            bomb.checkDefused();
        } else {
            bomb.addStrike();
        }
    }

    private void drawWire(Graphics2D g2, Shape shape, Color c) {
        g2.setColor(c);
        g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(shape);
        
        // Highlight
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(2));
        g2.draw(shape);
    }

    private void drawCutWire(Graphics2D g2, CubicCurve2D curve, Color c) {
        g2.setColor(c);
        g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Split curve roughly in half visually
        double x1 = curve.getX1();
        double y1 = curve.getY1();
        double cx1 = curve.getCtrlX1();
        double cy1 = curve.getCtrlY1();
        
        double x2 = curve.getX2();
        double y2 = curve.getY2();
        double cx2 = curve.getCtrlX2();
        double cy2 = curve.getCtrlY2();
        
        // Left part
        CubicCurve2D left = new CubicCurve2D.Double(x1, y1, cx1, cy1, cx1 + 20, cy1, cx1 + 40, cy1 + 10);
        g2.draw(left);
        
        // Right part
        CubicCurve2D right = new CubicCurve2D.Double(cx2 - 40, cy2 + 10, cx2 - 20, cy2, cx2, cy2, x2, y2);
        g2.draw(right);
        
        // Copper ends
        g2.setColor(new Color(184, 115, 51));
        g2.fillOval((int)left.getX2()-3, (int)left.getY2()-3, 6, 6);
        g2.fillOval((int)right.getX1()-3, (int)right.getY1()-3, 6, 6);
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

    private class Wire {
        Color color;
        boolean cut = false;
        CubicCurve2D shape;

        Wire(Color c) {
            this.color = c;
        }
    }
}