package com.divirad.flightcompensation.monolith.data.api;

import org.json.JSONObject;

public class DownloadEvent<T> {
	
	private Class<T> resource;

	private String url;
	private int status_code;
	private JSONObject result;
	
	private int elements;
	private int offset;
	private int total;
	
	private boolean is_last_download = false;
	
	public DownloadEvent(Class<T> resource) {
		this.resource = resource;
	}
	
	public DownloadEvent(Class<T> resource, String url, int status_code, JSONObject result) {
		this.resource = resource;
		this.url = url;
		this.status_code = status_code;
		this.result = result;
	}
	
	public DownloadEvent(Class<T> resource, String url, int status_code, JSONObject result, int elements, int offset, int total) {
		super();
		this.resource = resource;
		this.url = url;
		this.status_code = status_code;
		this.result = result;
		this.elements = elements;
		this.offset = offset;
		this.total = total;
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

	public void setElements(int elements) {
		this.elements = elements;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public void setIsLastDownload(boolean is_last) {
		this.is_last_download = is_last;
	}

	public Class<T> getResource() {
		return resource;
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

	public int getElements() {
		return elements;
	}

	public int getOffset() {
		return offset;
	}

	public int getTotal() {
		return total;
	}
	
	public boolean isLastDownload() {
		return is_last_download;
	}
	
}
