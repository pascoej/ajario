package me.pascoej.ajario.gui.views;

import me.pascoej.ajario.gui.ClientGUI;
import me.pascoej.ajario.util.ServerChooserUtil;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.net.URI;

/**
 * Created by john on 6/15/15.
 */
public class ServerChooserView extends View {
    private final ClientGUI clientGUI;
    private final String[] regions = ServerChooserUtil.getRegions();
    private boolean searchForBest = false;

    public ServerChooserView(ClientGUI clientGUI) {
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
        for (int i = 0; i < regions.length; i++) {
            int numbered = i + 1;
            String text = numbered + ". " + regions[i];
            int y = border + padding + textHeight * i;
            g.drawString(text, textX, y);
        }
        int optionsY = border + padding + textHeight * regions.length;
        g.drawString("0. Options", textX, optionsY);

        int exitY = height - border - padding - textHeight;
        g.setColor(Color.red);
        g.drawString("Press `/~ (grave)to exit. Escape exits the game from anywhere.", textX, exitY);
        int bestY = exitY-textHeight-padding;
        g.setColor(Color.cyan);
        String searchToggleText = "Press B to enable search for best server mode. (takes time to search)";
        if (searchForBest) {
            searchToggleText = "Press B to disable search for best server mode. (takes time to search)";
        }
        g.drawString(searchToggleText,textX,bestY);
    }

    private void pressNumber(int num) {
        if (num > 0 && num <= regions.length) {
            int i = num - 1;
            String region = regions[i];
            final View view = this;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URI uri;
                    if (searchForBest) {
                        clientGUI.addView(new MessageView(clientGUI,"Searching for best server...",25000));
                        uri = ServerChooserUtil.bestServer(region,20);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        uri = ServerChooserUtil.getServer(region);
                    }
                    if (uri != null) {
                        clientGUI.getAgarClient().connect(uri);
                        clientGUI.removeView(view);
                        clientGUI.addView(new MessageView(clientGUI, "Connected to: " + region, 5000));
                    }
                }
            }).start();
        }
        if (num == 0) {
            clientGUI.removeView(this);
            clientGUI.addView(new OptionsView(clientGUI));
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
            case Keyboard.KEY_0:
                pressNumber(0);
                break;
            case Keyboard.KEY_B:
                searchForBest = !searchForBest;
                break;
        }
    }
}
