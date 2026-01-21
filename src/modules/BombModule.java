package modules;

import javax.swing.JPanel;

public interface BombModule {
    JPanel getPanel();
    boolean isSolved();
    void onStrike(); // Callback when a mistake is made
    void onSolve();  // Callback when module is solved
    String getName();
}
