package game;

import javax.swing.SwingUtilities;

/**
 * Punctul de intrare in aplicatie.
 * Porneste interfata grafica folosind SwingUtilities.invokeLater pentru a asigura thread-safety.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App();
        });
    }
}