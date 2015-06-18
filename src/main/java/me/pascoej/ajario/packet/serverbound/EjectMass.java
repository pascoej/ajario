package me.pascoej.ajario.packet.serverbound;

import me.pascoej.ajario.packet.PacketType;

import java.nio.ByteBuffer;

/**
 * Created by john on 6/14/15.
 */
public class EjectMass extends ServerBoundPacket {
    @Override
    protected int size() {
        return 1;
    }

    @Override
    protected ByteBuffer addPayload(ByteBuffer byteBuffer) {
        return byteBuffer;
    }

    @Override
    public PacketType getType() {
        return PacketType.ServerBound.EJECT_MASS;
    }
}
