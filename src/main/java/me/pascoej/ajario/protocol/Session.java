package me.pascoej.ajario.protocol;

import me.pascoej.ajario.packet.AgarPacket;

import java.net.URI;
import java.util.Set;

/**
 * Created by john on 6/14/15.
 */
public class Session {
    private WebSocketHandler webSocketHandler;
    private URI uri;
    public Session(URI uri) {
        this.uri = uri;
        webSocketHandler = new WebSocketHandler(uri,this);
    }

    public void connect() {
        Set<PacketListener> packetListenerList = webSocketHandler.getPacketListeners();
        webSocketHandler = new WebSocketHandler(uri,this);
        webSocketHandler.getPacketListeners().addAll(packetListenerList);
        webSocketHandler.connect();
    }

    public void sendPacket(AgarPacket agarPacket) {
        if (!webSocketHandler.isOpen())
            return;
        try {
            webSocketHandler.sendPacket(agarPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        webSocketHandler.close();
        webSocketHandler.clearPacketListeners();
    }

    public void registerPacketListener(PacketListener packetListener) {
        webSocketHandler.registerPacketListener(packetListener);
    }

    public void unregisterPacketListener(PacketListener packetListener) {
        webSocketHandler.unregisterPacketListener(packetListener);
    }

    public boolean isReady() {
        return webSocketHandler.isOpen();
    }
}
