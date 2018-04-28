package org.kangaroo.container.channel;

import org.kangaroo.zk.notify.Event;

public interface Channel {
    void process(Event var1) throws Exception;
}
