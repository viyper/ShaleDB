package viyper.shale.serialiazation;

import java.util.ArrayList;
import java.util.List;

public class Cobs {
    public static List<Byte> encode(List<Byte> in) {
        ArrayList<Byte> out = new ArrayList<>(2 + in.size() + in.size() / 253);

        int offsetIndex = 0;
        out.add((byte) 0);

        int inIndex = 0;
        while (inIndex < in.size()) {
            byte next = in.get(inIndex++);

            if (next == 0) {
                out.set(offsetIndex, (byte) (out.size() - offsetIndex));
                offsetIndex = out.size();
                out.add((byte) 0);
            } else {
                if (out.size() - offsetIndex < 254) {
                    out.add(next);
                } else {
                    out.set(offsetIndex, (byte) 255);
                    offsetIndex = out.size();
                    out.add((byte) 0);
                    out.add(next);
                }
            }
        }

        out.set(offsetIndex, (byte) (out.size() - offsetIndex));
        out.add((byte) 0);

        out.trimToSize();
        return out;
    }

    public static List<Byte> decode(List<Byte> in) {
        ArrayList<Byte> out = new ArrayList<>(in.size());

        int index = 0;
        int offset = Byte.toUnsignedInt(in.get(index++));

        while (offset != 0) {
            if (offset == 255) {
                int nextIndex = index + offset - 2;
                out.addAll(in.subList(index, nextIndex));
                index = nextIndex;
                offset = Byte.toUnsignedInt(in.get(index++));
            } else {
                int nextIndex = index + offset - 1;
                out.addAll(in.subList(index, nextIndex));
                index = nextIndex;
                offset = Byte.toUnsignedInt(in.get(index++));
                if (offset != 0) out.add((byte) 0);
            }
        }

        out.trimToSize();
        return out;
    }
}
