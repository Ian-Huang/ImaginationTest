package com.example.imaginationtest;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.util.Log;

public class ParseJSON {

	private static String JSONString;

	private static JSONObject JsonObject = new JSONObject();

	public static void PutJsonData(String name, String value)
			throws JSONException {
		JsonObject.put(name, value);
	}

	public static void JsonOutput() throws JSONException {
		// JSONArray jsonArray = new JSONArray();
		// jsonArray.put(JsonObject);

		JSONString = JsonObject.toString();
		Log.i("JSON String", JSONString);
	}

	public static String GetJSONString() {
		return JSONString;
	}

	public static void PostData(String url) {

		// Create a new HttpClient and Post Header
		HttpParams myParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		HttpConnectionParams.setSoTimeout(myParams, 10000);
		HttpClient httpclient = new DefaultHttpClient(myParams);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		try {

			HttpPost httppost = new HttpPost(url.toString());
			httppost.setHeader("Content-type", "application/json");

			StringEntity se = new StringEntity(JSONString);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httppost.setEntity(se);

			HttpResponse response = httpclient.execute(httppost);
			String temp = EntityUtils.toString(response.getEntity());
			Log.i("JSON String", "YES:" + temp);

		} catch (ClientProtocolException ce) {
			Log.i("JSON String", ce.getMessage());
		} catch (IOException ie) {
			Log.i("JSON String", ie.getMessage());
		}
	}
}