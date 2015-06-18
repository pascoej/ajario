package me.pascoej.ajario;

import me.pascoej.ajario.gui.ClientGUI;
import me.pascoej.ajario.util.ServerChooserUtil;
import org.newdawn.slick.CanvasGameContainer;

import javax.swing.*;

/**
 * Created by john on 6/14/15.
 */
class GuiClientMain {
    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("Ajar - agar.io client by pascoej");
            AgarClient session = new AgarClient();
            session.connect(ServerChooserUtil.getServer("US-Fremont"));
            ClientGUI clientGUI = new ClientGUI(session);
            CanvasGameContainer appgc = new CanvasGameContainer(clientGUI);
            appgc.getContainer().setTargetFrameRate(60);
            appgc.getContainer().setVSync(false);
            appgc.getContainer().setShowFPS(false);
            appgc.getContainer().setMinimumLogicUpdateInterval(100);
            frame.add(appgc);
            frame.setSize(1280, 720);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
            appgc.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
