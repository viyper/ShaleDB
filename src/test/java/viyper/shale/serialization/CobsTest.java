package viyper.shale.serialization;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Random;

import static viyper.shale.serialiazation.Cobs.*;

public class CobsTest {
    @Test
    public void testCobs() {
        Random r = new Random(12345);

        for (int i = 0; i < 10000; i++) {
            byte[] bytes = new byte[r.nextInt(1024, 5192)];
            r.nextBytes(bytes);

            ByteBuffer in = ByteBuffer.wrap(bytes);
            ByteBuffer encoded = ByteBuffer.allocate(maxEncodedSize(in.remaining()));
            ByteBuffer decoded = ByteBuffer.allocate(in.remaining());

            encode(in, encoded);
            encoded.rewind();
            decode(encoded, decoded);

            Assert.assertArrayEquals(bytes, decoded.array());

            encoded.rewind();
            while (encoded.hasRemaining()) {
                if (encoded.get() == 0x00) {
                    while (encoded.hasRemaining()) {
                        Assert.assertEquals(0x00, encoded.get());
                    }
                    break;
                }
            }

            Assert.assertEquals(0x00, encoded.get(encoded.limit() - 1));
        }
    }
}
