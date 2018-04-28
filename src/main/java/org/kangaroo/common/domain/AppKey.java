//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.common.domain;

import java.util.List;

public class AppKey extends Base {
	private String envId;
	private String appKey;
	private String mysql;
	private String redis;
	private String oss;
	private List<String> msgQueues;

	public AppKey() {
	}

	public AppKey(String envId, String appKey) {
		this.envId = envId;
		this.appKey = appKey;
	}

	public String getEnvId() {
		return this.envId;
	}

	public void setEnvId(String envId) {
		this.envId = envId;
	}

	public String getAppKey() {
		return this.appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getMysql() {
		return this.mysql;
	}

	public void setMysql(String mysql) {
		this.mysql = mysql;
	}

	public String getRedis() {
		return this.redis;
	}

	public void setRedis(String redis) {
		this.redis = redis;
	}

	public String getOss() {
		return this.oss;
	}

	public void setOss(String oss) {
		this.oss = oss;
	}

	public List<String> getMsgQueues() {
		return this.msgQueues;
	}

	public void setMsgQueues(List<String> msgQueues) {
		this.msgQueues = msgQueues;
	}
}
