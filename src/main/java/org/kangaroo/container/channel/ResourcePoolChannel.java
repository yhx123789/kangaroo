package org.kangaroo.container.channel;

import com.alibaba.fastjson.JSONObject;
import java.util.Iterator;
import java.util.List;
import org.kangaroo.common.KangarooConfig;
import org.kangaroo.common.domain.AppKey;
import org.kangaroo.routing.ResourcePool;
import org.kangaroo.zk.Action;
import org.kangaroo.zk.notify.Event;

public class ResourcePoolChannel implements Channel {
    public ResourcePoolChannel() {
    }

    public void process(Event event) throws ChannelProcessException {
        Action action = event.getAction();
        if (action.getTopic() == 1) {
            KangarooConfig config = (KangarooConfig)event.getExtra();
            ResourcePool.init(config);
        } else if (action.getTopic() == 403) {
            List<AppKey> appKeys = JSONObject.parseArray(action.getContent(), AppKey.class);
            Iterator var4 = appKeys.iterator();

            while(var4.hasNext()) {
                AppKey item = (AppKey)var4.next();
                String appKey = item.getAppKey();
                if (null != ResourcePool.getKangarooRoutingDataSource()) {
                    ResourcePool.getKangarooRoutingDataSource().removeByAppKey(appKey);
                }
//                if (null != ResourcePool.getKangarooRedisRouting()) {
//                    ResourcePool.getKangarooRedisRouting().removeByAppKey(appKey);
//                }
//
//                if (null != ResourcePool.getOssRoutingData()) {
//                    ResourcePool.getOssRoutingData().removeByAppKey(appKey);
//                }
//
//                if (null != ResourcePool.getPairRoutingData()) {
//                    ResourcePool.getPairRoutingData().removeDataSource(appKey);
//                }
//
//                if (null != ResourcePool.getMessageQueueRoutingData()) {
//                    ResourcePool.getMessageQueueRoutingData().removeByAppKey(appKey);
//                }
            }
        }

    }
}
