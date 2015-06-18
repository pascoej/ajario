package me.pascoej.ajario.packet.clientbound;

import me.pascoej.ajario.packet.PacketType;

import java.nio.ByteBuffer;

/**
 * Created by john on 6/14/15.
 */
public class SetBorder extends ClientBoundPacket {
    private final double left;
    private final double bottom;
    private final double right;
    private final double top;

    public SetBorder(ByteBuffer byteBuffer) {
        super(byteBuffer);
        left = byteBuffer.getDouble();
        bottom = byteBuffer.getDouble();
        right = byteBuffer.getDouble();
        top = byteBuffer.getDouble();
    }

    public double getLeft() {
        return left;
    }

    public double getBottom() {
        return bottom;
    }

    public double getRight() {
        return right;
    }

    public double getTop() {
        return top;
    }

    @Override
    public String toString() {
        return "SetBorder{" +
                "left=" + left +
                ", bottom=" + bottom +
                ", right=" + right +
                ", top=" + top +
                "} " + super.toString();
    }

    @Override
    public PacketType getType() {
        return PacketType.ClientBound.SET_BORDER;
    }
}
