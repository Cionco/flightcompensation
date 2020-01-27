package com.divirad.flightcompensation.micro.collector.data.api;

import java.sql.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.divirad.flightcompensation.micro.collector.data.database.JSONDao;
import com.divirad.flightcompensation.monolith.data.api.DownloadEvent;
import com.divirad.flightcompensation.monolith.data.api.DownloadListener;

public class DownloadController implements DownloadListener {
	
	int downloaded_objects = 0;
	boolean downloading = false;
	JSONArray data;
	Date new_data_date;
	
	@Override
	public void startingMultiDownload() {
		downloaded_objects = 0;
		downloading = true;
		data = new JSONArray();
	}

	@Override
	public void dataDownloaded(DownloadEvent<?> e) {
		System.out.println("Recieved new Data (" + Math.min(e.getOffset() + e.getElements(), e.getTotal()) + "/" + e.getTotal() + ")");
		downloaded_objects++;
		data = joinArrays(data, e.getResult().getJSONArray("data"));
		if(e.isLastDownload()) {
			System.out.println("All " + downloaded_objects + " objects downloaded");
			downloading = false;
			JSONDao.instance.store(data, downloaded_objects, e.getResource().getSimpleName(), new_data_date);
		}
		
	}
	
	private JSONArray joinArrays(JSONArray a, JSONArray b) {
		for(Object o : b) 
			a.put((JSONObject) o);
		return a;
	}
	
	public void setNewDataDate(Date date) {
		this.new_data_date = date;
	}
	
	public Date getNewDataDate() {
		return new_data_date;
	}

}
