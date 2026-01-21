package modules;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class MazeData {
    public static class Layout {
        public final Point[] markers;
        public final boolean[][] hWalls; // walls to the right of cell [x][y]
        public final boolean[][] vWalls; // walls below cell [x][y]

        public Layout(Point m1, Point m2, String hData, String vData) {
            this.markers = new Point[]{m1, m2};
            this.hWalls = new boolean[5][6]; // 5 gaps between 6 columns, 6 rows
            this.vWalls = new boolean[6][5]; // 6 columns, 5 gaps between 6 rows
            
            // hData: 30 chars (5x6), '1' for wall, '0' for path
            for(int y=0; y<6; y++) {
                for(int x=0; x<5; x++) {
                    hWalls[x][y] = hData.charAt(y*5 + x) == '1';
                }
            }
            // vData: 30 chars (6x5), '1' for wall, '0' for path
            for(int y=0; y<5; y++) {
                for(int x=0; x<6; x++) {
                    vWalls[x][y] = vData.charAt(y*6 + x) == '1';
                }
            }
        }
        
        public boolean hasWall(int x, int y, int dx, int dy) {
            if (dx == 1) return x < 5 && hWalls[x][y];
            if (dx == -1) return x > 0 && hWalls[x-1][y];
            if (dy == 1) return y < 5 && vWalls[x][y];
            if (dy == -1) return y > 0 && vWalls[x][y-1];
            return false;
        }
    }

    public static final List<Layout> ALL_MAZES = new ArrayList<>();

    static {
        // Maze 1 (Official #4): Markers (1,1) and (1,4)
        ALL_MAZES.add(new Layout(new Point(0, 0), new Point(0, 3),
            "10101" + "00110" + "10101" + "01010" + "00100" + "10101",
            "010001" + "100100" + "001010" + "100001" + "010100"));

        // Maze 2 (Official #2): Markers (2,4) and (5,2)
        ALL_MAZES.add(new Layout(new Point(1, 3), new Point(4, 1),
            "01010" + "00011" + "10100" + "00110" + "11001" + "00100",
            "001010" + "010001" + "000100" + "101010" + "010001"));
            
        // Maze 3 (Official #3): Markers (4,4) and (6,4)
        ALL_MAZES.add(new Layout(new Point(3, 3), new Point(5, 3),
            "10101" + "01000" + "00111" + "11000" + "00101" + "10100",
            "010001" + "001010" + "100100" + "010001" + "001010"));

        // Maze 4 (Official #7): Markers (2,1) and (2,6)
        ALL_MAZES.add(new Layout(new Point(1, 0), new Point(1, 5),
            "01010" + "00101" + "10010" + "01010" + "00101" + "10100",
            "100100" + "010001" + "101010" + "010001" + "001001"));

        // Maze 5 (Official #5): Markers (5,3) and (4,6)
        ALL_MAZES.add(new Layout(new Point(4, 2), new Point(3, 5),
            "10011" + "00010" + "01010" + "10010" + "01001" + "00100",
            "010001" + "101010" + "010001" + "001010" + "100100"));

        // Maze 6 (Official #6): Markers (3,5) and (5,1)
        ALL_MAZES.add(new Layout(new Point(2, 4), new Point(4, 0),
            "01010" + "10010" + "01001" + "00101" + "10010" + "01010",
            "001010" + "100100" + "010001" + "001101" + "100100"));

        // Maze 7 (Official #8): Markers (4,1) and (3,4)
        ALL_MAZES.add(new Layout(new Point(3, 0), new Point(2, 3),
            "10100" + "01001" + "10010" + "01001" + "00101" + "10100",
            "010001" + "100100" + "010010" + "001001" + "100100"));

        // Maze 8 (Official #1): Markers (1,6) and (6,1)
        ALL_MAZES.add(new Layout(new Point(0, 5), new Point(5, 0),
            "00100" + "10101" + "00010" + "10100" + "01001" + "00100",
            "010010" + "101001" + "000100" + "100001" + "010100"));

        // Maze 9 (Official #9): Markers (1,5) and (3,2)
        ALL_MAZES.add(new Layout(new Point(0, 4), new Point(2, 1),
            "00011" + "01010" + "10010" + "01001" + "00010" + "00100",
            "001010" + "110100" + "010001" + "000010" + "000100"));
    }
}
