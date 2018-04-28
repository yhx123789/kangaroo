package org.kangaroo.common;

public class ThreadContextHolder {
    private static final ThreadLocal<String> context = new ThreadLocal();

    public ThreadContextHolder() {
    }

    public static void set(String appKey) {
        Assert.notNull(appKey, "app key cannot be null");
        context.set(appKey);
    }

    public static String get() {
        return (String)context.get();
    }

    public static void clear() {
        context.remove();
    }
}
