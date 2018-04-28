package org.kangaroo.container.channel;

public class ChannelProcessException extends RuntimeException {
    public ChannelProcessException() {
    }

    public ChannelProcessException(String message) {
        super(message);
    }

    public ChannelProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelProcessException(Throwable cause) {
        super(cause);
    }
}
