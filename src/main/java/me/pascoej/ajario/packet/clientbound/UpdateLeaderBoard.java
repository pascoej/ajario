package me.pascoej.ajario.packet.clientbound;

import me.pascoej.ajario.packet.PacketType;
import me.pascoej.ajario.util.ByteUtil;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by john on 6/14/15.
 */
public class UpdateLeaderBoard extends ClientBoundPacket {
    private final LeaderboardPosition[] leaderboardPositions;

    public UpdateLeaderBoard(ByteBuffer byteBuffer) {
        super(byteBuffer);
        int numberOfNodes = byteBuffer.getInt();
        leaderboardPositions = new LeaderboardPosition[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            int nodeId = byteBuffer.getInt();
            String name = ByteUtil.popString(byteBuffer);
            leaderboardPositions[i] = new LeaderboardPosition(nodeId, name);
        }
    }

    public LeaderboardPosition[] getLeaderboardPositions() {
        return leaderboardPositions;
    }

    @Override
    public PacketType getType() {
        return PacketType.ClientBound.UPDATE_LEADERBOARD;
    }

    public static class LeaderboardPosition {
        private final int nodeId;
        private final String nodeName;

        public LeaderboardPosition(int nodeId, String nodeName) {
            this.nodeId = nodeId;
            this.nodeName = nodeName;
        }

        public int getNodeId() {
            return nodeId;
        }

        public String getNodeName() {
            return nodeName;
        }

        @Override
        public String toString() {
            return "LeaderboardPosition{" +
                    "nodeId=" + nodeId +
                    ", nodeName='" + nodeName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UpdateLeaderBoard{" +
                "leaderboardPositions=" + Arrays.toString(leaderboardPositions) +
                "} " + super.toString();
    }
}
