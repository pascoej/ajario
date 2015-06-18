package me.pascoej.ajario.node;

/**
 * Created by john on 6/14/15.
 */
public class PlayerNode extends Node {
    private String name;
    private boolean mine;

    public PlayerNode(int nodeId) {
        super(nodeId, NodeType.PLAYER);
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine() {
        this.mine = true;
    }

    public void setName(String name) {
        this.name = name;
    }
}
