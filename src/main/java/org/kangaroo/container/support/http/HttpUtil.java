//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.container.support.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtil {
	public HttpUtil() {
	}

	public static HttpResponse get(String url, Map<String, String> payload, Map<String, String> header) {
		HttpClient httpClient = new DefaultHttpClient();
		StringBuilder queryString = new StringBuilder();
		if (null != payload) {
			Iterator var5 = payload.keySet().iterator();
			while (var5.hasNext()) {
				String item = (String) var5.next();
				try {
					String itemValue = URLEncoder.encode((String) payload.get(item), "utf-8");
					queryString.append(item).append("=").append(itemValue).append("&");
				} catch (UnsupportedEncodingException var9) {
					var9.printStackTrace();
				}
			}
		}

		HttpGet get;
		if (0 != queryString.length()) {
			get = new HttpGet(url + "?" + queryString.substring(0, queryString.length() - 1));
		} else {
			get = new HttpGet(url);
		}

		if (null != header) {
			Iterator var11 = header.keySet().iterator();
			while (var11.hasNext()) {
				String item = (String) var11.next();
				get.addHeader(item, (String) header.get(item));
			}
		}

		try {
			System.out.println("get = " + get);
			HttpResponse httpResponse = httpClient.execute(get);
			return httpResponse;
		} catch (Exception var8) {
			throw new IllegalStateException(var8);
		}
	}

	public static HttpResponse post(String url, Map<String, String> payload, Map<String, String> header) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(url);
		if (null != header) {
			Iterator var5 = header.keySet().iterator();

			while (var5.hasNext()) {
				String item = (String) var5.next();
				postMethod.addHeader(item, (String) header.get(item));
			}
		}

		List<NameValuePair> pairList = new ArrayList();
		if (null != payload) {
			Iterator var10 = payload.keySet().iterator();

			while (var10.hasNext()) {
				String item = (String) var10.next();
				pairList.add(new BasicNameValuePair(item, (String) payload.get(item)));
			}
		}

		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList, "utf-8");
			postMethod.setEntity(entity);
			return httpClient.execute(postMethod);
		} catch (IOException var8) {
			throw new IllegalStateException(var8);
		}
	}
	
	
}
