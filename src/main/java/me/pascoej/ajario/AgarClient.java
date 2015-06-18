package me.pascoej.ajario;

import me.pascoej.ajario.node.FoodNode;
import me.pascoej.ajario.node.Node;
import me.pascoej.ajario.node.PlayerNode;
import me.pascoej.ajario.node.VirusNode;
import me.pascoej.ajario.packet.clientbound.*;
import me.pascoej.ajario.packet.serverbound.*;
import me.pascoej.ajario.protocol.PacketListener;
import me.pascoej.ajario.protocol.Session;

import java.net.URI;

/**
 * Created by john on 6/14/15.
 */
public class AgarClient implements PacketListener {
    private final World world = new World();
    private Session session;
    private String nickname = "Scotty";
    private double offsetX, offsetY;


    public AgarClient() {
    }

    public void connect(URI uri) {
        if (session != null && session.isReady()) {
            session.close();
            world.clearAll();
        }
        session = new Session(uri);
        session.registerPacketListener(this);
        session.connect();
    }

    public String getNickname() {
        return nickname;
    }

    public void enterGame(String name) {
        nickname = name;
        session.sendPacket(new SetNickname(name));
    }

    public World getWorld() {
        return world;
    }
    @Override
    public void onRecvPacket(ClientBoundPacket agarPacket) {
        //System.out.println(agarPacket.getType() + ":" + agarPacket);
        switch (agarPacket.packetCType()) {
            case UPDATE_NODES:
                UpdateNodes updateNodes = (UpdateNodes) agarPacket;
                handleUpdateNodes(updateNodes);
                break;
            case UPDATE_POSITION_SIZE:
                UpdatePositionAndSize updatePositionAndSize = (UpdatePositionAndSize) agarPacket;
                world.setCenterSize(updatePositionAndSize.getX(), updatePositionAndSize.getY(), updatePositionAndSize.getSize());
                break;
            case CLEAR_ALL_NODES:
                world.clearAll();
                break;
            case ADD_NODE:
                AddNode addNode = (AddNode) agarPacket;
                int nodeId = addNode.getNodeId();
                PlayerNode node = (PlayerNode) world.getNode(nodeId);
                if (node == null) {
                    node = new PlayerNode(nodeId);
                    node.setMine();
                    world.addClientsNode(node);
                }
                break;
            case UPDATE_LEADERBOARD:
                UpdateLeaderBoard updateLeaderBoard = (UpdateLeaderBoard) agarPacket;
                leaderBoard = updateLeaderBoard.getLeaderboardPositions();
                break;
            case SET_BORDER:
                break;
        }
    }

    private UpdateLeaderBoard.LeaderboardPosition[] leaderBoard = new UpdateLeaderBoard.LeaderboardPosition[0];

    public UpdateLeaderBoard.LeaderboardPosition[] getLeaderBoard() {
        return leaderBoard;
    }

    public void setMousePosition(double x, double y) {
        double xOffset = x - world.getCenterX();
        double yOffset = y - world.getCenterY();
        this.offsetX = xOffset;
        this.offsetY = yOffset;
        //if (xOffset < 25 && yOffset < 25) {
        //    mag = 0;
        //    session.sendPacket(new MouseMove(world.getCenterX(), getWorld().getCenterY()));
        //}else if (mag == 0){
        //    mag = 400;
        //}
        //if (yOffset == 0) {
        //    if (xOffset == 0) {
        //        mag = 0;
        //    }else if (xOffset > 0) {
        //        direction = 0;
        //    }else {
        //        direction = (float) Math.PI;
        //    }
        //}
        //float tan = (float) (yOffset/xOffset);
        //direction = (float) Math.atan(tan);
//
        //System.out.println(direction);
        session.sendPacket(new MouseMove(x, y));
    }

    public void update() {
        if (!session.isReady())
            return;
        //if (mag == 0) {
        //    return;
        //}
        //mag += 50;
        //if (mag > 1000) {
        //    mag = 400;
        //}
        //double x = Math.cos(direction)*mag + world.getCenterX();
        //double y = Math.sin(direction)*mag + world.getCenterY();
        double x = offsetX + getWorld().getCenterX();
        double y = offsetY + getWorld().getCenterY();
        session.sendPacket(new MouseMove(x, y));
    }

    public void split() {
        session.sendPacket(new Split());
    }

    public void ejectMass() {
        session.sendPacket(new EjectMass());
    }

    public void joinGame() {
        session.sendPacket(new SetNickname(nickname));
    }
    public void serverResetPacket() {
        session.sendPacket(new ConnectionResetPacket());
    }

    public boolean isDead() {
        return world.getClientNodes().isEmpty();
    }
    private static final int RECALCULATE_INTERVAL = 50;
    private int updatePackets = 0;
    private long lastUpdateNodeTime = System.currentTimeMillis();
    private double updatePerSecond = 0;

    public int getUpdatePerSecond() {
        return (int) updatePerSecond;
    }

    private void handleUpdateNodes(UpdateNodes updateNodes) {
        updatePackets += 1;
        if (updatePackets >= RECALCULATE_INTERVAL) {
            long currentTime = System.currentTimeMillis();
            long elapsed = currentTime - lastUpdateNodeTime;
            updatePerSecond = (double) updatePackets/((double)elapsed/1000.0);
        }
        for (UpdateNodes.NodeData nodeData : updateNodes.getNodeDataList()) {
            int id = nodeData.getNodeId();
            Node node = world.getNode(id);
            if (node == null) {
                if (nodeData.isVirus()) {
                    node = new VirusNode(id);
                } else if (nodeData.getSize() < 20 && nodeData.getName().equals("")) {
                    node = new FoodNode(id);
                } else {
                    node = new PlayerNode(id);
                }
                world.addNode(node);
            }
            node.updatePositionSize(nodeData.getX(), nodeData.getY(), nodeData.getSize());
            if (!nodeData.getName().equals("")) {
                if (node instanceof PlayerNode) {
                    ((PlayerNode) node).setName(nodeData.getName());
                } else {
                    System.out.println("VERY WRONG");
                }
            }
        }
        for (UpdateNodes.NodeEaten nodeEaten : updateNodes.getNodeEatens()) {
            Node eaten = world.getNode(nodeEaten.getEatedId());
            if (eaten == null) {
                continue;
            }
            world.removeNode(eaten);
            if (eaten instanceof PlayerNode && ((PlayerNode) eaten).isMine()) {
                System.out.println("One of our cells got eaten");
                if (world.getClientNodes().size() == 0) {
                    System.out.println("We are dead");
                }
            }
            //System.out.println("removed" + eaten);
        }
        for (int id : updateNodes.getActiveNodeIds()) {
            Node node = world.getNode(id);
            if (node != null) {
                world.removeNode(node);
                //System.out.println("Node disappeared: " + node);
            }
        }
        if (!world.getClientNodes().isEmpty()) {
            double totalSize = world.getClientNodes().stream().mapToDouble(Node::getSize).sum();
            double left = world.getClientNodes().stream().mapToDouble(Node::getX).min().getAsDouble();
            double right = world.getClientNodes().stream().mapToDouble(Node::getX).max().getAsDouble();
            double top = world.getClientNodes().stream().mapToDouble(Node::getY).max().getAsDouble();
            double bottom = world.getClientNodes().stream().mapToDouble(Node::getY).min().getAsDouble();
            double centerX = (right + left) / 2.0;
            double centerY = (top + bottom) / 2.0;
            world.setCenterSize(centerX, centerY, totalSize);
        }
    }
}
