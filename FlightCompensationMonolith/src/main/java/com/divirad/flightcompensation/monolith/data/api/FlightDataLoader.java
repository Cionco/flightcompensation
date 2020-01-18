package com.divirad.flightcompensation.monolith.data.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringJoiner;

import javax.swing.event.EventListenerList;

import org.json.JSONObject;

public class FlightDataLoader {
	
	private static final String HOSTNAME = "https://api.aviationstack.com/v1/";
	private static final String ACCESS_KEY = "fd5dc26aa131c9e7f60f5ce54cf459ce";
	private static final int LIMIT = 100;

	public static FlightDataLoader instance;
	
	private EventListenerList listenerList = new EventListenerList();
	
	public interface IDetValue {
		public String value();
	}
	
	private IDetValue offsetController = new IDetValue() {
		int offset = 0;
		
		public String value() {
			int return_value = offset;
			offset += LIMIT;
			return Integer.toString(return_value);
		}
	};
	
	private ArrayList<Constraint> constraints = new ArrayList<>();
	
	private FlightDataLoader(Constraint... constraints) {
		for(Constraint c : constraints)
			this.constraints.add(c);
	}
	
	public static FlightDataLoader getInstance(Constraint... constraints) {
		if(instance == null) instance = new FlightDataLoader(constraints);
		return instance;
	}
	
	public void addDownloadListener(DownloadListener l) {
	    listenerList.add(DownloadListener.class, l);
	}
	
	public void removeDownloadListener(DownloadListener l) {
		listenerList.remove(DownloadListener.class, l);
	}
	
	protected void fireStartingMultiDownload() {
		DownloadListener[] listeners = getDownloadListeners();
		for(DownloadListener l : listeners) {
			l.startingMultiDownload();
		}
	}
	
	protected <T> void fireDownloaded(Class<T> resource, String url, int status_code, JSONObject result) {
		fireDownloaded(new DownloadEvent<T>(resource, url, status_code, result));
	}
	
	protected <T> void fireDownloaded(DownloadEvent<T> e) {
		DownloadListener[] listeners = getDownloadListeners();
		for(DownloadListener l : listeners) {
			l.dataDownloaded(e);
		}
	}
	
	protected void fireDoneDownloading() {
		DownloadListener[] listeners = getDownloadListeners();
		for(DownloadListener l : listeners) {
			l.doneDownloading();
		}
	}
	
	public DownloadListener[] getDownloadListeners() {
		return (DownloadListener[]) listenerList.getListeners(DownloadListener.class);
	}
	
	public <T> JSONObject getApiData(Class<T> resource) {
		StringJoiner filter = new StringJoiner("&", "?", "");
		filter.add("access_key=" + ACCESS_KEY);
		for(Constraint c : constraints)
			filter.add(c.key + "=" + c.value.value());
		String url = HOSTNAME + (resource.getName().toLowerCase() + "s") + filter;

		DownloadEvent<T> e = doGetCall(resource, url);
		
		fireDownloaded(e);
		return e.getResult();
	}
	
	public <T> void getAllApiData(Class<T> resource) {
		DownloadEvent<T> e;
		fireStartingMultiDownload();
		do {
			System.out.println("New Request");
			StringJoiner filter = new StringJoiner("&", "?", "");
			filter.add("access_key=" + ACCESS_KEY);
			for(Constraint c : constraints)
				filter.add(c.key + "=" + c.value.value());
			filter.add("offset" + "=" + offsetController.value());
			String url = HOSTNAME + (resource.getName().toLowerCase() + "s") + filter;
			
			e = doGetCall(resource, url);
			
			JSONObject pagination = e.getResult().getJSONObject("pagination");
			e.setElements(pagination.getInt("limit"));
			e.setTotal(pagination.getInt("total"));
			e.setOffset(pagination.getInt("offset"));
			if(e.getOffset() + e.getElements() >= e.getTotal())
				fireDoneDownloading();
			fireDownloaded(e);
		} while(e.getOffset() + e.getElements() < e.getTotal());
		
		
	}
	
	private <T> DownloadEvent<T> doGetCall(Class<T> resource, String url) {
		HttpURLConnection conn = null;
		DownloadEvent<T> ev = new DownloadEvent<>(resource);
		String output = "";
		ev.setUrl(url);
		try {  
		    URL obj = new URL(url);
		    conn = (HttpURLConnection) obj.openConnection();
		    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		    conn.setDoOutput(false);
		    conn.setRequestMethod("GET");
		    
		    ev.setStatus_code(conn.getResponseCode());
		    
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			for (String line; (line = reader.readLine()) != null;) {
			        output += line;
			}
			reader.close();
			ev.setResult(new JSONObject(output));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ev;
	}
	
	public static class Constraint {
		public String key;
		public IDetValue value;
		
		public Constraint(String key, IDetValue value) {
			this.key = key;
			this.value = value;
		}
	}
	
	
}
