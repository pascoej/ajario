package me.pascoej.ajario.packet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by john on 6/14/15.
 */
public interface PacketType {
    enum ServerBound implements PacketType {
        SET_NICKNAME(0), SPECTATE(1), MOUSE_MOVE(16), SPLIT(17), EJECT_MASS(21), RESET_CONNECTION(255);
        private final byte id;

        ServerBound(int id) {
            this.id = (byte) id;
        }

        public byte getId() {
            return id;
        }

        private static final Map<Byte, ServerBound> packetTypeById = Collections.unmodifiableMap(initPacketTypeById());

        private static Map<Byte, ServerBound> initPacketTypeById() {
            Map<Byte, ServerBound> map = new HashMap<>();
            for (ServerBound packetType : values()) {
                map.put(packetType.getId(), packetType);
            }
            return map;
        }

        public static ServerBound packetType(byte id) {
            return packetTypeById.get(id);
        }
    }


    enum ClientBound implements PacketType {
        UPDATE_NODES(16), UPDATE_POSITION_SIZE(17), CLEAR_ALL_NODES(20), ADD_NODE(32), UPDATE_LEADERBOARD(49),
        SET_BORDER(64);
        private final byte id;

        ClientBound(int id) {
            this.id = (byte) id;
        }

        public byte getId() {
            return id;
        }

        private static final Map<Byte, ClientBound> packetTypeById = Collections.unmodifiableMap(initPacketTypeById());

        private static Map<Byte, ClientBound> initPacketTypeById() {
            Map<Byte, ClientBound> map = new HashMap<>();
            for (ClientBound packetType : values()) {
                map.put(packetType.getId(), packetType);
            }
            return map;
        }

        public static ClientBound packetType(byte id) {
            return packetTypeById.get(id);
        }
    }

    byte getId();
}
