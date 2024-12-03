package viyper.shale.serialiazation;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<Byte> byteArrayToList(byte[] bytes) {
        ArrayList<Byte> list = new ArrayList<>(bytes.length);

        for (byte b : bytes) {
            list.add(b);
        }

        return list;
    }

    public static byte[] byteListToArray(List<Byte> list) {
        byte[] bytes = new byte[list.size()];

        for (int i = 0; i < list.size(); i++) {
            bytes[i] = list.get(i);
        }

        return bytes;
    }
}
