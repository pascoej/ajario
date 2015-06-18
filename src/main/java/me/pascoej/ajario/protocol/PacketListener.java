package me.pascoej.ajario.protocol;

import me.pascoej.ajario.packet.clientbound.ClientBoundPacket;

/**
 * Created by john on 6/14/15.
 */
public interface PacketListener {
    void onRecvPacket(ClientBoundPacket agarPacket);
}
