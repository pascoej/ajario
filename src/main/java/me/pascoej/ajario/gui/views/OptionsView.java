package me.pascoej.ajario.gui.views;

import me.pascoej.ajario.gui.ClientGUI;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Created by john on 6/15/15.
 */
public class OptionsView extends View {
    private final ClientGUI clientGUI;
    private static final String[] options = {"Show color guide", "Show masses"};

    public OptionsView(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        int width = gc.getWidth();
        int height = gc.getHeight();
        int border = 30;
        int padding = 10;
        g.setColor(new Color(110, 110, 110, 100));
        g.fillRoundRect(30, 30, width - 2 * border, height - 2 * border, 10);
        g.setColor(Color.white);
        int textX = border + padding;
        int textHeight = g.getFont().getHeight("|");
        for (int i = 0; i < options.length; i++) {
            int numbered = i + 1;
            String text = numbered + ". " + options[i];
            int y = border + padding + textHeight * i;
            g.drawString(text, textX, y);
        }
        int exitY = height - border - padding - textHeight;
        g.setColor(Color.red);
        g.drawString("Press `/~ (grave)to exit", textX, exitY);
    }

    private void pressNumber(int num) {
        switch (num) {
            case 1:
                clientGUI.getGameView().toggleGuide();
                break;
            case 2:
                clientGUI.getGameView().toggleMasses();
                break;
        }
    }


    @Override
    public boolean shouldDraw() {
        return true;
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void keyPressed(int key, char c) {
        switch (key) {
            case Keyboard.KEY_GRAVE:
                clientGUI.removeView(this);
                break;
            case 13:
                clientGUI.removeView(this);
                break;
            case Keyboard.KEY_1:
                pressNumber(1);
                break;
            case Keyboard.KEY_2:
                pressNumber(2);
                break;
            case Keyboard.KEY_3:
                pressNumber(3);
                break;
            case Keyboard.KEY_4:
                pressNumber(4);
                break;
            case Keyboard.KEY_5:
                pressNumber(5);
                break;
            case Keyboard.KEY_6:
                pressNumber(6);
                break;
            case Keyboard.KEY_7:
                pressNumber(7);
                break;
            case Keyboard.KEY_8:
                pressNumber(8);
                break;
            case Keyboard.KEY_9:
                pressNumber(9);
                break;
        }
    }
}
