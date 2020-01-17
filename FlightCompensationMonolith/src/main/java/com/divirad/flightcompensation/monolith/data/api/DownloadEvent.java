package com.divirad.flightcompensation.monolith.data.api;

import org.json.JSONObject;

public class DownloadEvent {

	private String url;
	private int status_code;
	private JSONObject result;
	
	public DownloadEvent() {}
	
	public DownloadEvent(String url, int status_code, JSONObject result) {
		this.url = url;
		this.status_code = status_code;
		this.result = result;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

	public String getUrl() {
		return url;
	}

	public int getStatus_code() {
		return status_code;
	}

	public JSONObject getResult() {
		return result;
	}
	
	
}
