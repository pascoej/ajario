package me.pascoej.ajario.util;

import java.nio.ByteBuffer;

/**
 * Created by john on 6/14/15.
 */
public class ByteUtil {
    public static String popString(ByteBuffer byteBuffer) {
        StringBuilder sb = new StringBuilder(byteBuffer.limit());
        while (true) {
            short num = byteBuffer.getShort();
            if (num == 0) {
                return sb.toString();
            }
            sb.append((char) num);
        }
    }

    public static String toBinaryString(byte in) {
        return String.format("%8s", Integer.toBinaryString(in & 0xFF)).replace(' ', '0');
    }
}
