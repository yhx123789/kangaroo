package org.kangaroo.container;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.text.html.HTML;

import org.albert.common.domain.Page;
import org.albert.domain.dictionary.core.domain.OperLog;
import org.albert.domain.logistics.facade.HellTest;
import org.kangaroo.common.AppContext;
import org.kangaroo.common.KangarooConfig;
import org.kangaroo.common.crypt.CipherFactory;
import org.kangaroo.container.channel.ChannelEnum;
import org.kangaroo.container.channel.ChannelHolder;
import org.kangaroo.container.channel.SpringContextChannel;
import org.kangaroo.container.support.http.ConfigClient;
import org.kangaroo.routing.ResourcePool;
import org.kangaroo.zk.Node;
import org.kangaroo.zk.notify.Event;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import com.sun.tools.javac.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyContainer extends AbstractContainer {
	private static final Logger logger = LoggerFactory.getLogger(AbstractContainer.class);
	private static ClassPathXmlApplicationContext context;
	private static volatile boolean started = false;
	private static volatile boolean running = true;
	private static Node local;
	// private ZookeeperRegistry registry;
	private ChannelHolder channelHolder;

	public MyContainer() {
	}

	public static void init() {
		try {
			if (started) {
				return;
			}

			String systemEnv = AppContext.getString("system.env");
			String host = AppContext.getString("host");
			if (null == systemEnv || null == host) {
				throw new IllegalArgumentException("[system.env],[host] cannot be null in kangaroo.cfg");
			}

			ConfigClient.init(systemEnv, host);
			CipherFactory.setPRIVATE_KEY(ConfigClient.queryPrivateKey());
			CipherFactory.init();
			KangarooConfig remoteConfig = ConfigClient.queryRemoteConfig();

			logger.info("Load Remote Config:{}", remoteConfig);
			System.setProperty("running.environment", remoteConfig.getEnv());
			System.setProperty("identity", systemEnv);
			System.setProperty("kangaroo.node.path",
					remoteConfig.getNs() + "/" + remoteConfig.getSystem() + "/" + remoteConfig.getEnv());
			final MyContainer myContainer = new MyContainer();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						myContainer.stop();
						MyContainer.logger.info(myContainer.getClass().getSimpleName() + " stopped!");
					} catch (Throwable var4) {
						MyContainer.logger.error(var4.getMessage(), var4);
					}

					Class var1 = MyContainer.class;
					synchronized (MyContainer.class) {
						MyContainer.running = false;
						MyContainer.class.notify();
					}
				}
			});
			myContainer.start(remoteConfig);
			// CuratorZkClient.APP_INITIALIZED = true;
		} catch (Exception var4) {
			System.err.println(var4);
			logger.error(var4.getMessage(), var4);
			System.exit(1);
		}

		started = true;
	}

	public static void main(String[] args) {
		String cfgPath = "/home/zt/test/kangaroo.cfg";
		System.out.println("main start");
//		String cfgPath = "F:\\kangaroo.cfg";
		if (null == cfgPath) {
			System.err.println("Need config path");
		}
		AppContext.init(cfgPath);
		init();

		doTest();

		synchronized (MyContainer.class) {
			while (running) {
				try {
					System.out.println("要睡觉了");
					MyContainer.class.wait();
				} catch (Throwable var5) {
					var5.printStackTrace();
				}
			}
			System.out.println("结束");
		}
	}

	public static void doTest() {
		logger.info("开始执行我的测试");
		String traceId = "SP_BETA_CSC&dictionary.queryOperLog&5245751";
		HellTest hellTest = (HellTest) MyContainer.getContext().getBean("hellTest");
		hellTest.say("yhx", traceId);
		Map<String, Object> params = new HashMap<String, Object>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startStr = dateFormat.format(new Date(Long.valueOf("1514736000")));
		String stopStr = dateFormat.format(new Date(Long.valueOf("1527782400")));
		params.put("startDate", startStr);
		params.put("endDate", stopStr);
		params.put("personName", null);
		params.put("order", "desc");
		OperLog operLog = (OperLog) MyContainer.getContext().getBean("operLog");
		Page<OperLog> oplogs = operLog.selecOperLogpage(1, 3, params);
		List<OperLog> opLogList =  (List<OperLog>) oplogs.getData();
		for(OperLog log:opLogList){
			logger.info("log = {}" , log.toString());
		}
	}

	public static ClassPathXmlApplicationContext getContext() {
		return context;
	}

	public void start(KangarooConfig initParams) {
		local = Node.createServiceNode(initParams.getZookeeperAddress(), initParams.getNs(), initParams.getSystem(),
				initParams.getEnv());
		this.channelHolder = ChannelHolder.createDefault(local);
		// this.registry();
		this.refreshContext(initParams);
		this.channelHolder.appendChannel(ChannelEnum.SPRING_CONTEXT_CONTAINER.name(), new SpringContextChannel(this));
	}

	// private void registry() {
	// this.registry = RegistryFactory.init(local);
	// this.registry.registry();
	// this.registry.getZkClient().setData(local.getNodePath(),
	// NodeStatusMsg.INITIALIZE);
	// this.registry.subscribe(local.getParentNodePath(), new
	// ServiceSideNodeListener(this.channelHolder));
	// }

	public void refreshContext(KangarooConfig initParams) {
		if (null != context) {
			context.stop();
			context.close();
			context = null;
			ResourcePool.destroy();
			System.gc();
		}

		Event event = Event.create(1, (String) null, "APP_START:" + ConfigClient.environmentId);
		event.setExtra(initParams);
		this.channelHolder.execute(event);
		context = new ClassPathXmlApplicationContext("classpath:root/beans.xml");
		AppContext.setApplicationContext(getContext());
		context.getEnvironment().getPropertySources()
				.addFirst(new PropertiesPropertySource("localProperties", initParams.getProperties()));
		// context.setConfigLocations("classpath*:/root.xml".split("[,\\s]+"));
		context.refresh();
		context.start();
		System.out.println(
				(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]")).format(new Date()) + "Kangaroo Container started!");
	}

	public void stop() {
		try {
			if (null != this.channelHolder) {
				this.channelHolder.clear();
			}

			// if (null != this.registry) {
			// this.registry.destroy();
			// }

			ResourcePool.destroy();
			if (null != context) {
				context.stop();
				context.close();
				context = null;
			}
		} catch (Throwable var2) {
			logger.error(var2.getMessage(), var2);
		}

	}
}
