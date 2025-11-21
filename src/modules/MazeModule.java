package modules;

import game.Bomb;
import game.Theme;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeModule implements BombModule {
    private JPanel panel;
    private boolean solved = false;
    private Bomb bomb;
    private int playerX, playerY;
    private int targetX, targetY;
    private int[][] maze; // 0 = path, 1 = wall
    private static final int SIZE = 6;
    private JPanel[][] gridPanels;

    // Simplified maze generation (hardcoded for demo, ideally procedural)
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
        
        // Find valid start/end
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
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.PANEL_BG);
        panel.setFocusable(true); // Allow focus for key events

        // Request focus when clicked
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                panel.requestFocusInWindow();
            }
        });

        // Key listener for WASD/Arrows
        panel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                int k = e.getKeyCode();
                if (k == java.awt.event.KeyEvent.VK_UP || k == java.awt.event.KeyEvent.VK_W) move(0, -1);
                else if (k == java.awt.event.KeyEvent.VK_DOWN || k == java.awt.event.KeyEvent.VK_S) move(0, 1);
                else if (k == java.awt.event.KeyEvent.VK_LEFT || k == java.awt.event.KeyEvent.VK_A) move(-1, 0);
                else if (k == java.awt.event.KeyEvent.VK_RIGHT || k == java.awt.event.KeyEvent.VK_D) move(1, 0);
            }
        });

        JPanel grid = new JPanel(new GridLayout(SIZE, SIZE, 2, 2));
        grid.setBackground(Theme.PANEL_BG);
        gridPanels = new JPanel[SIZE][SIZE];

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                JPanel cell = new JPanel();
                cell.setBackground(Color.DARK_GRAY);
                gridPanels[y][x] = cell;
                grid.add(cell);
            }
        }
        updateGrid();

        JPanel controls = new JPanel(new GridLayout(2, 3));
        controls.setBackground(Theme.PANEL_BG);
        
        controls.add(new JLabel());
        controls.add(createMoveButton("▲", 0, -1));
        controls.add(new JLabel());
        controls.add(createMoveButton("◄", -1, 0));
        controls.add(createMoveButton("▼", 0, 1));
        controls.add(createMoveButton("►", 1, 0));

        panel.add(grid, BorderLayout.CENTER);
        panel.add(controls, BorderLayout.SOUTH);
    }

    private JButton createMoveButton(String text, int dx, int dy) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setMargin(new Insets(0,0,0,0));
        btn.addActionListener(e -> move(dx, dy));
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
            updateGrid();
            
            if (playerX == targetX && playerY == targetY) {
                solved = true;
                panel.setBackground(Theme.ACCENT_GREEN);
                bomb.checkDefused();
            }
        }
    }

    private void updateGrid() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (x == playerX && y == playerY) {
                    gridPanels[y][x].setBackground(Color.WHITE); // Player
                } else if (x == targetX && y == targetY) {
                    gridPanels[y][x].setBackground(Theme.ACCENT_RED); // Target
                } else if (maze[y][x] == 1) {
                    gridPanels[y][x].setBackground(Color.BLACK); // Wall (invisible in real game, but visible here for logic)
                    // Actually, in real game walls are invisible, but markers are visible.
                    // Let's make walls visible for this simplified version to be playable without manual reference for maze layout.
                    gridPanels[y][x].setBorder(BorderFactory.createLineBorder(Theme.ACCENT_GREEN));
                } else {
                    gridPanels[y][x].setBackground(Color.DARK_GRAY);
                }
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