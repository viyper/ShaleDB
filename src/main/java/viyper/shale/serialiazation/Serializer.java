package viyper.shale.serialiazation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class Serializer {
    public record BaseSerializer(BiConsumer<?, ByteBuffer> serializer, BiConsumer<ByteBuffer, ?> deserializer, int size) {}
    private record AutoSerializationInfo(Field[] fields, Constructor<?> constructor, int size) {}

    private final ConcurrentHashMap<Class<?>, BaseSerializer> baseSerializers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<?>, AutoSerializationInfo> autoSerializationCache = new ConcurrentHashMap<>();

    public static final Serializer GLOBAL = new Serializer();

    public void serialize(Object object) {
        Class<?> clazz = object.getClass();

        ByteBuffer buffer = ByteBuffer.allocate(0);
        test(buffer);

        System.out.println(Arrays.toString(buffer.array()));
    }

    public void test(ByteBuffer buffer) {

    }

    public static void main(String[] args) {
        Serializer.GLOBAL.serialize(new Object());
    }
}
