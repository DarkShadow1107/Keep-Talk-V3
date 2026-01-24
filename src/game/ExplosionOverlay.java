package game;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.*;

/**
 * Gestioneaza efectele vizuale complexe de explozie, inclusiv particule, spray-uri,
 * flash-uri pe ecran, tremuratul camerei si efectul de ecran spart.
 */
public class ExplosionOverlay extends JComponent {
    private List<Particle> particles = new ArrayList<>(); // Lista de particule de foc/fum
    private List<Shockwave> shockwaves = new ArrayList<>(); // Undele de soc circulare
    private List<Debris> debrisList = new ArrayList<>(); // Resturi/bucati care zboara
    private List<Path2D> cracks = new ArrayList<>(); // Efectul de ecran crapat
    private Timer animationTimer; // Timerul pentru animatie (aprox. 60 FPS)
    private Random random = new Random();
    private float flashIntensity = 0f; // Luminozitatea flash-ului alb
    private float shakeIntensity = 0f; // Cat de tare tremura ecranul
    private float glitchIntensity = 0f; // Intensitatea efectelor de glitch video
    private float crackOpacity = 1.0f; // Transparenta crapaturilor pe ecran
    private BufferedImage scanlineImage; // Imagine statica pentru efect CRT (linii de scanare)
    private long startTime; // Momentul la care a inceput explozia

    public ExplosionOverlay() {
        setOpaque(false);
        setFocusable(false);
    }

    /** Declanșează secvența completă de explozie. */
    public void explode(int centerX, int centerY) {
        particles.clear();
        shockwaves.clear();
        debrisList.clear();
        cracks.clear();
        flashIntensity = 1.0f;
        shakeIntensity = 20.0f;
        glitchIntensity = 1.0f;
        crackOpacity = 1.0f;
        startTime = System.currentTimeMillis();

        // Generam particulele exploziei
        for (int i = 0; i < 400; i++) {
            particles.add(new Particle(centerX, centerY));
        }
        
        // Generam resturile mecanice
        for (int i = 0; i < 50; i++) {
            debrisList.add(new Debris(centerX, centerY));
        }
        
        // Adaugam cateva unde de soc succesive
        shockwaves.add(new Shockwave(centerX, centerY, 5));
        shockwaves.add(new Shockwave(centerX, centerY, 15));
        shockwaves.add(new Shockwave(centerX, centerY, 30));
        
        // Generam crapaturile pe ecran
        generateCracks(centerX, centerY);

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animationTimer = new Timer(16, e -> {
            updateEffects();
            repaint();
            // Oprim timerul cand toate efectele au disparut
            if (particles.isEmpty() && shockwaves.isEmpty() && debrisList.isEmpty() && flashIntensity <= 0.01f && crackOpacity <= 0.01f) {
                ((Timer)e.getSource()).stop();
            }
        });
        animationTimer.start();
    }
    
    /** Genereaza cai geometrice neregulate pentru a simula ecranul spart. */
    private void generateCracks(int cx, int cy) {
        int numCracks = 5 + random.nextInt(5);
        for (int i = 0; i < numCracks; i++) {
            Path2D crack = new Path2D.Float();
            double angle = random.nextDouble() * Math.PI * 2;
            double dist = 0;
            double maxDist = Math.max(getWidth(), getHeight()) * 0.8;
            
            crack.moveTo(cx, cy);
            
            while (dist < maxDist) {
                dist += random.nextInt(50) + 20;
                angle += (random.nextDouble() - 0.5) * 0.5; // Unghi variabil pentru naturalete
                double x = cx + Math.cos(angle) * dist;
                double y = cy + Math.sin(angle) * dist;
                crack.lineTo(x, y);
            }
            cracks.add(crack);
        }
    }

    /** Actualizeaza pozitia si intensitatea fiecarui element in fiecare cadru. */
    private void updateEffects() {
        // Reducem treptat intensitatile pentru efect de fade-out
        flashIntensity *= 0.90f;
        shakeIntensity *= 0.90f;
        glitchIntensity *= 0.92f;
        
        // Incepem sa facem crapaturile transparente dupa 10 secunde
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > 10000) {
            crackOpacity *= 0.95f;
        }

        // Actualizam si eliminam particulele "moarte"
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update();
            if (!p.isAlive()) {
                it.remove();
            }
        }
        
        // Actualizam resturile
        Iterator<Debris> dit = debrisList.iterator();
        while (dit.hasNext()) {
            Debris d = dit.next();
            d.update();
            if (!d.isAlive()) {
                dit.remove();
            }
        }

        // Actualizam undele de soc
        Iterator<Shockwave> swIt = shockwaves.iterator();
        while (swIt.hasNext()) {
            Shockwave sw = swIt.next();
            sw.update();
            if (!sw.isAlive()) {
                swIt.remove();
            }
        }
    }

    /** Creeaza imaginea de overlay pentru liniile de scanare (aspect retro/TV). */
    private void createScanlineImage(int w, int h) {
        if (scanlineImage != null && scanlineImage.getWidth() == w && scanlineImage.getHeight() == h) return;
        
        scanlineImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scanlineImage.createGraphics();
        
        // Desenam liniile orizontale fine
        g2.setColor(new Color(0, 0, 0, 40));
        for (int y = 0; y < h; y += 3) {
            g2.drawLine(0, y, w, y);
        }
        
        // Adaugam un efect de vignetta (intunecare pe margini)
        RadialGradientPaint rgp = new RadialGradientPaint(
            w / 2, h / 2, (float)Math.hypot(w/2, h/2),
            new float[]{0.6f, 1.0f},
            new Color[]{new Color(0,0,0,0), new Color(0,0,0,150)}
        );
        g2.setPaint(rgp);
        g2.fillRect(0, 0, w, h);
        
        g2.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Aplicam tremuratul ecranului (Screen Shake)
        if (shakeIntensity > 0.5f) {
            int dx = (int)((random.nextFloat() - 0.5f) * shakeIntensity * 2);
            int dy = (int)((random.nextFloat() - 0.5f) * shakeIntensity * 2);
            g2.translate(dx, dy);
        }

        // Desenam overlay-ul CRT
        createScanlineImage(getWidth(), getHeight());
        g2.drawImage(scanlineImage, 0, 0, null);

        // Desenam artefactele de glitch computerizat
        if (glitchIntensity > 0.1f) {
            drawGlitches(g2);
        }

        // Desenam flash-ul alb de inceput
        if (flashIntensity > 0.01f) {
            g2.setColor(new Color(1f, 1f, 1f, flashIntensity * 0.9f));
            g2.fillRect(-50, -50, getWidth()+100, getHeight()+100);
        }

        // Desenam restul elementelor vizuale
        for (Shockwave sw : shockwaves) {
            sw.draw(g2);
        }
        
        for (Debris d : debrisList) {
            d.draw(g2);
        }

        // Draw Particles
        for (Particle p : particles) {
            p.draw(g2);
        }
        
        // Draw Cracks (Persist but fade)
        if (!cracks.isEmpty() && crackOpacity > 0.01f) {
            g2.setColor(new Color(200, 220, 255, (int)(100 * crackOpacity))); // Glass color
            g2.setStroke(new BasicStroke(2));
            for (Path2D crack : cracks) {
                g2.draw(crack);
            }
            // Highlight
            g2.setColor(new Color(255, 255, 255, (int)(150 * crackOpacity)));
            g2.setStroke(new BasicStroke(1));
             for (Path2D crack : cracks) {
                g2.draw(crack);
            }
        }
        
        // Reset transform
        if (shakeIntensity > 0.5f) {
            g2.translate(0, 0);
        }
    }

    private void drawGlitches(Graphics2D g2) {
        int w = getWidth();
        int h = getHeight();
        int numGlitches = (int)(glitchIntensity * 10);
        
        for (int i = 0; i < numGlitches; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int gw = random.nextInt(100) + 20;
            int gh = random.nextInt(10) + 2;
            
            Color c = random.nextBoolean() ? Color.CYAN : Color.MAGENTA;
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 100));
            g2.fillRect(x, y, gw, gh);
            
            // Random offset lines
            if (random.nextBoolean()) {
                g2.setColor(Color.WHITE);
                g2.drawLine(0, y, w, y);
            }
        }
    }

    private class Shockwave {
        double x, y;
        double radius;
        double expansionRate;
        float opacity = 1.0f;

        public Shockwave(int x, int y, double speed) {
            this.x = x;
            this.y = y;
            this.radius = 10;
            this.expansionRate = speed + 15;
        }

        public void update() {
            radius += expansionRate;
            opacity *= 0.92f;
        }

        public boolean isAlive() {
            return opacity > 0.01f;
        }

        public void draw(Graphics2D g2) {
            if (opacity <= 0) return;
            float strokeWidth = (float)(60 * opacity);
            if (strokeWidth < 1) strokeWidth = 1;
            
            g2.setStroke(new BasicStroke(strokeWidth));
            g2.setColor(new Color(1f, 0.9f, 0.6f, opacity));
            g2.draw(new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2));
        }
    }
    
    private class Debris {
        double x, y;
        double vx, vy;
        double rotation;
        double rotSpeed;
        float size;
        float life = 1.0f;
        Color color;
        Path2D shape;

        public Debris(int startX, int startY) {
            x = startX;
            y = startY;
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = random.nextDouble() * 30 + 10;
            vx = Math.cos(angle) * speed;
            vy = Math.sin(angle) * speed;
            rotation = random.nextDouble() * Math.PI * 2;
            rotSpeed = (random.nextDouble() - 0.5) * 0.5;
            size = random.nextFloat() * 15 + 5;
            
            int gray = random.nextInt(100);
            color = new Color(gray, gray, gray);
            
            // Create jagged shape
            shape = new Path2D.Double();
            shape.moveTo(-size, -size);
            shape.lineTo(size, -size/2);
            shape.lineTo(size/2, size);
            shape.lineTo(-size/2, size/2);
            shape.closePath();
        }

        public void update() {
            x += vx;
            y += vy;
            vx *= 0.95;
            vy *= 0.95;
            vy += 0.5; // Gravity
            rotation += rotSpeed;
            life -= 0.01f;
        }

        public boolean isAlive() {
            return life > 0;
        }

        public void draw(Graphics2D g2) {
            if (life <= 0) return;
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(life * 255)));
            
            g2.translate(x, y);
            g2.rotate(rotation);
            g2.fill(shape);
            g2.rotate(-rotation);
            g2.translate(-x, -y);
        }
    }

    private class Particle {
        double x, y;
        double vx, vy;
        float size;
        float life = 1.0f;
        float decay;
        Color color;
        boolean isSmoke;

        public Particle(int startX, int startY) {
            x = startX;
            y = startY;
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = random.nextDouble() * 25 + 5;
            vx = Math.cos(angle) * speed;
            vy = Math.sin(angle) * speed;
            
            isSmoke = random.nextInt(3) == 0;
            
            if (isSmoke) {
                size = random.nextFloat() * 50 + 20;
                decay = random.nextFloat() * 0.02f + 0.005f;
                int gray = random.nextInt(50) + 50;
                color = new Color(gray, gray, gray);
            } else {
                size = random.nextFloat() * 25 + 5;
                decay = random.nextFloat() * 0.04f + 0.01f;
                if (random.nextBoolean()) {
                    color = new Color(255, random.nextInt(150), 0); // Orange/Red
                } else {
                    color = new Color(255, 200 + random.nextInt(55), 100); // Yellow/White
                }
            }
        }

        public void update() {
            x += vx;
            y += vy;
            vx *= 0.92; // Drag
            vy *= 0.92;
            life -= decay;
            
            if (isSmoke) {
                size *= 1.02; // Smoke expands
                vy -= 0.15; // Smoke rises faster
            } else {
                size *= 0.95; // Fire shrinks
            }
        }

        public boolean isAlive() {
            return life > 0;
        }

        public void draw(Graphics2D g2) {
            if (life <= 0) return;
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(life * 255)));
            g2.fill(new Ellipse2D.Double(x - size/2, y - size/2, size, size));
        }
    }
}
