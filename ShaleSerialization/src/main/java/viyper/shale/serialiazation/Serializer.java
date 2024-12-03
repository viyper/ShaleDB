package viyper.shale.serialiazation;

import java.util.List;
import java.util.OptionalInt;

public interface Serializer {
    record Dependency(SerializerDependencyLink linkType, Class<?> clazz) {
    }

    OptionalInt serializerPriority(Class<?> clazz);

    Dependency[] dependencies(Class<?> clazz);

    //OptionalInt sizeOf(Class<?> clazz, SerializerGraph.PathNode[] dependencies);

    //List<Byte> serialize(Object object, SerializerGraph.PathNode[] dependencies);

    //Object deserialize(Class<?> clazz, List<Byte> bytes, SerializerGraph.PathNode[] dependencies);
}
