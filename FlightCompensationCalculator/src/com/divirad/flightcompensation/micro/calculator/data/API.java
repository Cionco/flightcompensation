package com.divirad.flightcompensation.micro.calculator.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class API {
	
	private final static String HOSTNAME = "http://localhost:8080/FlightCompensationTransformer";

	public interface ISetParams {
		String run(String url);
	}
	
	public interface IUseResult<T> {
		T run(JSONObject o);
	}
	
	public static <T> T get(String url, IUseResult<T> useResult) {
		return get(url, u -> u, useResult);
	}
	
	public static <T> T get(String resource, ISetParams setParams, IUseResult<T> useResult) {
		JSONObject result = null;
		String output = "";
		resource = setParams.run(resource);
		try {
			URL obj = new URL(HOSTNAME + "/" + resource);
			System.out.println("Requesting: " + obj.toString());
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		    conn.setDoOutput(false);
		    conn.setRequestMethod("GET");
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			for (String line; (line = reader.readLine()) != null;) {
			        output += line;
			}
			reader.close();
			result = new JSONObject(output);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return useResult.run(result);
	}
}
