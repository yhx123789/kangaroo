package org.kangaroo.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.context.ApplicationContext;

public class AppContext {
    public static final String HOST_STR = "host";
    public static final String ENV_STR = "system.env";
    private static final String CONFIG_FILE_NAME = "kangaroo.cfg";
    private static ApplicationContext applicationContext;
    private static Properties config = new Properties();
    private static AtomicBoolean started = new AtomicBoolean(false);

    public AppContext() {
    }

    public static void init(String path) {
        load(path);
    }

    public static Properties getConfig() {
        return config;
    }

    public static void load(String path) {
        if (started.compareAndSet(false, true)) {
            if (null == path || 0 == path.length()) {
                throw new IllegalArgumentException("Not found config file path :" + path);
            }

            String file = path;
            if (!path.endsWith(".cfg")) {
                file = path + (path.endsWith("/") ? "kangaroo.cfg" : "/kangaroo.cfg");
            }

            try {
                config.load(new FileInputStream(file));
            } catch (IOException var3) {
                throw new IllegalStateException(var3.getMessage(), var3);
            }
        }

    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String p = config.getProperty(key);
        return null != p && 0 != p.length() ? Boolean.valueOf(p) : defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        String p = config.getProperty(key);
        return null != p && 0 != p.length() ? Integer.valueOf(p) : defaultValue;
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static long getLong(String key, long defaultValue) {
        String p = config.getProperty(key);
        return null != p && 0 != p.length() ? Long.valueOf(p) : defaultValue;
    }

    public static String getString(String key, String defaultValue) {
        String p = config.getProperty(key);
        return null != p && 0 != p.length() ? p : defaultValue;
    }

    public static String getString(String key) {
        return config.getProperty(key);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        applicationContext = applicationContext;
    }

    public static void put(String key, String value) {
        config.put(key, value);
    }
}
