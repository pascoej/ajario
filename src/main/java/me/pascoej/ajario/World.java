package me.pascoej.ajario;

import me.pascoej.ajario.node.Node;
import me.pascoej.ajario.node.PlayerNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by john on 6/14/15.
 */
public class World {
    private static final double WORLD_SIZE = 11180.339887498949;
    private final Map<Integer, Node> nodeById = new HashMap<>();
    private final List<Node> nodes = new CopyOnWriteArrayList<>();
    private final List<Node> clientNodes = new CopyOnWriteArrayList<>();
    private double centerX = WORLD_SIZE / 2;
    private double centerY = WORLD_SIZE / 2;
    private double totalSize = 0;
    private double scale = 1;

    public synchronized void addNode(Node node) {
        int id = node.getNodeId();
        if (nodeById.containsKey(id)) {
            removeNode(id);
        }
        nodeById.put(node.getNodeId(), node);
        nodes.add(node);
    }

    public void addClientsNode(Node node) {
        addNode(node);
        clientNodes.add(node);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Node> getClientNodes() {
        return clientNodes;
    }

    public Node getNode(int id) {
        return nodeById.get(id);
    }

    public void removeNode(int id) {
        Node node = nodeById.get(id);
        removeNode(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
        nodeById.remove(node.getNodeId());
        if (clientNodes.contains(node)) {
            clientNodes.remove(node);
        }
    }

    public boolean isSplit(PlayerNode node) {
        if (node == null) {
            return false;
        }
        if (node.isMine()) {
            return clientNodes.size() > 1;
        }
        for (Node node1 : nodes) {
            if (node1 == null) {
                continue;
            }
            if (node.getNodeId() != node1.getNodeId() && node1 instanceof PlayerNode && ((PlayerNode) node1).getName() != null && ((PlayerNode) node1).getName().equals(node.getName()) && node.distanceSquared(node1) < 750000) {
                return true;
            }
        }
        return false;
    }

    public void clearAll() {
        nodes.clear();
        nodeById.clear();
        clientNodes.clear();
    }

    public void setCenterSize(double x, double y, double size) {
        this.centerX = x;
        this.centerY = y;
        this.totalSize = size;
        this.scale = Math.pow(Math.min(1.0, 64.0 / totalSize), 0.4);
    }

    public static double getWORLD_SIZE() {
        return WORLD_SIZE;
    }

    public Map<Integer, Node> getNodeById() {
        return nodeById;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getTotalSize() {
        return totalSize;
    }

    public double getScale() {
        return scale;
    }
}
