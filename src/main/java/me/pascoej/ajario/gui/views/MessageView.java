package me.pascoej.ajario.gui.views;

import me.pascoej.ajario.gui.ClientGUI;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Created by john on 6/15/15.
 */
public class MessageView extends View {
    private long startTime = -1;
    private final ClientGUI clientGUI;
    private final long duration;
    private final String message;

    public MessageView(ClientGUI clientGUI, String message, long duration) {
        this.clientGUI = clientGUI;
        this.message = message;
        this.duration = duration;
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        long currentTime = System.currentTimeMillis();
        if (startTime == -1) {
            startTime = currentTime;
        }
        long timeLeft = duration - (currentTime - startTime);
        if (timeLeft <= 0) {
            clientGUI.removeView(this);
            return;
        }
        double opacityModifier = 1;
        if (timeLeft < 3000) {
            opacityModifier = timeLeft / 3000.0;
        }
        g.setColor(new Color(150, 150, 150, (int) (175 * opacityModifier)));
        int width = gc.getWidth();
        int borderX = 50;
        int borderY = 5;
        int padding = 10;
        int notifWidth = width - (2 * borderX);
        int notifHeight = g.getFont().getHeight("|") + 2 * padding;

        int messageWidth = g.getFont().getWidth(message);
        int centerX = width / 2;
        int startMessageX = centerX - (messageWidth / 2);
        g.fillRoundRect(borderX, borderY, notifWidth, notifHeight, 4);
        g.setColor(new Color(0, 255, 0, (int) (200 * opacityModifier)));
        g.drawString(message, startMessageX, borderY + padding);
    }

    @Override
    public boolean shouldDraw() {
        return true;
    }

    @Override
    public boolean isAcceptingInput() {
        return false;
    }
}
