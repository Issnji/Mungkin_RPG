package MungkinRpg.ui;

import MungkinRpg.util.AssetLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Main menu berbasis Swing — menggunakan JButton asli dengan styling kustom.
 * Jauh lebih mudah dari menggambar tombol manual di Graphics2D:
 *  - Hover effect otomatis lewat MouseListener
 *  - Klik ditangani lewat ActionListener biasa
 *  - Background tetap digambar di paintComponent() untuk tampilan game
 */
public class MainMenu extends JPanel {

    private final Runnable onPlay;
    private final Timer    animTimer;
    private int   animTick   = 0;
    private float torchFlick = 0f;

    private BufferedImage bgImage; // opsional: taruh menu_bg.png di assets/images/

    public MainMenu(Runnable onPlay) {
        this.onPlay = onPlay;
        setLayout(null);                                  // absolute positioning
        setPreferredSize(new Dimension(800, 600));

        bgImage = AssetLoader.loadImage("menu_bg.png");  // null jika tidak ada

        // ---- Tombol PLAY ----
        JButton playBtn = createGameButton("PLAY",
                new Color(100, 70, 0), new Color(255, 210, 50));
        playBtn.setBounds(290, 300, 220, 56);
        playBtn.addActionListener(e -> onPlay.run());
        add(playBtn);

        // ---- Tombol EXIT ----
        JButton exitBtn = createGameButton("EXIT",
                new Color(80, 20, 20), new Color(255, 120, 100));
        exitBtn.setBounds(290, 370, 220, 56);
        exitBtn.addActionListener(e -> System.exit(0));
        add(exitBtn);

        // ---- Timer animasi untuk kedip obor, dll. ----
        animTimer = new Timer(16, e -> {
            animTick++;
            torchFlick = (float)(Math.sin(animTick * 0.12) * 0.5 + 0.5);
            repaint();
        });
        animTimer.start();
    }

    /** Berhenti animasi saat panel tidak aktif (hemat CPU). */
    public void stopAnimation() { animTimer.stop(); }
    /** Mulai kembali animasi saat panel aktif. */
    public void startAnimation() { animTimer.start(); }

    // ----------------------------------------------------------------
    // Background + judul digambar di sini
    // ----------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, 800, 600, null);
            g2.setColor(new Color(0, 0, 0, 110));
            g2.fillRect(0, 0, 800, 600);
        } else {
            drawBackground(g2);
        }

        drawTitle(g2);

        // Hint kontrol di bawah layar
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(150, 130, 160));
        String hint = "Klik tombol atau gunakan mouse untuk navigasi";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(hint, (800 - fm.stringWidth(hint)) / 2, 574);
    }

    // ----------------------------------------------------------------
    // Factory: buat JButton bergaya game
    // ----------------------------------------------------------------
    private JButton createGameButton(String text, Color bgColor, Color borderColor) {

        JButton btn = new JButton(text) {

            // State hover internal
            boolean hovered = false;
            boolean pressed = false;

            @Override
            protected void paintComponent(Graphics g) {
                // Jangan panggil super — kita gambar sendiri sepenuhnya
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();

                if (hovered) {
                    // Glow luar
                    g2.setColor(new Color(borderColor.getRed(),
                            borderColor.getGreen(),
                            borderColor.getBlue(), 60));
                    g2.fillRoundRect(-6, -6, w + 12, h + 12, 18, 18);

                    // Isi tombol
                    GradientPaint grad = new GradientPaint(
                            0, 0, bgColor.brighter(),
                            0, h, bgColor);
                    g2.setPaint(grad);
                } else {
                    g2.setColor(new Color(bgColor.getRed(),
                            bgColor.getGreen(),
                            bgColor.getBlue(), 200));
                }

                g2.fillRoundRect(0, 0, w, h, 12, 12);

                // Border
                g2.setColor(pressed ? borderColor.darker() : borderColor);
                g2.setStroke(new BasicStroke(hovered ? 2.5f : 1.5f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);
                g2.setStroke(new BasicStroke(1f));

                // Teks
                g2.setFont(new Font("Serif", Font.BOLD, 26));
                g2.setColor(hovered
                        ? new Color(255, 235, 100)
                        : new Color(200, 180, 210));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(text,
                        (w - fm.stringWidth(text)) / 2,
                        (h + fm.getAscent() - fm.getDescent()) / 2);
            }

            // Pasang listener di initializer block
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) {
                        hovered = true; repaint();
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                    @Override public void mouseExited(MouseEvent e) {
                        hovered = false; pressed = false; repaint();
                        setCursor(Cursor.getDefaultCursor());
                    }
                    @Override public void mousePressed(MouseEvent e) {
                        pressed = true; repaint();
                    }
                    @Override public void mouseReleased(MouseEvent e) {
                        pressed = false; repaint();
                    }
                });
            }
        };

        // Matikan semua default Swing chrome
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);

        return btn;
    }

    // ----------------------------------------------------------------
    // Gambar background kastil (fallback kalau tidak ada menu_bg.png)
    // ----------------------------------------------------------------
    private void drawBackground(Graphics2D g2) {
        GradientPaint sky = new GradientPaint(0, 0, new Color(8, 4, 20),
                0, 420, new Color(25, 15, 50));
        g2.setPaint(sky);
        g2.fillRect(0, 0, 800, 600);

        // Bintang
        for (int i = 0; i < 60; i++) {
            int sx = (i * 137 + 23) % 800;
            int sy = (i * 89 + 11) % 350;
            int a  = (i % 4 == 0) ? 100 + (int)(torchFlick * 155) : 180;
            g2.setColor(new Color(255, 255, 255, a));
            g2.fillOval(sx, sy, i % 5 == 0 ? 3 : 2, i % 5 == 0 ? 3 : 2);
        }
        // Bulan
        g2.setColor(new Color(240, 235, 200));
        g2.fillOval(660, 40, 60, 60);
        g2.setColor(new Color(25, 15, 50));
        g2.fillOval(678, 36, 60, 60);
        // Tanah
        g2.setPaint(new GradientPaint(0, 420, new Color(18, 12, 8),
                0, 600, new Color(6, 4, 3)));
        g2.fillRect(0, 420, 800, 180);
        // Kastil
        g2.setColor(new Color(12, 8, 18));
        g2.fillRect(150, 330, 500, 270);
        for (int bx = 150; bx < 650; bx += 40) g2.fillRect(bx, 310, 25, 25);
        g2.fillRect(100, 260, 100, 340);
        g2.fillPolygon(new int[]{100, 200, 150}, new int[]{240, 240, 180}, 3);
        g2.fillRect(600, 260, 100, 340);
        g2.fillPolygon(new int[]{600, 700, 650}, new int[]{240, 240, 180}, 3);
        g2.setColor(new Color(6, 4, 10));
        g2.fillRect(350, 380, 100, 120);
        g2.fillArc(350, 360, 100, 50, 0, 180);
        drawTorch(g2, 300, 340);
        drawTorch(g2, 500, 340);
    }

    private void drawTorch(Graphics2D g2, int tx, int ty) {
        int gr = 30 + (int)(torchFlick * 15);
        g2.setColor(new Color(255, 150, 30, 60 + (int)(torchFlick * 60)));
        g2.fillOval(tx - gr/2, ty - gr/2, gr, gr);
        g2.setColor(new Color(255, 200, 50, 180 + (int)(torchFlick * 75)));
        g2.fillOval(tx - 5, ty - 8, 10, 14);
        g2.setColor(new Color(100, 70, 40));
        g2.fillRect(tx - 2, ty, 4, 14);
    }

    private void drawTitle(Graphics2D g2) {
        g2.setFont(new Font("Serif", Font.BOLD, 62));
        // Shadow
        g2.setColor(new Color(0, 0, 0, 160));
        drawCentered(g2, "DUNGEON RPG", 800, 189);
        // Warna emas
        g2.setColor(new Color(255, 220, 70));
        drawCentered(g2, "DUNGEON RPG", 800, 185);
        // Subtitle
        g2.setFont(new Font("Serif", Font.ITALIC, 18));
        g2.setColor(new Color(180, 160, 120));
        drawCentered(g2, "Enter the depths. Face your fate.", 800, 218);
    }

    private void drawCentered(Graphics2D g2, String text, int w, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, (w - fm.stringWidth(text)) / 2, y);
    }
}