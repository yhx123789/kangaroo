//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.common;

public class ResourceEnum {
    public static final int MYSQL = 1;
    public static final int REDIS = 2;
    public static final int PAIR = 4;
    public static final int MQ = 8;
    public static final int OSS = 16;

    public ResourceEnum() {
    }

    public static boolean hasMysql(int res) {
        return (1 & res) > 0;
    }

    public static boolean hasRedis(int res) {
        return (2 & res) > 0;
    }

    public static boolean hasPair(int res) {
        return (4 & res) > 0;
    }

    public static boolean hasMQ(int res) {
        return (8 & res) > 0;
    }

    public static boolean hasOSS(int res) {
        return (16 & res) > 0;
    }
}
