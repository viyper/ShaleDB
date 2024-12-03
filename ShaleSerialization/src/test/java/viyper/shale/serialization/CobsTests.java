package viyper.shale.serialization;

import org.junit.Assert;
import org.junit.Test;
import viyper.shale.serialiazation.Cobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CobsTests {
    @Test
    public void testCobsRandom() {
        Random random = new Random(31731009);

        for (int i = 0; i < 2500; i++) {
            int len = random.nextInt(5000, 10000);

            List<Byte> bytes = random.ints(len).mapToObj(x -> (byte) x).toList();
            List<Byte> encoded = Cobs.encode(bytes);
            List<Byte> decoded = Cobs.decode(encoded);

            Assert.assertEquals(bytes, decoded);
            Assert.assertEquals(1, encoded.stream().filter(x -> x == 0).count());
        }
    }

    @Test
    public void testCobsLongNonZero() {
        Random random = new Random(762978756);

        for (int i = 0; i < 2500; i++) {
            List<Byte> bytes = new ArrayList<>();

            for (int j = 0; j < 4; j++) {
                bytes.addAll(random.ints(random.nextInt(400, 1000))
                            .filter(x -> x != 0).mapToObj(x -> (byte) x).toList());
                bytes.add((byte)0);
            }

            List<Byte> encoded = Cobs.encode(bytes);
            List<Byte> decoded = Cobs.decode(encoded);

            Assert.assertEquals(bytes, decoded);
            Assert.assertEquals(1, encoded.stream().filter(x -> x == 0).count());
        }
    }
}
