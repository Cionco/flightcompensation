package com.divirad.flightcompensation.monolith.data.api;

import java.util.ArrayList;

import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.database.FlightDao;

public class DownloadParseSaveController implements DownloadListener, ParseListener {

	private int downloaded_objects = 0;
	private int parsed_objects = 0;
	private boolean downloading = false;
	
	private ArrayList<Flight> flights = new ArrayList<>();
	
	@Override
	public void startingMultiDownload() {
		downloaded_objects = 0;
		parsed_objects = 0;
		downloading = true;
	}
	
	@Override
	public void dataDownloaded(DownloadEvent e) {
		System.out.println("Recieved new Data (" + Math.min(e.getOffset() + e.getElements(), e.getTotal()) + "/" + e.getTotal() + ")");
		downloaded_objects++;
		Parser.getInstance().parseFlights(e.getResult().getJSONArray("data"));		
	}

	@Override
	public void doneDownloading() {
		System.out.println("All " + downloaded_objects + " objects downloaded");
		downloading = false;
	}

	@Override
	public void jsonParsed(ParseEvent e) {
		flights.addAll(e.getResult());
		parsed_objects++;
		if(!downloading && parsed_objects == downloaded_objects) {
			System.out.println("Done parsing, writing to db");
			FlightDao.instance.storeFlights(flights);
			
		}
	}
}
