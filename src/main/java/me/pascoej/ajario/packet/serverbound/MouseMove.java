package me.pascoej.ajario.packet.serverbound;

import me.pascoej.ajario.packet.PacketType;

import java.nio.ByteBuffer;

/**
 * Created by john on 6/14/15.
 */
public class MouseMove extends ServerBoundPacket {
    private final double x;
    private final double y;

    public MouseMove(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected int size() {
        return 21;
    }

    @Override
    protected ByteBuffer addPayload(ByteBuffer byteBuffer) {
        byteBuffer.putDouble(x);
        byteBuffer.putDouble(y);
        byteBuffer.putInt(0);
        return byteBuffer;
    }

    @Override
    public PacketType getType() {
        return PacketType.ServerBound.MOUSE_MOVE;
    }
}
