//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.routing;

import com.alibaba.druid.pool.DruidPooledConnection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.kangaroo.common.Resource;
import org.kangaroo.common.ThreadContextHolder;
import org.kangaroo.common.UtilCollection;
import org.kangaroo.common.crypt.CipherFactory;
import org.kangaroo.common.domain.AppKey;
import org.kangaroo.common.domain.Mysql;
import org.kangaroo.routing.exception.DataSourceNotFoundException;
import org.kangaroo.routing.exception.DataSourceNotFoundException.Type;
import org.kangaroo.routing.support.KangarooDruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class KangarooRoutingDataSource extends KangarooDruidDataSource {
    private static final Logger logger = LoggerFactory.getLogger(KangarooRoutingDataSource.class);
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    // key- appkey   value- mysqlId
    private Map<String, String> targetSource;
    // kye- mysqlId  value- DataSource
    private Map<String, KangarooDruidDataSource> instances;

    public KangarooRoutingDataSource() {
        this.init();
    }

    public void init() {
        if (initialized.compareAndSet(false, true)) {
        	logger.info("DataSouce start init.");
            ResourcePool.setKangarooRoutingDataSource(this);
            this.targetSource = new HashMap();
            this.instances = new HashMap();
            List<Mysql> list = ResourcePool.getResource(Resource.MYSQL);
            if (!UtilCollection.isEmpty(list)) {
                Map<String, List<String>> map = new HashMap();
                Iterator var3 = ResourcePool.getAppKeys().iterator();

                while(var3.hasNext()) {
                    AppKey item = (AppKey)var3.next();
                    if (map.containsKey(item.getMysql())) {
                        ((List)map.get(item.getMysql())).add(item.getAppKey());
                    } else {
                        map.put(item.getMysql(), UtilCollection.singleObjectConvertToList(item.getAppKey()));
                    }
                }

                var3 = list.iterator();

                while(true) {
                    KangarooDruidDataSource dataSource;
                    Mysql item;
                    do {
                        if (!var3.hasNext()) {
                            logger.info("Mysql - Load Target Source:{}", this.targetSource);
                            return;
                        }

                        item = (Mysql)var3.next();
                        dataSource = this.buildDataSource(item);
                    } while(!map.containsKey(item.getId()));

                    Iterator var6 = ((List)map.get(item.getId())).iterator();

                    while(var6.hasNext()) {
                        String appKey = (String)var6.next();
                        this.targetSource.put(appKey, item.getId());
                        dataSource.addKey(appKey);
                    }
                }
            }
        }
    }

    public void appendDataSource(String appKey, Mysql config) {
        if (!this.targetSource.containsKey(appKey)) {
            KangarooDruidDataSource dataSource = this.buildDataSource(config);
            dataSource.addKey(appKey);
            this.targetSource.put(appKey, config.getId());
            logger.info("AppKey:{} Use Mysql Instance:{}", appKey, dataSource.getName());
        }
    }

    public void updateAppKeyResource(String appKey, Mysql config) {
        if (this.targetSource.containsKey(appKey)) {
            String oldMysqlId = (String)this.targetSource.get(appKey);
            if (this.instances.containsKey(config.getId())) {
                KangarooDruidDataSource newSource = this.updateDataSource(config);
                if (!oldMysqlId.equals(config.getId())) {
                    this.remove(appKey, oldMysqlId);
                    newSource.addKey(appKey);
                    this.targetSource.put(appKey, config.getId());
                }
            } else {
                this.removeByAppKey(appKey);
                this.appendDataSource(appKey, config);
            }

        }
    }

    public KangarooDruidDataSource updateDataSource(Mysql config) {
        synchronized(this) {
            KangarooDruidDataSource ds = (KangarooDruidDataSource)this.instances.get(config.getId());
            if (null != ds) {
                Set<String> keys = new HashSet(ds.getKeys());
                ds.close();
                this.instances.remove(config.getId());
                KangarooDruidDataSource newSource = this.buildDataSource(config);
                newSource.addKey(keys);
                logger.info("Update Mysql Resource:{}", config);
                return newSource;
            } else {
                logger.info("Not Found Target Mysql Resource:{}.Discard Update.", config.getId());
                return null;
            }
        }
    }

    public void removeByAppKey(String appKey) {
        String mysqlId = (String)this.targetSource.remove(appKey);
        this.remove(appKey, mysqlId);
        logger.info("Remove AppKey:{},MysqlSource Instance-Id:{}", appKey, mysqlId);
    }

    public void removeByInstance(String mysqlId) {
        synchronized(this) {
            KangarooDruidDataSource remove = (KangarooDruidDataSource)this.instances.remove(mysqlId);
            if (null != remove) {
                remove.close();
                Iterator var4 = remove.getKeys().iterator();

                while(var4.hasNext()) {
                    String appKey = (String)var4.next();
                    this.targetSource.remove(appKey);
                }

                logger.info("Destroy Mysql Instance:{},Remove AppKey:{}", remove.getName(), remove.getKeys());
            }

        }
    }

    private void remove(String appKey, String mysqlId) {
        KangarooDruidDataSource source = (KangarooDruidDataSource)this.instances.get(mysqlId);
        if (null != source) {
            source.removeKey(appKey);
            if (!source.hasKey()) {
                logger.info("Destroy Mysql Instance:{}", source.getName());
                source.close();
                this.instances.remove(mysqlId);
            }
        }

    }

    protected String determineCurrentLookupKey() {
        return ThreadContextHolder.get();
    }

    public DruidPooledConnection getConnection() throws SQLException {
        return this.determineTargetDataSource().getConnection();
    }

    protected KangarooDruidDataSource determineTargetDataSource() {
        Assert.notNull(this.targetSource, "DataSource router not initialized");
        String key = this.determineCurrentLookupKey();
        String mysqlId = (String)this.targetSource.get(key);
        if (null == mysqlId) {
            throw new DataSourceNotFoundException(Type.SQL, key);
        } else {
            KangarooDruidDataSource datasource = (KangarooDruidDataSource)this.instances.get(mysqlId);
            if (null == datasource) {
                throw new DataSourceNotFoundException("Not Found MySql Resource Instance:" + mysqlId);
            } else {
                return datasource;
            }
        }
    }

    public void destroy() {
        if (initialized.compareAndSet(true, false)) {
            if (null != this.instances) {
                Iterator var1 = this.instances.values().iterator();

                while(var1.hasNext()) {
                    KangarooDruidDataSource item = (KangarooDruidDataSource)var1.next();
                    item.close();
                }

                this.instances.clear();
            }
            this.targetSource.clear();
        }

    }

    private KangarooDruidDataSource buildDataSource(Mysql config) {
        if (this.instances.containsKey(config.getId())) {
            return (KangarooDruidDataSource)this.instances.get(config.getId());
        } else {
            KangarooDruidDataSource ds = new KangarooDruidDataSource();
            ds.setName(config.getInstance() + ":" + config.getSchema());
            ds.setDriverClassName("com.mysql.jdbc.Driver");
            ds.setUrl("jdbc:mysql://" + config.getInstance() + ":3306/" + config.getSchema() + "?useUnicode=true&amp;characterEncoding=UTF-8&allowMultiQueries=true");
            String u = CipherFactory.decryptByPrivateK(config.getUsername());
            String pw = CipherFactory.decryptByPrivateK(config.getPassword());
            ds.setUsername(u);
            ds.setPassword(pw);
            ds.setInitialSize(config.getInitialSize());
            ds.setMaxActive(config.getMaxActive());
            ds.setMinIdle(config.getMinIdle());
            ds.setMaxWait((long)config.getMaxWait());
            ds.setRemoveAbandoned(config.getRemoveAbandoned() == 1);
            ds.setRemoveAbandonedTimeout(config.getRemoveAbandonedTimeout());
            ds.setTimeBetweenEvictionRunsMillis((long)config.getTimeBetweenEvictionRunsMillis());
            ds.setMinEvictableIdleTimeMillis((long)config.getMinEvictableIdleTimeMillis());
            ds.setValidationQuery("select 1");
            ds.setTestWhileIdle(config.getTestWhileIdle() == 1);
            ds.setTestOnBorrow(config.getTestOnBorrow() == 1);
            ds.setTestOnReturn(config.getTestOnReturn() == 1);
            ds.setPoolPreparedStatements(config.getPoolPs() == 1);
            ds.setMaxPoolPreparedStatementPerConnectionSize(config.getMaxPoolPsConnectionSize());
            try {
                ds.setFilters(config.getFilters());
            } catch (SQLException var6) {
                var6.printStackTrace();
            }
            logger.info("xxxxx   url={}",ds.getUrl());
            logger.info("xxxxx   Load DruidDataSource:{},password:{}", ds.getName(),ds.getPassword());
            this.instances.put(config.getId(), ds);
            return ds;
        }
    }
}
