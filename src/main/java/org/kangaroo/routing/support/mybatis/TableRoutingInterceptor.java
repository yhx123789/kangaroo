//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.routing.support.mybatis;

import java.sql.Connection;
import java.util.Properties;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.kangaroo.routing.support.TableRouting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({@Signature(
    type = StatementHandler.class,
    method = "prepare",
    args = {Connection.class}
)})
public class TableRoutingInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(TableRoutingInterceptor.class);
    private static final ObjectFactory defaultObjectFactory = new DefaultObjectFactory();
    private static final ObjectWrapperFactory defaultObjectWrapperFactory = new DefaultObjectWrapperFactory();
    private TableRouting tableRouting;

    public TableRoutingInterceptor() {
    }

    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler target = (StatementHandler)invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(target, defaultObjectFactory, defaultObjectWrapperFactory);
        metaObject.setValue("delegate.boundSql.sql", this.routingTable(target.getBoundSql()));
        return invocation.proceed();
    }

    private String routingTable(BoundSql oldBoundSql) {
        String oldSql = oldBoundSql.getSql();
        String target = this.rebuildSql(oldSql);
        if (logger.isDebugEnabled()) {
            logger.debug("Target SQL:{}", target);
        }

        return target;
    }

    private String rebuildSql(String sql) {
        String[] split = sql.trim().toLowerCase().split("\\s+");
        String var3 = split[0];
        byte var4 = -1;
        switch(var3.hashCode()) {
        case -1335458389:
            if (var3.equals("delete")) {
                var4 = 3;
            }
            break;
        case -1183792455:
            if (var3.equals("insert")) {
                var4 = 0;
            }
            break;
        case -906021636:
            if (var3.equals("select")) {
                var4 = 2;
            }
            break;
        case -838846263:
            if (var3.equals("update")) {
                var4 = 1;
            }
        }

        int i;
        switch(var4) {
        case 0:
            split[2] = this.tableRouting.determineTable(split[2]);
            break;
        case 1:
            split[1] = this.tableRouting.determineTable(split[1]);
            break;
        case 2:
            boolean tag = false;

            for(i = 1; i < split.length; ++i) {
                if ("from".equals(split[i])) {
                    tag = true;
                    split[i + 1] = this.tableRouting.determineTable(split[i + 1]);
                    break;
                }
            }

            if (!tag) {
                throw new IllegalArgumentException("Incorrect SQL:" + sql);
            }
            break;
        case 3:
            split[1] = this.tableRouting.determineTable(split[1]);
            break;
        default:
            throw new IllegalArgumentException("Unsupported SQL type :" + split[0]);
        }

        StringBuilder sb = new StringBuilder();
        String[] var9 = split;
        int var10 = split.length;

        for(i = 0; i < var10; ++i) {
            String item = var9[i];
            sb.append(item).append(" ");
        }

        return sb.toString();
    }

    public Object plugin(Object target) {
        return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }

    public void setProperties(Properties properties) {
    }

    public TableRouting getTableRouting() {
        return this.tableRouting;
    }

    public void setTableRouting(TableRouting tableRouting) {
        this.tableRouting = tableRouting;
    }
}
