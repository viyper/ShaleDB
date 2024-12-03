package viyper.shale.serialiazation;

public class NoApplicableSerializerException extends RuntimeException {
    public NoApplicableSerializerException(Class<?> clazz) {
        super("No applicable serializer exits for " + clazz.getCanonicalName());
    }
}
