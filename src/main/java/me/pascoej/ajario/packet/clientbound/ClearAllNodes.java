package me.pascoej.ajario.packet.clientbound;

import me.pascoej.ajario.packet.PacketType;

import java.nio.ByteBuffer;

/**
 * Created by john on 6/14/15.
 */
public class ClearAllNodes extends ClientBoundPacket {
    public ClearAllNodes(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    @Override
    public PacketType getType() {
        return PacketType.ClientBound.CLEAR_ALL_NODES;
    }

    @Override
    public String toString() {
        return "ClearAllNodes{} " + super.toString();
    }
}
