package org.kangaroo.common;

import com.alibaba.fastjson.JSONObject;

public class Serialize {
    public Serialize() {
    }

    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
