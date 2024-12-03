package viyper.shale.serialiazation;

public class CannotSerializeException extends RuntimeException {
    public CannotSerializeException(Class<?> clazz) {
        super("This serializer cannot serialize " + clazz.getCanonicalName());
    }
}
