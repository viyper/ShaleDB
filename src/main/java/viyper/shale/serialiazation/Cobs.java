package viyper.shale.serialiazation;

import java.nio.ByteBuffer;

public class Cobs {
    public static void encode(ByteBuffer in, ByteBuffer out) {
        int offsetPos = out.position();
        out.get();
        int offset = 1;

        while (in.hasRemaining()) {
            byte next = in.get();

            if (next == 0x00) {
                out.put(offsetPos, (byte) offset);
                offsetPos = out.position();
                out.get();
                offset = 1;
            } else {
                if (offset == 254) {
                    out.put(offsetPos, (byte) (offset + 1));
                    offsetPos = out.position();
                    out.get();
                    offset = 1;

                    in.position(in.position() - 1);
                } else {
                    out.put(next);
                    offset++;
                }
            }
        }

        out.put(offsetPos, (byte) offset);
        out.put((byte) 0x00);
    }

    public static void decode(ByteBuffer in, ByteBuffer out) {
        int offset = Byte.toUnsignedInt(in.get());

        while (true) {
            int length;
            if (offset == 255) length = 253;
            else length = offset - 1;

            out.put(out.position(), in, in.position(), length);
            out.position(out.position() + length);
            in.position(in.position() + length);

            int nextOffset = Byte.toUnsignedInt(in.get());

            if (nextOffset == 0x00) break;

            if (offset != 255) out.put((byte) 0x00);

            offset = nextOffset;
        }
    }

    public static int maxEncodedSize(int rawSize) {
        return 2 + rawSize + rawSize / 253;
    }
}
