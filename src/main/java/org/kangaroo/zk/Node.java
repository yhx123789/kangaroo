//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.zk;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Node implements Serializable {
    public static final String ROOT_NODE = "/Kangaroo";
    private Node.Role role;
    private String ns;
    private String system;
    private String env;
    private String host;
    private String alias;
    private String registryAddress;
    private Node.SubscribeType subscribeType;

    public Node(String registryAddress) {
        this(registryAddress, Node.Role.SERVICE);
    }

    public Node(String registryAddress, Node.Role role) {
        this.registryAddress = registryAddress;

        try {
            this.host = InetAddress.getLocalHost().getHostAddress();
            this.alias = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var4) {
            throw new IllegalStateException(var4);
        }

        this.role = role;
    }

    public static Node createAdminNode(String registryAddress) {
        return new Node(registryAddress, Node.Role.ADMIN);
    }

    public static Node createServiceNode(String registryAddress, String ns, String sys, String env) {
        Node node = new Node(registryAddress, Node.Role.SERVICE);
        node.ns = ns;
        node.system = sys;
        node.env = env;
        return node;
    }

    public String getNodeName() {
        return this.host + "_" + this.alias;
    }

    public String getNodePath() {
        return this.getParentNodePath() + "/" + this.getNodeName();
    }

    public String getParentNodePath() {
        StringBuilder sb = new StringBuilder();
        sb.append("/Kangaroo").append("/").append(this.role);
        if (Node.Role.ADMIN == this.role) {
            return sb.toString();
        } else {
            sb.append("/").append(this.ns).append("/").append(this.system).append("/").append(this.env);
            return sb.toString();
        }
    }

    public Node.Role getRole() {
        return this.role;
    }

    public void setRole(Node.Role role) {
        this.role = role;
    }

    public String getNs() {
        return this.ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getSystem() {
        return this.system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getEnv() {
        return this.env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Node.SubscribeType getSubscribeType() {
        return this.subscribeType;
    }

    public void setSubscribeType(Node.SubscribeType subscribeType) {
        this.subscribeType = subscribeType;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRegistryAddress() {
        return this.registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public static enum SubscribeType {
        PATH_CACHE,
        NODE_CACHE,
        TREE_CACHE;

        private SubscribeType() {
        }
    }

    public static enum Role {
        SERVICE,
        ADMIN,
        ANONYMOUS;

        private Role() {
        }
    }
}
