package me.pascoej.ajario.packet.serverbound;

import me.pascoej.ajario.packet.PacketType;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created by john on 6/14/15.
 */
public class SetNickname extends ServerBoundPacket {
    private final String nickName;

    public SetNickname(String nickName) {
        this.nickName = nickName;
    }

    @Override
    protected int size() {
        return 1 + 2 * nickName.length();
    }

    @Override
    protected ByteBuffer addPayload(ByteBuffer byteBuffer) {
        byteBuffer.put(StandardCharsets.UTF_16LE.encode(nickName));
        return byteBuffer;
    }

    @Override
    public PacketType getType() {
        return PacketType.ServerBound.SET_NICKNAME;
    }
}
