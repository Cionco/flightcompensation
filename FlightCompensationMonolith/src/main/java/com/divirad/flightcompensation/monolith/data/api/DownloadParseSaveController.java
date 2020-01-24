package com.divirad.flightcompensation.monolith.data.api;

import java.util.ArrayList;

import com.divirad.flightcompensation.monolith.data.Airport;
import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.database.AirportDao;
import com.divirad.flightcompensation.monolith.data.database.FlightDao;

public class DownloadParseSaveController implements DownloadListener, ParseListener {

	private int downloaded_objects = 0;
	private int parsed_jsons = 0;
	private boolean downloading = false;
	
	private ArrayList<Object> parsed = new ArrayList<>();
	
	@Override
	public void startingMultiDownload() {
		//downloaded_objects = 0;
		parsed_jsons = 0;
		downloading = true;
	}
	
	@Override
	public void dataDownloaded(DownloadEvent<?> e) {
		System.out.println("Recieved new Data (" + Math.min(e.getOffset() + e.getElements(), e.getTotal()) + "/" + e.getTotal() + ")");
		downloaded_objects++;
		if(e.isLastDownload()) {
			System.out.println("All " + downloaded_objects + " objects downloaded");
			downloading = false;
		}
		Parser.getInstance().parseData(e.getResource(), e.getResult().getJSONArray("data"));
	}

	@Override
	public void jsonParsed(ParseEvent<?> e) {
		parsed.addAll(e.getResult());
		if(!downloading) {
			System.out.println("Done parsing, writing to db");
			if(e.getResource() == Flight.class)
				FlightDao.instance.storeFlights(typifyArrayList(parsed));
			else if(e.getResource() == Airport.class) {
				AirportDao.instance.updateAirports(typifyArrayList(parsed));
			}
			parsed = new ArrayList<>();
		}
	}

	@Override
	public void objectParsed(ParseEvent<?> e) {
		System.out.println("Parsed object: " + e.getResult().get(0));
	}
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> typifyArrayList(ArrayList<Object> objects) {
		ArrayList<T> result = new ArrayList<>();
		for(Object o : objects)
			result.add((T) o);
		return result;
	}
}
