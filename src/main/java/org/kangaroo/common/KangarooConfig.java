package org.kangaroo.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.kangaroo.common.domain.AppKey;

public class KangarooConfig implements Serializable {
	private String zookeeperAddress;
	private String ns;
	private String system;
	private String env;
	private List<AppKey> appKeys;
	private Properties properties;
	private Map<Resource, List> resources = new HashMap();

	public KangarooConfig() {
	}

	public void putResource(Resource resEnum, List res) {
		if (null != res) {
			this.resources.put(resEnum, res);
		}
	}

	public <T> List<T> getResource(Resource resEnum, Class<T> clazz) {
		List objects = (List) this.resources.get(resEnum);
		return null == objects ? null : JSONObject.parseArray(JSON.toJSONString(objects), clazz);
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

	public Properties getProperties() {
		return this.properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Map<Resource, List> getResources() {
		return this.resources;
	}

	public void setResources(Map<Resource, List> resources) {
		this.resources = resources;
	}

	public List<AppKey> getAppKeys() {
		return this.appKeys;
	}

	public void setAppKeys(List<AppKey> appKeys) {
		this.appKeys = appKeys;
	}

	public String getZookeeperAddress() {
		return this.zookeeperAddress;
	}

	public void setZookeeperAddress(String zookeeperAddress) {
		this.zookeeperAddress = zookeeperAddress;
	}

	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
