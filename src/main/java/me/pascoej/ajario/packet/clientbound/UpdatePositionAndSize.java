package me.pascoej.ajario.packet.clientbound;

import me.pascoej.ajario.packet.PacketType;

import java.nio.ByteBuffer;

/**
 * Created by john on 6/14/15.
 */
public class UpdatePositionAndSize extends ClientBoundPacket {
    private final float x;
    private final float y;
    private final float size;

    public UpdatePositionAndSize(ByteBuffer byteBuffer) {
        super(byteBuffer);
        x = byteBuffer.getFloat();
        y = byteBuffer.getFloat();
        size = byteBuffer.getFloat();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSize() {
        return size;
    }

    @Override
    public PacketType getType() {
        return PacketType.ClientBound.UPDATE_POSITION_SIZE;
    }

    @Override
    public String toString() {
        return "UpdatePositionAndSize{" +
                "x=" + x +
                ", y=" + y +
                ", size=" + size +
                "} " + super.toString();
    }
}
