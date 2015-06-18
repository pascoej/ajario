package me.pascoej.ajario.util;

import me.pascoej.ajario.packet.PacketType;
import me.pascoej.ajario.packet.serverbound.SetNickname;
import me.pascoej.ajario.protocol.WebSocketHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by john on 6/15/15.
 */
public class LagScore {
    public static double lagScore(URI uri) {
        double lagScore = 0;
        final long[] updatePackets = {0};
        final long[] firstUpdatePacket = {-1};
        final long sample = 10000;
        WebSocketHandler webSocketHandler = new WebSocketHandler(uri,null);
        CompletableFuture<Double> updateSecondFuture = new CompletableFuture<>();
        webSocketHandler.registerPacketListener(agarPacket -> {
            long currentTime = System.currentTimeMillis();
            if (agarPacket.getType() == PacketType.ClientBound.UPDATE_NODES) {
                updatePackets[0] += 1;
                if (firstUpdatePacket[0] == -1) {
                    firstUpdatePacket[0] = currentTime;
                } else {
                    long elapsed = currentTime - firstUpdatePacket[0];
                    if (elapsed > sample) {
                        double updatesSecond = (double)updatePackets[0] / (elapsed / 1000.0);
                        updateSecondFuture.complete(updatesSecond);
                        webSocketHandler.close();
                    }
                }
            }
        });
        webSocketHandler.sendPacket(new SetNickname("facebook"));
        double updateSecond = 10;
        try {
            updateSecond = updateSecondFuture.get(20000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
           //e.printStackTrace();
        } catch (ExecutionException e) {
            //e.printStackTrace();
        } catch (TimeoutException e) {
            //e.printStackTrace();
        }
        double avgPing = 0;
        try {
            int times = 15;
            double total = 0;
            for (int i = 0; i < times; i++) {
                long before = System.currentTimeMillis();
                InetAddress.getByName(uri.getHost()).isReachable(300);
                total += System.currentTimeMillis() - before;
            }
            avgPing = total/times;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (avgPing <= 90) {
            lagScore += avgPing;
        }else {
            lagScore += Math.pow(avgPing,1.2);
        }
        lagScore -= Math.pow(updateSecond,1.6);
        return  lagScore;
    }
}
