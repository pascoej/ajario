package me.pascoej.ajario.gui.views;

import me.pascoej.ajario.AgarClient;
import me.pascoej.ajario.node.Node;
import me.pascoej.ajario.node.NodeType;
import me.pascoej.ajario.node.PlayerNode;
import me.pascoej.ajario.packet.clientbound.UpdateLeaderBoard;
import me.pascoej.ajario.util.Position;
import me.pascoej.ajario.util.ThreatInfo;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by john on 6/15/15.
 */
public class GameView extends View {
    private final AgarClient agarClient;
    private Position screenCenter;
    private double screenScale;

    public GameView(AgarClient agarClient) {
        this.agarClient = agarClient;
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) {
        int width = gameContainer.getWidth();
        int height = gameContainer.getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        screenScale = agarClient.getWorld().getScale() * Math.max(width / 1920.0, height / 1080.0);
        screenCenter = new Position(centerX, centerY);


        for (Node node : agarClient.getWorld().getNodes()) {
            Position inter = new Position(node.getX(), node.getY());
            Position screenPosition = toScreenCoords(inter);
            float size = (float) (node.getSize() * screenScale);
            float diameter = size * 2;
            if (outOfBounds(screenPosition, width, height, size)) {
                continue;
            }
            float x = (float) (screenPosition.getX() - size);
            float y = (float) (screenPosition.getY() - size);
            switch (node.getNodeType()) {
                case FOOD:
                    g.setColor(org.newdawn.slick.Color.white);
                    break;
                case VIRUS:
                    g.setColor(org.newdawn.slick.Color.green);
                    break;
                case PLAYER:
                    org.newdawn.slick.Color color = ThreatInfo.threatInfo(agarClient, (PlayerNode) node).getThreatLevel().getColor();
                    g.setColor(color);
                    break;
            }
            g.fillOval(x, y, diameter, diameter, 35);
            if (node.getNodeType() == NodeType.PLAYER) {
                g.setColor(org.newdawn.slick.Color.lightGray);
                PlayerNode playerNode = (PlayerNode) node;
                float textX = (float) (screenPosition.getX() - (.5 * g.getFont().getWidth(playerNode.getName())));
                float textY = (float) (screenPosition.getY() - 15);
                if (playerNode.getName() != null) {
                    g.drawString(playerNode.getName(), textX, textY);
                }
                if (showMasses) {
                    g.drawString("(" + playerNode.getSize() + ")", textX, textY + g.getFont().getHeight("|") + 1);
                }
            }
        }
        drawLeaderBoard(gameContainer, g);
        if (guide) {
            drawHelp(g);
        }
    }

    @Override
    public boolean shouldDraw() {
        return true;
    }

    private Position toWorldCoords(Position screen) {
        double wX = (screen.getX() - screenCenter.getX()) * screenScale + agarClient.getWorld().getCenterX();
        double wY = (screen.getY() - screenCenter.getY()) * screenScale + agarClient.getWorld().getCenterY();
        return new Position(wX, wY);
    }

    private Position toScreenCoords(Position world) {
        double sX = (world.getX() - agarClient.getWorld().getCenterX()) * screenScale + screenCenter.getX();
        double sY = (world.getY() - agarClient.getWorld().getCenterY()) * screenScale + screenCenter.getY();
        return new Position(sX, sY);
    }

    private void drawLeaderBoard(GameContainer gc, org.newdawn.slick.Graphics g) {
        UpdateLeaderBoard.LeaderboardPosition[] leaderboard = agarClient.getLeaderBoard();
        int width = 150;
        int charHeight = g.getFont().getHeight("|") + 4;
        int height = charHeight * leaderboard.length + 10;
        org.newdawn.slick.Color bg = new org.newdawn.slick.Color(150, 150, 150, 100);
        int bgX = gc.getWidth() - width;
        g.setColor(bg);
        g.fillRect(bgX, 0, width, height);
        org.newdawn.slick.Color text = new org.newdawn.slick.Color(256, 256, 256, 125);
        g.setColor(text);

        int textX = bgX + 4;
        for (int i = 0; i < leaderboard.length; i++) {
            UpdateLeaderBoard.LeaderboardPosition leaderboardPosition = leaderboard[i];
            String cellName = leaderboardPosition.getNodeName();
            if (cellName.equals("")) {
                cellName = "Unamed: " + leaderboardPosition.getNodeId();
            }
            String line = (i + 1) + ". " + cellName;
            int textY = i * charHeight;
            g.drawString(line, textX, textY);
        }
    }

    private void drawHelp(Graphics g) {
        org.newdawn.slick.Color bg = new org.newdawn.slick.Color(150, 150, 150, 100);
        org.newdawn.slick.Color text = new org.newdawn.slick.Color(256, 256, 256, 125);
        int charHeight = g.getFont().getHeight("|") + 4;
        g.setColor(bg);
        g.fillRoundRect(5, 5, 150, charHeight * ThreatInfo.ThreatLevel.values().length + 5, 3);
        ThreatInfo.ThreatLevel[] values = ThreatInfo.ThreatLevel.values();
        for (int i = 0; i < values.length; i++) {
            ThreatInfo.ThreatLevel threatLevel = values[i];
            g.setColor(threatLevel.getColor());
            int boxX = 5;
            int textX = boxX + charHeight + 2;
            int y = i * charHeight + 7;
            g.fillRect(boxX, y, charHeight, charHeight);
            g.setColor(text);
            g.drawString(threatLevel.toString(), textX, y);
        }
    }

    private boolean outOfBounds(Position screenPos, int maxWidth, int maxHeight, float size) {
        double x = screenPos.getX();
        double y = screenPos.getY();
        return x < -size || y < -size || x > maxWidth + size || y > maxHeight + size;
    }

    private boolean guide = false;

    public void toggleGuide() {
        guide = !guide;
    }

    private boolean showMasses = true;

    public void toggleMasses() {
        showMasses = !showMasses;
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Keyboard.KEY_SPACE) {
            agarClient.split();
        } else if (key == Keyboard.KEY_W) {
            agarClient.ejectMass();
        } else if (key == Keyboard.KEY_Q) {
            Timer timer = new Timer();
            final int[] i = {7};
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    agarClient.update();
                    agarClient.ejectMass();
                    i[0] -= 1;
                    if (i[0] <= 0) {
                        this.cancel();
                    }
                }
            }, 50, 50);
        } else if (key == Keyboard.KEY_P) {
            agarClient.joinGame();
        }
        if (agarClient.isDead() && key == Keyboard.KEY_SPACE) {
            agarClient.joinGame();
        }
    }

    @Override
    public void mouseMoved(int i, int i1, int x, int y) {
        Position gamePos = toWorldCoords(new Position(x, y));
        agarClient.setMousePosition(gamePos.getX(), gamePos.getY());
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }
}
