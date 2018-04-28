package org.kangaroo.container.support.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.kangaroo.common.KangarooConfig;
import org.kangaroo.common.Resource;
import org.kangaroo.common.domain.Mysql;
import org.kangaroo.common.notify.NotifyCallback;

import com.alibaba.fastjson.JSONObject;

import sun.tools.tree.ThisExpression;

public class ConfigClient {
	private static final String auth = "a2FuZ2Fyb28=";
	public static String environmentId;
	private static String hostAddress;
	private static Map<String, String> header;

	public ConfigClient() {
	}

	public static void init(String env, String hostAddress2) {
		environmentId = env;
		hostAddress = hostAddress2;
		header = new HashMap();
		header.put("Authorization", "a2FuZ2Fyb28=");
	}

	public static String queryPrivateKey() throws LoadConfigException {
		Map<String, String> payload = new HashMap();
		payload.put("envId", environmentId);
		String result = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKH6f5Ib2BUonJTdK0NUSx0/t3e3BTMt/0VIuOYEGT3yQ/WfDIhKGIV0pqER/VA/z2+yhkUrvBdfEiRaD3YAweHbRIueYcmJ2nctKQj4YFIDzYnsNIRs3vti9+Wdcm+UZabHwD1wP/iIW85Gjwd+nKs39wn6fNG8zaKeBlCOQoVxAgMBAAECgYBfcRKqLO3tybg5nvhW+9wmp9ybFaRiu7+mO2t3XQLilVWl7MmdpMs+F7X3QnpYRsEbt72WFGcH/mTaNpbVmSyjzrQ0waM3W5uQI1kqLlQtuwiwoAXsnu0/ih8YUTChCC7jOzpTttQDOo6s3m/1wDf8jq2SQZfmoEqLGgm2q97GAQJBAOWOL02/xoVoY3glTykt388KOo9FGLE7q9c6hjzsrxDQ4fsDC9czHDAetG2IATvxO4sfqx83GL4io6qNPpeZZrkCQQC0o2d3dGUj0q6TmAcw2Gs18K8IkO9H2aD1cpO4gFPklSue4UO6aTvsWlO6ZOv0MV76XUcaNeqv9MxTRnhWTLh5AkEAw5Yc6dLPc9WYgsLPKtfiTIZ7lJybyE5+QJilzX0gQGSpVnSm/wmWJ83Rj02FN6x3PjeSVrtcAoyDBIrp3MMgSQJBAIhuKgVEuebWhtgeqL+xlTfGMszLeo3Pxwvan8tn0PUB3VJAXQD73jBjAH31H2jtqQh7Tupy8nA2+eOjg9vaUjECQQCyRVS5JtgEBDlOOVXgCp22EkEG7i0Vccbc9/7W5oC7vjmgXPANuBwaS8vvIuVKZvRne5D/+uuh/mMuPH0ppfGE";
	
//		return getExecute(assembleUrl("/client/query/rsa/private"), payload);
		return result;
	}

	public static KangarooConfig queryRemoteConfig() throws LoadConfigException {
		Map<String, String> payload = new HashMap();
		payload.put("envId", environmentId);
		return (KangarooConfig) JSONObject.parseObject(getExecute(assembleUrl("/client/query/config/all"), payload),
				KangarooConfig.class);
	}

	public static List<Mysql> queryMysql(List<String> mysqlIds) throws LoadConfigException {
		Map<String, String> payload = new HashMap();
		payload.put("envId", environmentId);
		payload.put("mysqlIds", JSONObject.toJSONString(mysqlIds));
		KangarooConfig config = (KangarooConfig) JSONObject
				.parseObject(getExecute(assembleUrl("/client/query/mysql"), payload), KangarooConfig.class);
		return config.getResource(Resource.MYSQL, Mysql.class);
	}

	// public static List<Redis> queryRedisSource(List<String> redisIds) throws
	// LoadConfigException {
	// Map<String, String> payload = new HashMap();
	// payload.put("envId", environmentId);
	// payload.put("redisIds", JSONObject.toJSONString(redisIds));
	// KangarooConfig config = (KangarooConfig) JSONObject
	// .parseObject(getExecute(assembleUrl("/client/query/redis"), payload),
	// KangarooConfig.class);
	// return config.getResource(Resource.REDIS, Redis.class);
	// }
	//
	// public static List<MsgQueue> queryMsgQueue(List<String> mqIds) throws
	// LoadConfigException {
	// Map<String, String> payload = new HashMap();
	// payload.put("envId", environmentId);
	// payload.put("mqIds", JSONObject.toJSONString(mqIds));
	// KangarooConfig config = (KangarooConfig) JSONObject
	// .parseObject(getExecute(assembleUrl("/client/query/mq"), payload),
	// KangarooConfig.class);
	// return config.getResource(Resource.MSG_QUEUE, MsgQueue.class);
	// }
	//
	// public static List<Oss> queryOss(List<String> ossIds) throws
	// LoadConfigException {
	// Map<String, String> payload = new HashMap();
	// payload.put("envId", environmentId);
	// payload.put("ossIds", JSONObject.toJSONString(ossIds));
	// KangarooConfig config = (KangarooConfig) JSONObject
	// .parseObject(getExecute(assembleUrl("/client/query/oss"), payload),
	// KangarooConfig.class);
	// return config.getResource(Resource.OSS, Oss.class);
	// }
	//
	// public static List<Pair> queryPair(List<String> appKeys) throws
	// LoadConfigException {
	// Map<String, String> payload = new HashMap();
	// payload.put("envId", environmentId);
	// payload.put("appKeys", JSONObject.toJSONString(appKeys));
	// KangarooConfig config = (KangarooConfig) JSONObject
	// .parseObject(getExecute(assembleUrl("/client/query/pair"), payload),
	// KangarooConfig.class);
	// return config.getResource(Resource.KV_PAIR_TABLE, Pair.class);
	// }

	public static void callback(NotifyCallback callback) throws LoadConfigException {
		Map<String, String> payload = new HashMap();
		payload.put("envId", environmentId);
		payload.put("callback", JSONObject.toJSONString(callback));
//		if (!"success".equals(postExecute(assembleUrl("/client/command/callback"), payload))) {
//			throw new LoadConfigException("Callback Failed");
//		}
	}

	private static String getExecute(String url, Map<String, String> payload) throws LoadConfigException {
		// String str = responseFormat(url, HttpUtil.get(url, payload, header));
		String str = HttpUtil.getStr();
		return str;
	}

	private static String postExecute(String url, Map<String, String> payload) throws LoadConfigException {
		return responseFormat(url, HttpUtil.post(url, payload, header));
	}

	private static String responseFormat(String url, HttpResponse response) throws LoadConfigException {
		if (200 == response.getStatusLine().getStatusCode()) {
			try {
				String result = EntityUtils.toString(response.getEntity());
				return result;
			} catch (IOException var3) {
				throw new IllegalStateException(var3);
			}
		} else {
			try {
				throw new LoadConfigException(
						"Failure to Request " + url + " . Http Code:" + response.getStatusLine().getStatusCode()
								+ " cause: " + EntityUtils.toString(response.getEntity()));
			} catch (IOException var4) {
				throw new IllegalStateException(var4);
			}
		}
	}

	private static String assembleUrl(String api) {
		return hostAddress + (hostAddress.endsWith("/") ? api.substring(1) : api);
	}

	interface ConfigAPI {
		String CONFIG_ALL = "/client/query/config/all";
		String QUERY_RSA_PRIVATE = "/client/query/rsa/private";
		String QUERY_MYSQL = "/client/query/mysql";
		String QUERY_REDIS = "/client/query/redis";
		String QUERY_OSS = "/client/query/oss";
		String QUERY_PAIR = "/client/query/pair";
		String QUERY_MQ = "/client/query/mq";
		String CALL_BACK = "/client/command/callback";
	}
}
