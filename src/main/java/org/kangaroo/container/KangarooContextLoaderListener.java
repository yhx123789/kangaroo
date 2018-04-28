package org.kangaroo.container;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.kangaroo.common.AppContext;
import org.kangaroo.common.KangarooConfig;
import org.kangaroo.common.crypt.CipherFactory;
import org.kangaroo.container.channel.ChannelEnum;
import org.kangaroo.container.channel.ChannelHolder;
import org.kangaroo.container.channel.SpringContextChannel;
import org.kangaroo.container.support.http.ConfigClient;
//import org.kangaroo.container.support.zk.NodeStatusMsg;
//import org.kangaroo.container.support.zk.ServiceSideNodeListener;
import org.kangaroo.routing.ResourcePool;
import org.kangaroo.zk.Node;
//import org.kangaroo.zk.curator.CuratorZkClient;
import org.kangaroo.zk.notify.Event;
//import org.kangaroo.zk.registry.RegistryFactory;
//import org.kangaroo.zk.registry.ZookeeperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class KangarooContextLoaderListener extends AbstractContainer implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(KangarooContextLoaderListener.class);
    private ChannelHolder channelHolder;
    private ContextLoader contextLoader;
//    private ZookeeperRegistry registry;
    private ServletContext sc;

    public KangarooContextLoaderListener() {
    }

    public void contextInitialized(ServletContextEvent event) {
        this.sc = event.getServletContext();
        String systemEnv = this.sc.getInitParameter("systemEnv");
        String configServer = this.sc.getInitParameter("configServer");

        try {
            ConfigClient.init(systemEnv, configServer);
            CipherFactory.setPRIVATE_KEY(ConfigClient.queryPrivateKey());
            CipherFactory.init();
            KangarooConfig remoteConfig = ConfigClient.queryRemoteConfig();
            System.setProperty("running.environment", remoteConfig.getEnv());
            System.setProperty("identity", systemEnv);
            System.setProperty("kangaroo.node.path", remoteConfig.getNs() + "/" + remoteConfig.getSystem() + "/" + remoteConfig.getEnv());
            
            Node local = Node.createServiceNode(remoteConfig.getZookeeperAddress(), remoteConfig.getNs(), remoteConfig.getSystem(), remoteConfig.getEnv());
            this.channelHolder = ChannelHolder.createDefault(local);
//            this.registry = RegistryFactory.init(local);
//            this.registry.registry();
//            this.registry.getZkClient().setData(local.getNodePath(), NodeStatusMsg.INITIALIZE);
//            this.registry.subscribe(local.getParentNodePath(), new ServiceSideNodeListener(this.channelHolder));
            this.refreshContext(remoteConfig);
            this.channelHolder.appendChannel(ChannelEnum.SPRING_CONTEXT_CONTAINER.name(), new SpringContextChannel(this));
//            CuratorZkClient.APP_INITIALIZED = true;
        } catch (Exception var6) {
            logger.error("initial error", var6);
        }

    }

    public void refreshContext(KangarooConfig remoteConfig) {
        if (null != this.contextLoader) {
            this.close(this.sc);
            this.contextLoader = null;
            ResourcePool.destroy();
            System.gc();
        }

        Event myEvent = Event.create(1, (String)null, "APP_START:" + ConfigClient.environmentId);
        myEvent.setExtra(remoteConfig);
        this.channelHolder.execute(myEvent);
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        AppContext.setApplicationContext(context);
        context.getEnvironment().getPropertySources().addFirst(new PropertiesPropertySource("localProperties", remoteConfig.getProperties()));
        logger.info("Loaded Remote config :{}", remoteConfig);
        this.contextLoader = new ContextLoader(context);
        this.contextLoader.initWebApplicationContext(this.sc);
        System.out.println((new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]")).format(new Date()) + "Kangaroo Container started!");
    }

    public void contextDestroyed(ServletContextEvent event) {
        if (null != this.channelHolder) {
            this.channelHolder.clear();
        }

//        if (null != this.registry) {
//            this.registry.destroy();
//        }

        ResourcePool.destroy();
        this.close(event.getServletContext());
    }

    private void close(ServletContext sc) {
        this.contextLoader.closeWebApplicationContext(sc);
        Enumeration attrNames = sc.getAttributeNames();

        while(attrNames.hasMoreElements()) {
            String attrName = (String)attrNames.nextElement();
            if (attrName.startsWith("org.springframework.")) {
                Object attrValue = sc.getAttribute(attrName);
                if (attrValue instanceof DisposableBean) {
                    try {
                        ((DisposableBean)attrValue).destroy();
                    } catch (Throwable var6) {
                        logger.error("Couldn't invoke destroy method of attribute with name '" + attrName + "'", var6);
                    }
                }
            }
        }

    }
}
