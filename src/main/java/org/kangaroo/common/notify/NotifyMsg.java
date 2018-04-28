package org.kangaroo.common.notify;

import java.util.Date;
import org.kangaroo.common.Serialize;

public class NotifyMsg extends Serialize {
    private String id;
    private int topic;
    private String path;
    private String content;
    private Date time;

    public NotifyMsg() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
