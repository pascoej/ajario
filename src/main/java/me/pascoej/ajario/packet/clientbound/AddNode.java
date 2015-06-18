package me.pascoej.ajario.packet.clientbound;

import me.pascoej.ajario.packet.PacketType;

import java.nio.ByteBuffer;

/**
 * Created by john on 6/14/15.
 */
public class AddNode extends ClientBoundPacket {
    private final int nodeId;

    public AddNode(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.nodeId = byteBuffer.getInt();
    }

    public int getNodeId() {
        return nodeId;
    }

    @Override
    public PacketType getType() {
        return PacketType.ClientBound.ADD_NODE;
    }

    @Override
    public String toString() {
        return "AddNode{" +
                "nodeId=" + nodeId +
                "} " + super.toString();
    }
}
