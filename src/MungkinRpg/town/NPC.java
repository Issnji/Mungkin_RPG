package MungkinRpg.town;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;

public class NPC {
    private int x, y;
    private String name;
    private String[] dialogues;
    private int currentDialogue;
    private Rectangle hitbox;
    private boolean talking;

    public NPC(int x, int y, String name, String[] dialogues) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.dialogues = dialogues;
        this.currentDialogue = 0;
        this.hitbox = new Rectangle(x, y, 32, 48);
        this.talking = false;
    }

    public void interact() {
        talking = true;
    }

    public void nextDialogue() {
        currentDialogue++;
        if (currentDialogue >= dialogues.length) {
            currentDialogue = 0;
            talking = false;
        }
    }

    public boolean isTalking() { return talking; }
    public Rectangle getHitbox() { return hitbox; }

    public void draw(Graphics2D g2) {
        // Body
        g2.setColor(new Color(100, 80, 60));
        g2.fillRect(x, y, 32, 48);
        // Head
        g2.setColor(new Color(255, 200, 150));
        g2.fillOval(x + 4, y, 24, 20);

        // Name tag
        g2.setColor(Color.WHITE);
        g2.drawString(name, x - 5, y - 5);
    }

    public void drawDialogue(Graphics2D g2) {
        if (!talking) return;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(100, 450, 600, 120);
        g2.setColor(Color.WHITE);
        g2.drawRect(100, 450, 600, 120);

        g2.setColor(Color.YELLOW);
        g2.drawString(name + ":", 120, 480);
        g2.setColor(Color.WHITE);
        g2.drawString(dialogues[currentDialogue], 120, 510);
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Press E to continue...", 500, 550);
    }
}