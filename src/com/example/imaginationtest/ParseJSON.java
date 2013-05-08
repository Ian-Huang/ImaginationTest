package com.example.imaginationtest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ParseJSON {

	private static String JSONString;

	private static JSONObject JsonObject = new JSONObject();

	public static void PutJsonData(String name, String value)
			throws JSONException {
		JsonObject.put(name, value);
	}

	public static void JsonOutput() throws JSONException {
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(JsonObject);

		JSONString = jsonArray.toString();
		Log.i("JSON String", JSONString);
	}
}