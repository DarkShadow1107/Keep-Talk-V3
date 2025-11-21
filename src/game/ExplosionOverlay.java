package game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ExplosionOverlay extends JComponent {
    private List<Particle> particles = new ArrayList<>();
    private Timer animationTimer;
    private Random random = new Random();

    public ExplosionOverlay() {
        setOpaque(false);
    }

    public void explode(int centerX, int centerY) {
        particles.clear();
        // Create explosion particles
        for (int i = 0; i < 200; i++) {
            particles.add(new Particle(centerX, centerY));
        }
        
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animationTimer = new Timer(16, e -> {
            updateParticles();
            repaint();
            if (particles.isEmpty()) {
                ((Timer)e.getSource()).stop();
            }
        });
        animationTimer.start();
    }

    private void updateParticles() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update();
            if (!p.isAlive()) {
                it.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Particle p : particles) {
            p.draw(g2);
        }
    }

    private class Particle {
        double x, y;
        double vx, vy;
        float size;
        float life = 1.0f;
        float decay;
        Color color;

        public Particle(int startX, int startY) {
            x = startX;
            y = startY;
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = random.nextDouble() * 15 + 2;
            vx = Math.cos(angle) * speed;
            vy = Math.sin(angle) * speed;
            size = random.nextFloat() * 30 + 10;
            decay = random.nextFloat() * 0.03f + 0.01f;
            
            // Fire colors
            if (random.nextBoolean()) {
                color = new Color(255, random.nextInt(100), 0); // Orange/Red
            } else {
                color = new Color(255, 200 + random.nextInt(55), 0); // Yellow
            }
            if (random.nextInt(5) == 0) color = Color.GRAY; // Smoke
        }

        public void update() {
            x += vx;
            y += vy;
            vx *= 0.95; // Drag
            vy *= 0.95;
            life -= decay;
            size *= 0.95;
        }

        public boolean isAlive() {
            return life > 0;
        }

        public void draw(Graphics2D g2) {
            if (life <= 0) return;
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(life * 255)));
            g2.fillOval((int)(x - size/2), (int)(y - size/2), (int)size, (int)size);
        }
    }
}
