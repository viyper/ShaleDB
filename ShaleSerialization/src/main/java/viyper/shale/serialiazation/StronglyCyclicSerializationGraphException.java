package viyper.shale.serialiazation;

public class StronglyCyclicSerializationGraphException extends RuntimeException {
    public StronglyCyclicSerializationGraphException(String message) {
        super(message);
    }
}
