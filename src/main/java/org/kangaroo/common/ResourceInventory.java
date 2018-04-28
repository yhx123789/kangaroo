//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.common;

public class ResourceInventory {
    private static final Resource[] resources;

    public ResourceInventory() {
    }

    public static Resource[] list() {
        return resources;
    }

    static {
        resources = new Resource[]{Resource.MYSQL, Resource.REDIS, Resource.MSG_QUEUE, Resource.OSS, Resource.KV_PAIR_TABLE};
    }
}
