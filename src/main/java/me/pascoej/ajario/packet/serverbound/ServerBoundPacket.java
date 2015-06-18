package me.pascoej.ajario.packet.serverbound;

import me.pascoej.ajario.packet.AgarPacket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by john on 6/14/15.
 */
public abstract class ServerBoundPacket implements AgarPacket {
    protected ByteBuffer getByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(size()).order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.put(this.getType().getId());
        addPayload(byteBuffer);
        return byteBuffer;
    }

    protected abstract int size();

    protected abstract ByteBuffer addPayload(ByteBuffer byteBuffer);

    @Override
    public byte[] toBytes() {
        return getByteBuffer().array();
    }
}
