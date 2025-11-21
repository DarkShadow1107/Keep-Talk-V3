package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.Random;

public class MazeModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private int playerX, playerY;
    private int targetX, targetY;
    private int[][] maze; // 0 = path, 1 = wall
    private static final int SIZE = 6;
    private float scanLinePos = 0;
    private Timer scanTimer;

    // Simplified maze generation
    private static final int[][][] MAZES = {
        {
            {0, 1, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 0},
            {0, 0, 0, 1, 0, 0},
            {1, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 0}
        },
        {
            {0, 0, 0, 1, 0, 0},
            {0, 1, 0, 1, 0, 1},
            {0, 1, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0},
            {1, 1, 0, 1, 1, 0}
        }
    };

    public MazeModule(Bomb bomb) {
        this.bomb = bomb;
        generatePuzzle();
        setupUI();
    }

    private void generatePuzzle() {
        Random rand = new Random();
        maze = MAZES[rand.nextInt(MAZES.length)];
        
        do {
            playerX = rand.nextInt(SIZE);
            playerY = rand.nextInt(SIZE);
        } while (maze[playerY][playerX] == 1);

        do {
            targetX = rand.nextInt(SIZE);
            targetY = rand.nextInt(SIZE);
        } while (maze[targetY][targetX] == 1 || (targetX == playerX && targetY == playerY));
    }

    private void setupUI() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw Status LED
                Theme.drawLed(g2, getWidth() - 25, 15, solved, true);

                // Draw Radar Screen Background
                int padding = 20;
                int size = Math.min(getWidth(), getHeight()) - padding * 2;
                int startX = (getWidth() - size) / 2;
                int startY = (getHeight() - size) / 2 + 10;

                g2.setColor(new Color(0, 20, 0));
                g2.fillRect(startX, startY, size, size);
                g2.setColor(Theme.ACCENT_GREEN);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(startX, startY, size, size);

                // Draw Grid
                int cellSize = size / SIZE;
                g2.setColor(new Color(0, 50, 0));
                g2.setStroke(new BasicStroke(1));
                for (int i = 1; i < SIZE; i++) {
                    g2.drawLine(startX + i * cellSize, startY, startX + i * cellSize, startY + size);
                    g2.drawLine(startX, startY + i * cellSize, startX + size, startY + i * cellSize);
                }

                // Draw Walls (Markers)
                // In the real game, walls are invisible but circle markers indicate the maze.
                // Here we'll draw "digital" walls for the radar look.
                g2.setColor(new Color(0, 100, 0));
                for (int y = 0; y < SIZE; y++) {
                    for (int x = 0; x < SIZE; x++) {
                        if (maze[y][x] == 1) {
                            g2.fillRect(startX + x * cellSize + 5, startY + y * cellSize + 5, cellSize - 10, cellSize - 10);
                        }
                    }
                }

                // Draw Target (Ring)
                int tx = startX + targetX * cellSize + cellSize / 2;
                int ty = startY + targetY * cellSize + cellSize / 2;
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(tx - 10, ty - 10, 20, 20);

                // Draw Player (Triangle)
                int px = startX + playerX * cellSize + cellSize / 2;
                int py = startY + playerY * cellSize + cellSize / 2;
                g2.setColor(Color.WHITE);
                Path2D player = new Path2D.Float();
                player.moveTo(px, py - 8);
                player.lineTo(px + 6, py + 8);
                player.lineTo(px - 6, py + 8);
                player.closePath();
                g2.fill(player);

                // Draw Scanline
                int scanX = startX + (int)(scanLinePos * size);
                g2.setColor(new Color(0, 255, 0, 100));
                g2.drawLine(scanX, startY, scanX, startY + size);
                
                // Scanline trail
                GradientPaint gp = new GradientPaint(scanX - 50, startY, new Color(0, 255, 0, 0), scanX, startY, new Color(0, 255, 0, 50));
                g2.setPaint(gp);
                g2.fillRect(scanX - 50, startY, 50, size);
            }
        };
        panel.setBackground(Theme.PANEL_BG);
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
        JPanel controls = new JPanel(new GridLayout(2, 3, 5, 5));
        controls.setOpaque(false);
        controls.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 40));
        
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
        btn.setBackground(new Color(30, 30, 30));
        btn.setForeground(Color.WHITE);
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
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

        if (newX < 0 || newX >= SIZE || newY < 0 || newY >= SIZE || maze[newY][newX] == 1) {
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
        return "Maze";
    }
}