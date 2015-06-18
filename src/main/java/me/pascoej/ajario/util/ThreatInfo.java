package me.pascoej.ajario.util;

import me.pascoej.ajario.AgarClient;
import me.pascoej.ajario.node.Node;
import me.pascoej.ajario.node.PlayerNode;

import java.awt.*;

/**
 * Created by john on 6/14/15.
 */
public class ThreatInfo {
    private final ThreatLevel threatLevel;

    public enum ThreatLevel {
        ME(new Color(0x106022)),
        TOO_HUGE(new Color(0x7A028F)),
        SPLIT_KILL_ME(new Color(0xFE3E48)),
        KILL_ME(new Color(0x9D0008)),
        NEUTREL(new Color(0x7d7d7d)),
        CAN_KILL(new Color(0x156880)),
        CAN_SPLIT_KILL(new Color(0x468DA1));
        private final Color color;

        ThreatLevel(Color color) {
            this.color = color;
        }

        public org.newdawn.slick.Color getColor() {
            return new org.newdawn.slick.Color(color.getRGB());
        }
    }

    private ThreatInfo(ThreatLevel threatLevel) {
        this.threatLevel = threatLevel;
    }

    public ThreatLevel getThreatLevel() {
        return threatLevel;
    }

    public static ThreatInfo threatInfo(AgarClient agarClient, PlayerNode node) {
        if (agarClient.isDead()) {
            return new ThreatInfo(ThreatLevel.NEUTREL);
        }
        double myAvgSize = agarClient.getWorld().getClientNodes().stream().mapToDouble(Node::getSize).average().getAsDouble() - 10;
        double myLargestSize = agarClient.getWorld().getClientNodes().stream().mapToDouble(Node::getSize).max().getAsDouble() - 10;
        double nodeSize = node.getSize() - 10;
        ThreatLevel threatLevel = ThreatLevel.NEUTREL;
        boolean isSplit = agarClient.getWorld().isSplit(node);
        boolean amISplit = agarClient.getWorld().getClientNodes().size() > 1;
        if (node.isMine()) {
            threatLevel = ThreatLevel.ME;
        } else if (nodeSize >= 4.7 * myAvgSize) {
            threatLevel = ThreatLevel.TOO_HUGE;
        } else if (nodeSize >= 2.55 * myAvgSize) {
            threatLevel = ThreatLevel.SPLIT_KILL_ME;
        } else if (!isSplit && nodeSize >= 1.09 * myAvgSize) {
            threatLevel = ThreatLevel.KILL_ME;
        } else if (nodeSize > 1.29 * myAvgSize) {
            threatLevel = ThreatLevel.KILL_ME;
        }
        if (2.6 * nodeSize <= myLargestSize) {
            threatLevel = ThreatLevel.CAN_SPLIT_KILL;
        } else if (amISplit && 1.3 * nodeSize <= myLargestSize) {
            threatLevel = ThreatLevel.CAN_KILL;
        } else if (!amISplit && 1.10 * nodeSize <= myLargestSize) {
            threatLevel = ThreatLevel.CAN_KILL;
        }
        return new ThreatInfo(threatLevel);
    }
}
