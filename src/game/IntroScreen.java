package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

/**
 * Ecranul de introducere (Intro) care simuleaza pornirea unui sistem hackuit/securizat
 * folosind efecte grafice precum "Matrix rain" si un boot log fictiv.
 */
public class IntroScreen extends JPanel {
    private App app;
    private Timer timer;
    private List<String> logLines = new ArrayList<>(); // Liniile procesului de boot fictiv
    private Random random = new Random();
    private int progress = 0; // Progresul barei de incarcare (0-100)
    private final int MAX_PROGRESS = 100;
    private boolean bootComplete = false;
    
    // Matrice pentru efectul de ploaie Matrix
    private int[] drops;
    private final int FONT_SIZE = 14;

    public IntroScreen(App app) {
        this.app = app;
        setBackground(Color.BLACK);
        
        // Initializarea picaturilor in functie de latimea ecranului
        drops = new int[1024 / FONT_SIZE];
        for (int i = 0; i < drops.length; i++) {
            drops[i] = random.nextInt(100) * -1;
        }

        // Timer principal pentru animatii (aprox. 30 FPS)
        timer = new Timer(30, new ActionListener() {
            int tick = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                tick++;
                
                // Actualizarea plumajului Matrix
                for (int i = 0; i < drops.length; i++) {
                    if (drops[i] * FONT_SIZE > getHeight() && Math.random() > 0.975) {
                        drops[i] = 0;
                    }
                    drops[i]++;
                }

                // Generarea mesajelor din Log si actualizarea barei de progres
                if (tick % 5 == 0 && !bootComplete) {
                    if (progress < MAX_PROGRESS) {
                        progress += random.nextInt(3);
                        if (progress > MAX_PROGRESS) progress = MAX_PROGRESS;
                        
                        // Ocazional adaugam o linie noua in log-ul de pornire
                        if (random.nextInt(10) > 6) {
                            logLines.add(generateLogMessage());
                            if (logLines.size() > 15) logLines.remove(0);
                        }
                    } else {
                        bootComplete = true;
                        logLines.add("SYSTEM READY.");
                        logLines.add("INITIALIZING INTERFACE...");
                        
                        // Cand sistemul este "gata", asteptam 2 secunde inainte sa mergem la Meniu
                        Timer switchTimer = new Timer(2000, evt -> {
                            ((Timer)e.getSource()).stop();
                            app.showMenu();
                        });
                        switchTimer.setRepeats(false);
                        switchTimer.start();
                    }
                }
                
                repaint();
            }
        });
        timer.start();
    }
    
    /** Genereaza un mesaj aleatoriu care pare tehnic/de sistem. */
    private String generateLogMessage() {
        String[] prefixes = {"LOADING", "VERIFYING", "ALLOCATING", "DECRYPTING", "MOUNTING"};
        String[] components = {"KERNEL", "MODULES", "ASSETS", "SECURITY", "PROTOCOL", "INTERFACE", "AUDIO", "PHYSICS"};
        String[] suffixes = {"OK", "DONE", "COMPLETE", "SUCCESS", "READY"};
        
        return String.format("> %s %s... %s", 
            prefixes[random.nextInt(prefixes.length)],
            components[random.nextInt(components.length)],
            suffixes[random.nextInt(suffixes.length)]
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Desenarea ploii Matrix verde
        g2.setFont(new Font("Monospaced", Font.BOLD, FONT_SIZE));
        for (int i = 0; i < drops.length; i++) {
            int x = i * FONT_SIZE;
            int y = drops[i] * FONT_SIZE;
            
            if (y > 0) {
                for (int j = 0; j < 10; j++) {
                    int alpha = 255 - (j * 25);
                    if (alpha < 0) alpha = 0;
                    g2.setColor(new Color(0, 255, 0, alpha));
                    char c = (char)(random.nextInt(94) + 33);
                    g2.drawString(String.valueOf(c), x, y - (j * FONT_SIZE));
                }
            }
        }

        // Adaugam un fundal intunecat (vignetta) peste text
        RadialGradientPaint rgp = new RadialGradientPaint(
            getWidth() / 2, getHeight() / 2, getWidth(),
            new float[]{0.0f, 1.0f},
            new Color[]{new Color(0,0,0,100), new Color(0,0,0,255)}
        );
        g2.setPaint(rgp);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Desenarea titlului jocului in centru
        String title = "KEEP TALKING AND NOBODY EXPLODES";
        String subtitle = "v3.0 // SYSTEM STARTUP";
        
        g2.setFont(Theme.FONT_TITLE.deriveFont(48f));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (getWidth() - fm.stringWidth(title)) / 2;
        int ty = getHeight() / 3;
        
        // Efect rar de glitch pentru titlu
        if (random.nextInt(20) == 0) {
            g2.setColor(new Color(255, 0, 0, 100));
            g2.drawString(title, tx + random.nextInt(5), ty);
            g2.setColor(new Color(0, 255, 255, 100));
            g2.drawString(title, tx - random.nextInt(5), ty);
        }
        g2.setColor(Color.WHITE);
        g2.drawString(title, tx, ty);
        
        g2.setFont(Theme.FONT_MONO.deriveFont(24f));
        fm = g2.getFontMetrics();
        g2.setColor(Theme.ACCENT_GREEN);
        g2.drawString(subtitle, (getWidth() - fm.stringWidth(subtitle)) / 2, ty + 40);

        // Desenarea barei de incarcare
        int barW = 600;
        int barH = 20;
        int barX = (getWidth() - barW) / 2;
        int barY = getHeight() / 2;
        
        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(barX, barY, barW, barH);
        g2.setColor(Theme.ACCENT_GREEN);
        g2.fillRect(barX, barY, (int)(barW * (progress / 100.0)), barH);
        g2.setColor(Color.WHITE);
        g2.drawRect(barX, barY, barW, barH);

        // Desenarea Log-ului sub bara de incarcare
        g2.setFont(Theme.FONT_MONO.deriveFont(14f));
        int logY = barY + 50;
        for (String line : logLines) {
            g2.setColor(new Color(0, 255, 0, 200));
            g2.drawString(line, barX, logY);
            logY += 20;
        }
    }
}
