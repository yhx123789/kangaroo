//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.common;

public enum Resource {
    MYSQL("MYSQL"),
    REDIS("REDIS"),
    MSG_QUEUE("MSG_QUEUE"),
    OSS("OSS"),
    KV_PAIR_TABLE("KV_PAIR_TABLE");

    private String name;

    private Resource(String name) {
        this.name = name;
    }
}
