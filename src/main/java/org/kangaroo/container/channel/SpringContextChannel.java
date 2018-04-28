package org.kangaroo.container.channel;

import org.kangaroo.common.KangarooConfig;
import org.kangaroo.container.AbstractContainer;
import org.kangaroo.container.support.http.ConfigClient;
import org.kangaroo.container.support.http.LoadConfigException;
import org.kangaroo.zk.Action;
import org.kangaroo.zk.notify.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringContextChannel implements Channel {
    private static final Logger logger = LoggerFactory.getLogger(SpringContextChannel.class);
    private AbstractContainer container;

    public SpringContextChannel(AbstractContainer container) {
        this.container = container;
    }

    public void process(Event event) throws ChannelProcessException {
        Action action = event.getAction();
        if (4 == Action.getSupTopic(action.getTopic())) {
            switch(action.getTopic()) {
            case 402:
                try {
                    KangarooConfig config = ConfigClient.queryRemoteConfig();
                    if (null != this.container) {
                        this.container.refreshContext(config);
                    }
                } catch (LoadConfigException var4) {
                    logger.error("update spring context failed:{}", var4);
                    throw new ChannelProcessException(var4);
                }
            }
        }

    }
}
