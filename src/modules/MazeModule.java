package modules;

import game.Bomb;
import game.Theme;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;

public class MazeModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private int playerX, playerY;
    private int targetX, targetY;
    private MazeData.Layout layout;
    private static final int SIZE = 6;
    private float scanLinePos = 0;
    private Timer scanTimer;

    public MazeModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
    }

    private void generatePuzzle() {
        Random rand = new Random();
        layout = MazeData.ALL_MAZES.get(rand.nextInt(MazeData.ALL_MAZES.size()));
        
        playerX = layout.markers[0].x;
        playerY = layout.markers[0].y;
        targetX = layout.markers[1].x;
        targetY = layout.markers[1].y;
    }

    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw Radar Screen Background
                int size = 100; // Fixed size to avoid overlap
                int startX = (getWidth() - size) / 2;
                int startY = 5; // Move to top

                g2.setColor(new Color(0, 20, 0));
                g2.fillRect(startX, startY, size, size);
                g2.setColor(Theme.ACCENT_GREEN);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(startX, startY, size, size);

                // Draw Grid
                int cellSize = size / SIZE;
                g2.setColor(new Color(0, 50, 0));
                g2.setStroke(new BasicStroke(1));
                for (int i = 0; i <= SIZE; i++) {
                    g2.drawLine(startX + i * cellSize, startY, startX + i * cellSize, startY + size);
                    g2.drawLine(startX, startY + i * cellSize, startX + size, startY + i * cellSize);
                }

                // Draw Fixed Maze Markers (from MazeData) as faint green dots
                g2.setColor(new Color(0, 150, 0, 150));
                for (java.awt.Point p : layout.markers) {
                    int mx = startX + p.x * cellSize + cellSize / 2;
                    int my = startY + p.y * cellSize + cellSize / 2;
                    g2.fillOval(mx - 4, my - 4, 8, 8);
                }

                // Draw Target (Red Square as per manual icon)
                int tx = startX + targetX * cellSize + cellSize / 4;
                int ty = startY + targetY * cellSize + cellSize / 4;
                g2.setColor(Color.RED);
                g2.fillRect(tx, ty, cellSize / 2, cellSize / 2);

                // Draw Player (White Circle/Light)
                int px = startX + playerX * cellSize + cellSize / 2;
                int py = startY + playerY * cellSize + cellSize / 2;
                g2.setColor(Color.WHITE);
                g2.fillOval(px - 5, py - 5, 10, 10);

                // Draw Scanline
                int scanX = startX + (int)(scanLinePos * size);
                g2.setColor(new Color(0, 255, 0, 100));
                g2.drawLine(scanX, startY, scanX, startY + size);
                
                // Scanline trail
                GradientPaint gp = new GradientPaint(scanX - 30, startY, new Color(0, 255, 0, 0), scanX, startY, new Color(0, 255, 0, 50));
                g2.setPaint(gp);
                g2.fillRect(scanX - 30, startY, 30, size);
            }
        };
        panel.setBackground(Theme.PANEL_BG);
        panel.setPreferredSize(new Dimension(180, 180));
        panel.setFocusable(true);
        Theme.applyCursor(panel);

        // Scanline animation
        scanTimer = new Timer(20, e -> {
            scanLinePos += 0.01f;
            if (scanLinePos > 1.0f) scanLinePos = 0;
            panel.repaint();
        });
        scanTimer.start();

        // Interaction
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                panel.requestFocusInWindow();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int k = e.getKeyCode();
                if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W) move(0, -1);
                else if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) move(0, 1);
                else if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) move(-1, 0);
                else if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) move(1, 0);
            }
        });
        
        // Add visual controls for mouse users
        JPanel controls = new JPanel(new GridLayout(2, 3, 2, 2));
        controls.setOpaque(false);
        controls.setBorder(BorderFactory.createEmptyBorder(0, 45, 5, 45));
        
        controls.add(new JLabel());
        controls.add(createBtn("▲", 0, -1));
        controls.add(new JLabel());
        controls.add(createBtn("◄", -1, 0));
        controls.add(createBtn("▼", 0, 1));
        controls.add(createBtn("►", 1, 0));
        
        panel.setLayout(new BorderLayout());
        panel.add(controls, BorderLayout.SOUTH);
    }

    private JButton createBtn(String text, int dx, int dy) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(45, 45, 50));
        btn.setForeground(Color.WHITE);
        btn.setFocusable(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 75)));
        btn.addActionListener(e -> {
            move(dx, dy);
            panel.requestFocusInWindow();
        });
        return btn;
    }

    private void move(int dx, int dy) {
        if (solved) return;

        int newX = playerX + dx;
        int newY = playerY + dy;

        // Check bounds
        if (newX < 0 || newX >= SIZE || newY < 0 || newY >= SIZE) {
            bomb.addStrike();
            return;
        }

        // Check walls (logic from MazeData)
        if (layout.hasWall(playerX, playerY, dx, dy)) {
            bomb.addStrike();
        } else {
            playerX = newX;
            playerY = newY;
            panel.repaint();
            
            if (playerX == targetX && playerY == targetY) {
                solved = true;
                scanTimer.stop();
                bomb.checkDefused();
            }
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
        return game.Localization.get("MOD_MAZE");
    }
}
