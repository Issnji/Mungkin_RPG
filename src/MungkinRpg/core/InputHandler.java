package MungkinRpg.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    public boolean up, down, left, right, attack, skill1, skill2, skill3, interact, escape;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_J -> attack = true;
            case KeyEvent.VK_K -> skill1 = true;
            case KeyEvent.VK_L -> skill2 = true;
            case KeyEvent.VK_SEMICOLON -> skill3 = true;
            case KeyEvent.VK_E -> interact = true;
            case KeyEvent.VK_ESCAPE -> escape = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_J -> attack = false;
            case KeyEvent.VK_K -> skill1 = false;
            case KeyEvent.VK_L -> skill2 = false;
            case KeyEvent.VK_SEMICOLON -> skill3 = false;
            case KeyEvent.VK_E -> interact = false;
            case KeyEvent.VK_ESCAPE -> escape = false;
        }
    }
}