package me.pascoej.ajario.packet.serverbound;

import me.pascoej.ajario.packet.PacketType;

import java.nio.ByteBuffer;

/**
 * Created by john on 6/14/15.
 */
public class ConnectionResetPacket extends ServerBoundPacket {


    public PacketType getType() {
        return PacketType.ServerBound.RESET_CONNECTION;
    }


    @Override
    protected int size() {
        return 5;
    }

    @Override
    protected ByteBuffer addPayload(ByteBuffer byteBuffer) {
        byteBuffer.putInt(1);
        return byteBuffer;
    }
}
