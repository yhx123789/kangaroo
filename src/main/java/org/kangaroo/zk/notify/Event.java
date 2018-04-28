package org.kangaroo.zk.notify;

import java.util.HashMap;
import java.util.Map;
import org.kangaroo.zk.Action;

public class Event {
    private Map<String, Object> context = new HashMap();
    private Action action;
    private Object extra;

    public Event() {
    }

    public static Event create(int topic, String target, String msgId) {
        return create(new Action(topic, target, msgId));
    }

    public static Event create(Action action) {
        Event event = new Event();
        event.action = action;
        return event;
    }

    public void addContextVariable(String key, Object obj) {
        this.context.put(key, obj);
    }

    public Object getContextVariable(String key) {
        return this.context.get(key);
    }

    public Map<String, Object> getContext() {
        return this.context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Object getExtra() {
        return this.extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
}
