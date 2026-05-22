package MungkinRpg.town;

import MungkinRpg.util.AssetLoader;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC {
    private int x, y;
    private String name;
    private String[] dialogues;
    private int currentDialogue;
    private Rectangle hitbox;
    private boolean talking;

    // Optional sprite sheet — put "villager.png" in assets/images/
    // Expected: single standing-facing sprite, at least 32×48 pixels
    private BufferedImage sprite;

    // Bob animation
    private int animTick = 0;

    public NPC(int x, int y, String name, String[] dialogues) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.dialogues = dialogues;
        this.currentDialogue = 0;
        this.hitbox = new Rectangle(x, y, 32, 48);
        this.talking = false;

        sprite = AssetLoader.loadImage("villager.png");
    }

    public void interact()     { talking = true; }
    public boolean isTalking() { return talking; }
    public Rectangle getHitbox() { return hitbox; }

    public void nextDialogue() {
        currentDialogue++;
        if (currentDialogue >= dialogues.length) {
            currentDialogue = 0;
            talking = false;
        }
    }

    // ----------------------------------------------------------------
    public void draw(Graphics2D g2) {
        animTick++;
        int bob = (int)(Math.sin(animTick * 0.05f) * 2); // subtle up-down bob
        g2.drawImage(sprite, x, y + bob, 32, 48, null);

        // Name tag (small floating text above)
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(name);
        int nx = x + 16 - tw / 2;
        int ny = y + bob - 4;
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(nx - 4, ny - 13, tw + 8, 16, 6, 6);
        g2.setColor(new Color(255, 230, 150));
        g2.drawString(name, nx, ny);
    }


    // ----------------------------------------------------------------
    public void drawDialogue(Graphics2D g2) {
        if (!talking) return;

        // Box
        g2.setColor(new Color(20, 15, 30, 220));
        g2.fillRoundRect(80, 445, 640, 130, 16, 16);
        g2.setColor(new Color(200, 160, 50));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(80, 445, 640, 130, 16, 16);
        g2.setStroke(new BasicStroke(1f));

        // Portrait area
        g2.setColor(new Color(50, 35, 20));
        g2.fillRoundRect(96, 460, 60, 60, 10, 10);
        g2.drawImage(sprite, 88, 458, 59, 60, null);

        // Speaker name
        g2.setFont(new Font("Serif", Font.BOLD, 16));
        g2.setColor(new Color(255, 200, 60));
        g2.drawString(name, 170, 480);

        // Dialogue text
        g2.setFont(new Font("Arial", Font.PLAIN, 15));
        g2.setColor(Color.WHITE);
        g2.drawString(dialogues[currentDialogue], 170, 505);

        // Continue hint
        g2.setFont(new Font("Arial", Font.ITALIC, 13));
        g2.setColor(new Color(180, 160, 120));
        g2.drawString("Press E to continue...", 540, 560);
    }
}