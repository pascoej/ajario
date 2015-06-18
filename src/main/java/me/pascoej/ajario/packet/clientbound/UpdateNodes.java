package me.pascoej.ajario.packet.clientbound;

import me.pascoej.ajario.packet.PacketType;
import me.pascoej.ajario.util.ByteUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by john on 6/14/15.
 */
public class UpdateNodes extends ClientBoundPacket {
    private final List<NodeData> nodeDataList;
    private final List<NodeEaten> nodeEatens;
    final int activeNodes;
    final int[] activeNodeIds;

    public UpdateNodes(ByteBuffer byteBuffer) {
        super(byteBuffer);
        short nodesToBeDestroyed = byteBuffer.getShort();
        nodeEatens = new ArrayList<>();
        for (int i = 0; i < nodesToBeDestroyed; i++) {
            int eaterId = byteBuffer.getInt();
            int eatedId = byteBuffer.getInt();
            NodeEaten nodeEaten = new NodeEaten(eaterId, eatedId);
            nodeEatens.add(nodeEaten);
        }
        nodeDataList = new ArrayList<>();
        while (true) {
            int nodeId = byteBuffer.getInt();
            if (nodeId == 0) {
                break;
            }

            short x = byteBuffer.getShort();
            short y = byteBuffer.getShort();
            short size = byteBuffer.getShort();

            byte r = byteBuffer.get();
            byte g = byteBuffer.get();
            byte b = byteBuffer.get();

            boolean virus = false;
            boolean agitated = false;
            byte flags = byteBuffer.get();
            int skips = 0;
            if (bitSet(flags, (byte) 1)) {
                virus = true;
            }
            if (bitSet(flags, (byte) 16)) {
                agitated = true;
            }
            if (bitSet(flags, (byte) 2)) {
                skips += 4;
            }
            if (bitSet(flags, (byte) 4)) {
                skips += 8;
            }
            if (bitSet(flags, (byte) 8)) {
                skips += 16;
            }
            byteBuffer.position(byteBuffer.position() + skips);
            String name = ByteUtil.popString(byteBuffer);
            NodeData nodeData = new NodeData(nodeId, x, y, size, r, g, b, name, virus, agitated);
            nodeDataList.add(nodeData);
        }
        activeNodes = byteBuffer.getInt();
        activeNodeIds = new int[activeNodes];
        for (int j = 0; j < activeNodes; j++) {
            activeNodeIds[j] = byteBuffer.getInt();
        }
    }

    private boolean bitSet(byte value1, byte value2) {
        return (value1 & value2) == value2;
    }


    public List<NodeData> getNodeDataList() {
        return nodeDataList;
    }

    public int getActiveNodes() {
        return activeNodes;
    }

    public int[] getActiveNodeIds() {
        return activeNodeIds;
    }

    @Override
    public PacketType getType() {
        return PacketType.ClientBound.UPDATE_NODES;
    }

    public static class NodeEaten {
        final int eaterId;
        final int eatedId;

        public NodeEaten(int eaterId, int eatedId) {
            this.eaterId = eaterId;
            this.eatedId = eatedId;
        }

        public int getEaterId() {
            return eaterId;
        }

        public int getEatedId() {
            return eatedId;
        }
    }

    public static class NodeData {
        private final int nodeId;
        private final short x;
        private final short y;
        private final short size;
        private final byte r;
        private final byte g;
        private final byte b;
        private final String name;
        private final boolean virus;
        private final boolean agitated;

        public NodeData(int nodeId, short x, short y, short size, byte r, byte g, byte b, String name, boolean virus, boolean agitated) {
            this.nodeId = nodeId;
            this.x = x;
            this.y = y;
            this.size = size;
            this.r = r;
            this.g = g;
            this.b = b;
            this.name = name;
            this.virus = virus;
            this.agitated = agitated;
        }

        public boolean isVirus() {
            return virus;
        }


        public int getNodeId() {
            return nodeId;
        }

        public short getX() {
            return x;
        }

        public short getY() {
            return y;
        }

        public short getSize() {
            return size;
        }

        public byte getR() {
            return r;
        }

        public byte getG() {
            return g;
        }

        public byte getB() {
            return b;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "NodeData{" +
                    "nodeId=" + nodeId +
                    ", x=" + x +
                    ", y=" + y +
                    ", size=" + size +
                    ", r=" + r +
                    ", g=" + g +
                    ", b=" + b +
                    ", name='" + name + '\'' +
                    ", virus=" + virus +
                    ", agitated=" + agitated +
                    '}';
        }
    }

    public List<NodeEaten> getNodeEatens() {
        return nodeEatens;
    }

    @Override
    public String toString() {
        return "UpdateNodes{" +
                "nodeDataList=" + nodeDataList +
                ", nodeEatens=" + nodeEatens +
                ", activeNodes=" + activeNodes +
                ", activeNodeIds=" + Arrays.toString(activeNodeIds) +
                "} " + super.toString();
    }
}
