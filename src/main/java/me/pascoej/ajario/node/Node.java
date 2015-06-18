package me.pascoej.ajario.node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 6/14/15.
 */
public class Node {
    private final int nodeId;
    private final NodeType nodeType;
    private long lastUpdate;
    private short x, y, size;
    double vX, vY;

    Node(int nodeId, NodeType nodeType) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
    }

    List<Double> errors = new ArrayList<>();

    public void updatePositionSize(short x, short y, short size) {
        long currentTime = System.currentTimeMillis();
        this.x = x;
        this.y = y;
        this.size = size;
        lastUpdate = currentTime;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public int getNodeId() {
        return nodeId;
    }

    public double getLastUpdate() {
        return lastUpdate;
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

    public double distanceSquared(Node node1) {
        double xDiff = node1.getX() - this.getX();
        double yDiff = node1.getY() - this.getY();
        return (xDiff * xDiff) + (yDiff * yDiff);
    }
}
