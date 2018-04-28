//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.common.domain;

import com.alibaba.fastjson.JSONObject;
import java.util.Date;

public class Base {
    private String id;
    private Date createdAt;

    public Base() {
    }

    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
