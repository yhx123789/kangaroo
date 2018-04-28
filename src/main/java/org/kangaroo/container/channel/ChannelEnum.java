package org.kangaroo.container.channel;

public enum ChannelEnum {
    RESOURCE_POOL,
    ROUTING_DATA_SOURCE,
    ROUTING_REDIS_SOURCE,
    SPRING_CONTEXT_CONTAINER,
    MESSAGE_QUEUE_SOURCE,
    OSS_SOURCE,
    PAIR_SOURCE;

    private ChannelEnum() {
    }
}
