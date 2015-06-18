package me.pascoej.ajario.packet.clientbound;

import me.pascoej.ajario.packet.AgarPacket;
import me.pascoej.ajario.packet.PacketType;

import java.nio.ByteBuffer;

/**
 * Created by john on 6/14/15.
 */
public abstract class ClientBoundPacket implements AgarPacket {
    private final ByteBuffer byteBuffer;

    ClientBoundPacket(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
        byteBuffer.position(1);
    }

    @Override
    public byte[] toBytes() {
        return byteBuffer.array();
    }

    public PacketType.ClientBound packetCType() {
        return (PacketType.ClientBound) getType();
    }
}
