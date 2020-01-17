package com.divirad.flightcompensation.monolith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.api.DownloadEvent;
import com.divirad.flightcompensation.monolith.data.api.DownloadListener;
import com.divirad.flightcompensation.monolith.data.api.FlightDataLoader;
import com.divirad.flightcompensation.monolith.data.api.FlightDataLoader.Constraint;
import com.divirad.flightcompensation.monolith.data.api.ParseEvent;
import com.divirad.flightcompensation.monolith.data.api.ParseListener;
import com.divirad.flightcompensation.monolith.data.api.Parser;
import com.divirad.flightcompensation.monolith.data.database.FlightDao;



public class App implements DownloadListener, ParseListener {
	private int downloaded_objects = 0;
	private int parsed_objects = 0;
	private boolean downloading = false;
	private ArrayList<Flight> flights = new ArrayList<>();
	
    public static void main(String[] args) {
    	new App().run();
    }
	
	public void run() {
		
		FlightDataLoader fdl = FlightDataLoader.getInstance(	new Constraint("flight_date", () -> LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
															,	new Constraint("dep_iata", () -> "haj"));
		fdl.addDownloadListener(this);
		Parser.getInstance().addParseListener(this);
		//JSONObject o = fdl.getApiFlightData();
		fdl.getAllApiFlightData();
		
//		File f = new File("C:\\Users\\h4098099\\Desktop\\xamplejson.json");
//		try {
//			String content = new String(Files.readAllBytes(Paths.get(f.toURI())), "UTF-8");
//			JSONObject o = new JSONObject(content);
//		flights = Parser.getInstance().parseFlights(o.getJSONArray("data"));
			
//		} catch(IOException e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
		
		//FlightDao.instance.storeFlights(flights);
    }

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
		if(!false && parsed_objects == downloaded_objects) {
			System.out.println("Done parsing, writing to db");
			FlightDao.instance.storeFlights(flights);
			
		}
	}


}
