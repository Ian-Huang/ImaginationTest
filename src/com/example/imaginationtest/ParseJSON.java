package com.example.imaginationtest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ParseJSON {

	private static String JSONString;

	private static JSONObject JsonObject = new JSONObject();

	public static ArrayList<String> nameList = new ArrayList<String>();
	public static ArrayList<String> valueList = new ArrayList<String>();

	public static void PutJsonData(String name, String value)
			throws JSONException {

		JsonObject.put(name, value);
		nameList.add(name);
		valueList.add(value);
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

	public static String UploadData(String url) {

		String strResult = null;

		HttpPost httpRequest = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("data", "get"));
		for (int i = 0; i < nameList.size(); i++) {
			params.add(new BasicNameValuePair(nameList.get(i), valueList.get(i)));
		}

		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				strResult = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);

				strResult = strResult.toString().replace("\r\n", "\\r\\n");
			}
		}

		catch (ClientProtocolException e) {
			return strResult;
		} catch (IOException e) {
			return strResult;
		} catch (Exception e) {
			return strResult;
		}
		return strResult;
	}
}