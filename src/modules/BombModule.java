package modules;

import javax.swing.JPanel;

/**
 * Interfata de baza pentru toate modulele bombei.
 * Orice modul nou (Fire, Butoane, Tastatura, etc.) trebuie sa implementeze aceasta interfata
 * pentru a fi compatibil cu sistemul de gestionare a bombei.
 */
public interface BombModule {
    /** Returneaza panoul vizual (Interfata Grafica) a modului. */
    JPanel getPanel();
    
    /** Verifica daca modulul a fost rezolvat cu succes. */
    boolean isSolved();
    
    /** Metoda apelata (callback) atunci cand jucatorul face o greseala la acest modul. */
    void onStrike();
    
    /** Metoda apelata (callback) atunci cand modulul este rezolvat corect. */
    void onSolve();
    
    /** Returneaza numele localizat al modului pentru afisare. */
    String getName();
}
