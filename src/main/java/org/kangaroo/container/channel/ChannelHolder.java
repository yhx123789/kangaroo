//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.container.channel;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.kangaroo.common.notify.NotifyCallback;
import org.kangaroo.container.support.http.ConfigClient;
import org.kangaroo.container.support.http.LoadConfigException;
import org.kangaroo.zk.Node;
import org.kangaroo.zk.notify.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelHolder {
    private static final Logger logger = LoggerFactory.getLogger(ChannelHolder.class);
    private Map<String, Channel> channels = new HashMap();
    private Node localNode;

    public ChannelHolder() {
    }

    public static ChannelHolder createDefault(Node node) {
        ChannelHolder holder = new ChannelHolder();
        holder.setLocalNode(node);
        holder.appendChannel(ChannelEnum.RESOURCE_POOL.name(), new ResourcePoolChannel());
        holder.appendChannel(ChannelEnum.ROUTING_DATA_SOURCE.name(), new MysqlChannel());
//        holder.appendChannel(ChannelEnum.ROUTING_REDIS_SOURCE.name(), new RedisChannel());
//        holder.appendChannel(ChannelEnum.MESSAGE_QUEUE_SOURCE.name(), new MsgQueueChannel());
//        holder.appendChannel(ChannelEnum.OSS_SOURCE.name(), new OssChannel());
//        holder.appendChannel(ChannelEnum.PAIR_SOURCE.name(), new PairChannel());
        return holder;
    }

    public void execute(Event event) {
        if (0 != this.channels.size()) {
            try {
                Iterator var6 = this.channels.values().iterator();

                while(var6.hasNext()) {
                    Channel entry = (Channel)var6.next();
                    entry.process(event);
                }

                ConfigClient.callback(this.create(event.getAction().getMsgId(), "SUCCESS", "SUCCESS"));
            } catch (Exception var5) {
                Exception e = var5;
                logger.error("Channel Process Error:{}", var5);

                try {
                    ConfigClient.callback(this.create(event.getAction().getMsgId(), "FAILED", e.getMessage()));
                } catch (LoadConfigException var4) {
                    throw new IllegalStateException(var5);
                }
            }

        }
    }

    private NotifyCallback create(String logId, String result, String comment) {
        NotifyCallback callback = new NotifyCallback();
        callback.setLogId(logId);
        callback.setNode(this.localNode.getNodeName());
        callback.setResult(result);
        callback.setTime(new Date());
        callback.setComment(comment);
        return callback;
    }

    public void appendChannel(String name, Channel channel) {
        this.channels.put(name, channel);
    }

    public Channel remove(String name) {
        return (Channel)this.channels.remove(name);
    }

    public Channel get(String name) {
        return (Channel)this.channels.get(name);
    }

    public void clear() {
        this.channels.clear();
    }

    public void setLocalNode(Node localNode) {
        this.localNode = localNode;
    }
}
