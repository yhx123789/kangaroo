//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.common.domain;

public class Mysql extends Base {
    private String group;
    private String name;
    private String instance;
    private String schema;
    private String username;
    private String password;
    private int initialSize = 1;
    private int maxActive = 8;
    private int minIdle = 0;
    private int maxWait = 5000;
    private int removeAbandoned = 0;
    private int removeAbandonedTimeout = 120;
    private int timeBetweenEvictionRunsMillis = 60000;
    private int minEvictableIdleTimeMillis = 40000;
    private int testWhileIdle = 1;
    private int testOnBorrow = 0;
    private int testOnReturn = 0;
    private int poolPs = 1;
    private int maxPoolPsConnectionSize = 10;
    private String filters = "stat";

    public Mysql() {
    }

    public String getUniqueName() {
        return this.instance + ":" + this.schema;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstance() {
        return this.instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getInitialSize() {
        return this.initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxActive() {
        return this.maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return this.minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxWait() {
        return this.maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getRemoveAbandoned() {
        return this.removeAbandoned;
    }

    public void setRemoveAbandoned(int removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public int getRemoveAbandonedTimeout() {
        return this.removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public int getTestWhileIdle() {
        return this.testWhileIdle;
    }

    public void setTestWhileIdle(int testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public int getTestOnBorrow() {
        return this.testOnBorrow;
    }

    public void setTestOnBorrow(int testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public int getTestOnReturn() {
        return this.testOnReturn;
    }

    public void setTestOnReturn(int testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public int getPoolPs() {
        return this.poolPs;
    }

    public void setPoolPs(int poolPs) {
        this.poolPs = poolPs;
    }

    public int getMaxPoolPsConnectionSize() {
        return this.maxPoolPsConnectionSize;
    }

    public void setMaxPoolPsConnectionSize(int maxPoolPsConnectionSize) {
        this.maxPoolPsConnectionSize = maxPoolPsConnectionSize;
    }

    public String getFilters() {
        return this.filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }
}
