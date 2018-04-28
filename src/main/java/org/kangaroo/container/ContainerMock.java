//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.container;

import org.kangaroo.common.AppContext;
import org.kangaroo.common.UtilCollection;

public class ContainerMock {
    public ContainerMock() {
    }

    public void start(String envId, String host, String zkAddress) {
        if (!UtilCollection.isEmpty(envId) && !UtilCollection.isEmpty(host) && !UtilCollection.isEmpty(zkAddress)) {
            AppContext.put("system.env", envId);
            AppContext.put("host", host);
            AppContext.put("zookeeper.address", zkAddress);
            MyContainer.init();
        } else {
            throw new IllegalArgumentException("Expected 3 arguments:system.env,host,zookeeper.address");
        }
    }

    public Object getBean(String name) {
        return MyContainer.getContext().getBean(name);
    }

    public Object getBean(Class<?> requiredType) {
        return MyContainer.getContext().getBean(requiredType);
    }
}
