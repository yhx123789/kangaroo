package org.kangaroo.routing.exception;

public class DataSourceNotFoundException extends RuntimeException {
    public DataSourceNotFoundException() {
    }

    public DataSourceNotFoundException(DataSourceNotFoundException.Type type, String key) {
        super("Cannot determine " + type + " Resource for lookup key [" + key + "]");
    }

    public DataSourceNotFoundException(String message) {
        super(message);
    }

    public DataSourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public static enum Type {
        REDIS,
        SQL,
        MESSAGE_QUEUE,
        OSS,
        PAIR;

        private Type() {
        }
    }
}
