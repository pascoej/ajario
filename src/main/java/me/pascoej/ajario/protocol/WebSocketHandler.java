package me.pascoej.ajario.protocol;

import me.pascoej.ajario.packet.AgarPacket;
import me.pascoej.ajario.packet.PacketType;
import me.pascoej.ajario.packet.clientbound.ClientBoundPacket;
import me.pascoej.ajario.packet.serverbound.ConnectionResetPacket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

/**
 * Created by john on 6/14/15.
 */
public class WebSocketHandler extends WebSocketClient {
    private final Set<PacketListener> packetListenerList = new HashSet<>();
    private boolean open = false;
    private Queue<AgarPacket> packetQueue = new ArrayDeque<>();
    private Session session;
    public WebSocketHandler(URI serverURI, Session session) {
        super(serverURI, new Draft_17(), headers(), 0);
        this.session = session;
    }

    public Set<PacketListener> getPacketListeners() {
        return packetListenerList;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        open = true;
        sendPacket(new ConnectionResetPacket());
        while (!packetQueue.isEmpty()) {
            AgarPacket agarPacket = packetQueue.poll();
            sendPacket(agarPacket);
        }
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public void onMessage(String s) {
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        try {
            bytes = bytes.order(ByteOrder.LITTLE_ENDIAN);
            AgarPacket agarPacket = AgarPacket.parseByteBuffer(bytes);
            if (agarPacket != null) {
                if (agarPacket.getType() == PacketType.ClientBound.UPDATE_POSITION_SIZE) {
                    System.out.println(agarPacket);
                }
                for (PacketListener packetListener : packetListenerList) {
                    packetListener.onRecvPacket((ClientBoundPacket) agarPacket);
                }
            } else {
                System.out.println("unknown packet: " + Arrays.toString(bytes.array()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        open = false;
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        this.close();
        if (session != null) {
            System.out.println("Reconnecting");
            session.connect();
        }
    }

    public void sendPacket(AgarPacket packet) {
        if (!open) {
            packetQueue.add(packet);
            return;
        }
        byte[] bytes = packet.toBytes();
        this.send(bytes);
    }

    public void registerPacketListener(PacketListener packetListener) {
        packetListenerList.add(packetListener);
    }

    public void unregisterPacketListener(PacketListener packetListener) {
        packetListenerList.remove(packetListener);
    }

    public void clearPacketListeners() {
        packetListenerList.clear();
    }

    private static Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Origin", "http://agar.io");
        return headers;
    }
}
