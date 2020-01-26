package com.divirad.flightcompensation.monolith.data.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringJoiner;

import javax.swing.event.EventListenerList;

import org.json.JSONObject;

import lib.StreamThread;

public class DataLoader {
	
	private static final String HOSTNAME = "https://api.aviationstack.com/v1/";
	private static final String ACCESS_KEY = "fd5dc26aa131c9e7f60f5ce54cf459ce";
	private static final int LIMIT = 100;

	public static DataLoader instance;
	
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
	
	private ArrayList<Constraint> constraints = new ArrayList<Constraint>() {
		private static final long serialVersionUID = 8934892856813631325L;

		public int indexOf(Object o) {
	        if (o == null) {
	            for (int i = 0; i < this.size(); i++)
	                if (this.get(i)==null)
	                    return i;
	        } else {
	            for (int i = 0; i < this.size(); i++)
	                if (this.get(i).equals(o))
	                    return i;
	        }
	        return -1;
	    }
	};
	
	private DataLoader(Constraint... constraints) {
		for(Constraint c : constraints)
			this.constraints.add(c);
	}
	
	public static DataLoader getInstance(Constraint... constraints) {
		if(instance == null) instance = new DataLoader(constraints);
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
	
	public DownloadListener[] getDownloadListeners() {
		return (DownloadListener[]) listenerList.getListeners(DownloadListener.class);
	}
	
	public <T> JSONObject getApiData(Class<T> resource) {
		StringJoiner filter = new StringJoiner("&", "?", "");
		filter.add("access_key=" + ACCESS_KEY);
		for(Constraint c : constraints)
			filter.add(c.key + "=" + c.value.value());
		String url = HOSTNAME + (resource.getSimpleName().toLowerCase() + "s") + filter;

		DownloadEvent<T> e = doGetCall(resource, url);
		JSONObject pagination = e.getResult().getJSONObject("pagination");
		e.setElements(pagination.getInt("count"));
		e.setTotal(pagination.getInt("limit"));
		e.setOffset(pagination.getInt("offset"));
		
		fireDownloaded(e);
		return e.getResult();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public <T> void getAllApiData(Class<T> resource) {
		DownloadEvent<T> e;
		fireStartingMultiDownload();
		int limit = 0;
		if(constraints.contains("limit"))
			limit = Integer.parseInt(constraints.get(constraints.indexOf("limit")).value.value());
		do {
			StreamThread.currentThread().getOut().println("New Request");
			StringJoiner filter = new StringJoiner("&", "?", "");
			filter.add("access_key=" + ACCESS_KEY);
			int offset = Integer.parseInt(offsetController.value());
			for(Constraint c : constraints) {
				if(c.equals("limit") && limit - offset - 100 < 0)
					filter.add(c.key + "=" + (Integer.parseInt(c.value.value()) - offset));
				else if(!c.equals("limit"))
					filter.add(c.key + "=" + c.value.value());
			}
			filter.add("offset" + "=" + offset);
			String url = HOSTNAME + (resource.getSimpleName().toLowerCase() + "s") + filter;
			
			e = doGetCall(resource, url);
			
			JSONObject pagination = e.getResult().getJSONObject("pagination");
			e.setElements(pagination.getInt("count"));
			e.setTotal(limit == 0 ? pagination.getInt("total") : limit);
			e.setOffset(pagination.getInt("offset"));
			if(e.getOffset() + e.getElements() >= e.getTotal())
				e.setIsLastDownload(true);
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
		
		@Override
		public boolean equals(Object o) {
			if(o instanceof String)
				return key.equals(o);
			return false;
		}
	}
	
	
}
