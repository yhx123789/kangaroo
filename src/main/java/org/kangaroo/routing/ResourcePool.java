package org.kangaroo.routing;

import java.util.Date;
import java.util.List;
import org.kangaroo.common.KangarooConfig;
import org.kangaroo.common.Resource;
import org.kangaroo.common.ResourceInventory;
import org.kangaroo.common.UtilCollection;
import org.kangaroo.common.domain.AppKey;
import org.kangaroo.common.domain.Mysql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourcePool {
    private static final Logger logger = LoggerFactory.getLogger(ResourcePool.class);
    private static ResourcePool pool;
    private static volatile int status = 0;
    private List<AppKey> appKeys;
    private List<Mysql> mysqlResources;
    private KangarooRoutingDataSource kangarooRoutingDataSource;
    private ResourcePool(KangarooConfig config) {
        if (null == config) {
            throw new IllegalArgumentException("config cannot be null");
        } else if (!UtilCollection.isEmpty(config.getAppKeys())) {
            this.appKeys = config.getAppKeys();
            Resource[] var2 = ResourceInventory.list();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Resource item = var2[var4];
                switch(item) {
                case MYSQL:
                    this.mysqlResources = config.getResource(item, Mysql.class);
                    status |= 1;
                    break;
                }
            }

        }
    }

    public static void init(KangarooConfig config) {
        if (0 == status) {
            pool = new ResourcePool(config);
        }

    }

    public static void destroy() {
        if (0 != status) {
            if ((status & 1) > 0) {
                logger.info("Destroy MysqlInstance on {}", new Date());
                if (null != pool.mysqlResources) {
                    pool.mysqlResources.clear();
                }

                if (null != pool.kangarooRoutingDataSource) {
                    pool.kangarooRoutingDataSource.destroy();
                }
            }

            if ((status & 2) > 0) {
                logger.info("Destroy RedisInstance on {}", new Date());
//                if (null != pool.redisResources) {
//                    pool.redisResources.clear();
//                }

//                if (null != pool.kangarooRedisRouting) {
//                    pool.kangarooRedisRouting.destroy();
//                }
            }

            if ((status & 8) > 0) {
                logger.info("Destroy Pair Resource on {}", new Date());
//                if (null != pool.pairResources) {
//                    pool.pairResources.clear();
//                }

//                if (null != pool.pairRoutingData) {
//                    pool.pairRoutingData.destroy();
//                }
            }

            if ((status & 4) > 0) {
                logger.info("Destroy OSS Resource on {}", new Date());
//                if (null != pool.ossResources) {
//                    pool.ossResources.clear();
//                }
//
//                if (null != pool.ossRoutingData) {
//                    pool.ossRoutingData.destroy();
//                }
            }

            if ((status & 16) > 0) {
                logger.info("Destroy Message Queue Resource on {}", new Date());
//                if (null != pool.msgQueueResources) {
//                    pool.msgQueueResources.clear();
//                }
//
//                if (null != pool.messageQueueRoutingData) {
//                    pool.messageQueueRoutingData.destroy();
//                }
            }
        }

        status = 0;
    }

    public static List getResource(Resource resource) {
        switch(resource) {
        case MYSQL:
            return pool.mysqlResources;
//        case REDIS:
//            return pool.redisResources;
//        case MSG_QUEUE:
//            return pool.msgQueueResources;
//        case OSS:
//            return pool.ossResources;
//        case KV_PAIR_TABLE:
//            return pool.pairResources;
        default:
            throw new IllegalArgumentException("Unknown Resource Enum:" + resource);
        }
    }

    public static List<AppKey> getAppKeys() {
        return pool.appKeys;
    }

//    public static PairRoutingData getPairRoutingData() {
//        return pool.pairRoutingData;
//    }
//
//    public static void setPairRoutingData(PairRoutingData pairRoutingData) {
//        pool.pairRoutingData = pairRoutingData;
//    }
//
//    public static OssRoutingData getOssRoutingData() {
//        return pool.ossRoutingData;
//    }
//
//    public static void setOssRoutingData(OssRoutingData ossRoutingData) {
//        pool.ossRoutingData = ossRoutingData;
//    }
//
//    public static MessageQueueRoutingData getMessageQueueRoutingData() {
//        return pool.messageQueueRoutingData;
//    }
//
//    public static void setMessageQueueRoutingData(MessageQueueRoutingData mqRoutingData) {
//        pool.messageQueueRoutingData = mqRoutingData;
//    }

    public static KangarooRoutingDataSource getKangarooRoutingDataSource() {
        return pool.kangarooRoutingDataSource;
    }

    public static void setKangarooRoutingDataSource(KangarooRoutingDataSource kangarooRoutingDataSource) {
        pool.kangarooRoutingDataSource = kangarooRoutingDataSource;
    }

//    public static KangarooRedisRouting getKangarooRedisRouting() {
//        return pool.kangarooRedisRouting;
//    }
//
//    public static void setKangarooRedisRouting(KangarooRedisRouting kangarooRedisRouting) {
//        pool.kangarooRedisRouting = kangarooRedisRouting;
//    }

    private interface Status {
        int UNINITIALIZED = 0;
        int MYSQL_RESOURCE_INITIALIZED = 1;
        int REDIS_RESOURCE_INITIALIZED = 2;
        int OSS_INITIALIZED = 4;
        int PAIR_INITIALIZED = 8;
        int MQ_INITIALIZED = 16;
    }
}
