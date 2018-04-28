//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.zk;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;

public class Action implements Serializable {
    public static final int APP_INITIAL = 1;
    public static final int MYSQL_SOURCE = 2;
    public static final int REDIS_SOURCE = 3;
    public static final int SPRING_CONTEXT = 4;
    public static final int MESSAGE_QUEUE = 5;
    public static final int OSS = 6;
    public static final int PAIR = 7;
    public static final int MYSQL_ADD = 201;
    public static final int MYSQL_UPDATE = 202;
    public static final int MYSQL_REMOVE = 203;
    public static final int MYSQL_UPDATE_INSTANCE = 204;
    public static final int MYSQL_REMOVE_INSTANCE = 205;
    public static final int REDIS_ADD = 301;
    public static final int REDIS_UPDATE = 302;
    public static final int REDIS_REMOVE = 303;
    public static final int REDIS_UPDATE_INSTANCE = 304;
    public static final int REDIS_REMOVE_INSTANCE = 305;
    public static final int SPRING_CONTEXT_UPDATE = 402;
    public static final int APP_KEY_RES_CLEAR = 403;
    public static final int MESSAGE_QUEUE_ADD = 501;
    public static final int MESSAGE_QUEUE_UPDATE = 502;
    public static final int MESSAGE_QUEUE_REMOVE = 503;
    public static final int MESSAGE_QUEUE_UPDATE_INSTANCE = 504;
    public static final int MESSAGE_QUEUE_REMOVE_INSTANCE = 505;
    public static final int OSS_ADD = 601;
    public static final int OSS_UPDATE = 602;
    public static final int OSS_REMOVE = 603;
    public static final int OSS_UPDATE_INSTANCE = 604;
    public static final int OSS_REMOVE_INSTANCE = 605;
    public static final int PAIR_ADD = 701;
    public static final int PAIR_UPDATE = 702;
    public static final int PAIR_REMOVE = 703;
    private int topic;
    private String content;
    private String msgId;

    public Action() {
    }

    public Action(int topic, String content, String msgId) {
        this.topic = topic;
        this.content = content;
        this.msgId = msgId;
    }

    public static int getSupTopic(int subTopic) {
        return subTopic / 100;
    }

    public static int getSubTopic(int supTopic, int action) {
        return supTopic * 100 + action;
    }

    public int getTopic() {
        return this.topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
