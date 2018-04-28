package org.kangaroo.container.channel;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.kangaroo.common.UtilCollection;
import org.kangaroo.common.domain.AppKey;
import org.kangaroo.common.domain.Mysql;
import org.kangaroo.container.support.http.ConfigClient;
import org.kangaroo.routing.ResourcePool;
import org.kangaroo.zk.Action;
import org.kangaroo.zk.notify.Event;

public class MysqlChannel implements Channel {
    public MysqlChannel() {
    }

    public void process(Event event) throws Exception {
        Action action = event.getAction();
        if (2 == Action.getSupTopic(action.getTopic())) {
            switch(action.getTopic()) {
            case 201:
            case 202:
                this.refreshDataSource(action);
                break;
            case 203:
                List<AppKey> appKeys = JSONObject.parseArray(action.getContent(), AppKey.class);
                Iterator var4 = appKeys.iterator();

                while(var4.hasNext()) {
                    AppKey item = (AppKey)var4.next();
                    ResourcePool.getKangarooRoutingDataSource().removeByAppKey(item.getAppKey());
                }

                return;
            case 204:
                this.updateInstanceOnly(JSONObject.parseArray(action.getContent(), String.class));
                break;
            case 205:
                this.removeInstanceOnly(JSONObject.parseArray(action.getContent(), String.class));
                break;
            default:
                throw new IllegalArgumentException("Unknown Topic:" + action.getTopic());
            }
        }

    }

    private void updateInstanceOnly(List<String> mysqlIds) throws Exception {
        if (!UtilCollection.isEmpty(mysqlIds)) {
            List<Mysql> list = ConfigClient.queryMysql(mysqlIds);
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
                Mysql item = (Mysql)var3.next();
                ResourcePool.getKangarooRoutingDataSource().updateDataSource(item);
            }

        }
    }

    private void removeInstanceOnly(List<String> mysqlIds) {
        if (!UtilCollection.isEmpty(mysqlIds)) {
            Iterator var2 = mysqlIds.iterator();

            while(var2.hasNext()) {
                String item = (String)var2.next();
                ResourcePool.getKangarooRoutingDataSource().removeByInstance(item);
            }

        }
    }

    private void refreshDataSource(Action action) throws Exception {
        List<AppKey> appKeys = JSONObject.parseArray(action.getContent(), AppKey.class);
        Set<String> mysqlIds = new HashSet();
        Map<String, List<String>> map = new HashMap();
        Iterator var5 = appKeys.iterator();

        while(var5.hasNext()) {
            AppKey item = (AppKey)var5.next();
            if (null != item.getMysql()) {
                mysqlIds.add(item.getMysql());
                if (map.containsKey(item.getMysql())) {
                    ((List)map.get(item.getMysql())).add(item.getAppKey());
                } else {
                    map.put(item.getMysql(), UtilCollection.singleObjectConvertToList(item.getAppKey()));
                }
            }
        }

        List<Mysql> configs = ConfigClient.queryMysql(new ArrayList(mysqlIds));
        Iterator var12 = configs.iterator();

        while(var12.hasNext()) {
            Mysql item = (Mysql)var12.next();
            List<String> appKeyStr = (List)map.get(item.getId());
            Iterator var9 = appKeyStr.iterator();

            while(var9.hasNext()) {
                String key = (String)var9.next();
                switch(action.getTopic()) {
                case 201:
                    ResourcePool.getKangarooRoutingDataSource().appendDataSource(key, item);
                    break;
                case 202:
                    ResourcePool.getKangarooRoutingDataSource().updateAppKeyResource(key, item);
                }
            }
        }

    }
}
