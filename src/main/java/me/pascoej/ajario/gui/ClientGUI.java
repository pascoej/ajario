package me.pascoej.ajario.gui;

import me.pascoej.ajario.AgarClient;
import me.pascoej.ajario.gui.views.GameView;
import me.pascoej.ajario.gui.views.OptionsView;
import me.pascoej.ajario.gui.views.ServerChooserView;
import me.pascoej.ajario.gui.views.View;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by john on 6/14/15.
 */
public class ClientGUI extends BasicGame {
    private final AgarClient agarClient;
    private boolean showFPS = false;
    private boolean fullscreen = false;
    private final List<View> views = new CopyOnWriteArrayList<>();
    private GameContainer gameContainer;

    public ClientGUI(AgarClient agarClient) {
        super("Ajar Client");
        this.agarClient = agarClient;
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Keyboard.KEY_BACKSLASH) {
            showFPS = !showFPS;
        } else if (key == Keyboard.KEY_9) {
            fullscreen = !fullscreen;
        } else if (key == Keyboard.KEY_ESCAPE) {
            System.exit(0);
        } else if ((key == Keyboard.KEY_GRAVE || key == 13) && !hasViewClass(ServerChooserView.class) && !hasViewClass(OptionsView.class)) {
            addView(new ServerChooserView(this));
        }
        if (key == Keyboard.KEY_M) {
            agarClient.serverResetPacket();
        }
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        this.gameContainer = gameContainer;
        gameContainer.setVSync(true);
        addView(new GameView(agarClient));
    }

    private boolean hasViewClass(Class clazz) {
        for (View view : views) {
            if (view.getClass() == clazz) {
                return true;
            }
        }
        return false;
    }

    public void addView(View view) {
        views.add(view);
        gameContainer.getInput().addListener(view);
    }

    public GameView getGameView() {
        return (GameView) views.get(0);
    }

    public AgarClient getAgarClient() {
        return agarClient;
    }

    public void removeView(View view) {
        views.remove(view);
        gameContainer.getInput().removeListener(view);
    }

    int refresh = 0;

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        if (gameContainer.isShowingFPS() != showFPS) {
            gameContainer.setShowFPS(showFPS);
        }
        if (gameContainer.isFullscreen() != fullscreen) {
            if (fullscreen) {
                if (gameContainer instanceof AppGameContainer) {
                    AppGameContainer appGameContainer = (AppGameContainer) gameContainer;
                    appGameContainer.setDisplayMode(1440, 900, true);
                    init(gameContainer);
                }
            }
        }
        agarClient.update();
    }


    @Override
    public void render(GameContainer gameContainer, Graphics g) throws SlickException {
        if (showFPS) {
            g.setColor(Color.white);
            String text = "u/s: " + agarClient.getUpdatePerSecond();
            int textHeight = g.getFont().getHeight(text);
            int textWidth = g.getFont().getWidth(text);
            int textX = gameContainer.getWidth()-5-textWidth;
            int textY = gameContainer.getHeight()-5-textHeight;
            g.drawString(text,textX,textY);
        }
        views.stream().filter(View::shouldDraw).forEach(view -> view.render(gameContainer, g));
    }
}
