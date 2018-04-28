package org.kangaroo.routing.support;

import com.alibaba.druid.pool.DruidDataSource;
import java.util.HashSet;
import java.util.Set;

public class KangarooDruidDataSource extends DruidDataSource {
    private Set<String> keys;

    public KangarooDruidDataSource() {
    }

    public boolean hasKey() {
        return null != this.keys && !this.keys.isEmpty();
    }

    public boolean removeKey(String key) {
        return this.keys.remove(key);
    }

    public void addKey(String key) {
        if (null == this.keys) {
            this.keys = new HashSet();
        }

        this.keys.add(key);
    }

    public void addKey(Set<String> keys) {
        if (null == this.keys) {
            this.keys = new HashSet();
        }

        this.keys.addAll(keys);
    }

    public Set<String> getKeys() {
        return this.keys;
    }

    public void close() {
        if (null != this.keys) {
            this.keys.clear();
        }

        super.close();
    }
}
