package me.pascoej.ajario.packet;

import me.pascoej.ajario.packet.clientbound.*;

import java.nio.ByteBuffer;

/**
 * Created by john on 6/14/15.
 */
public interface AgarPacket {
    byte[] toBytes();

    PacketType getType();

    static AgarPacket parseByteBuffer(ByteBuffer byteBuffer) {
        if (byteBuffer.capacity() == 0) {
            return null;
        }
        PacketType.ClientBound packetType = PacketType.ClientBound.packetType(byteBuffer.get());
        if (packetType == null) {
            return null;
        }
        switch (packetType) {
            case UPDATE_NODES:
                return new UpdateNodes(byteBuffer);
            case UPDATE_POSITION_SIZE:
                return new UpdatePositionAndSize(byteBuffer);
            case CLEAR_ALL_NODES:
                return new ClearAllNodes(byteBuffer);
            case ADD_NODE:
                return new AddNode(byteBuffer);
            case UPDATE_LEADERBOARD:
                return new UpdateLeaderBoard(byteBuffer);
            case SET_BORDER:
                return new SetBorder(byteBuffer);
        }
        return null;
    }
}
