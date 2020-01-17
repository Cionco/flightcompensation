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
import com.divirad.flightcompensation.monolith.data.api.FlightDataLoader;
import com.divirad.flightcompensation.monolith.data.api.FlightDataLoader.Constraint;
import com.divirad.flightcompensation.monolith.data.api.Parser;
import com.divirad.flightcompensation.monolith.data.database.FlightDao;



public class App {
    
	
	public static void main(String[] args) {
		ArrayList<Flight> flights = null;
		FlightDataLoader fdl = FlightDataLoader.getInstance(	new Constraint("flight_date", () -> LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
															,	new Constraint("dep_iata", () -> "fra"));
		
		JSONObject o = fdl.getApiFlightData();
		
//		File f = new File("C:\\Users\\h4098099\\Desktop\\xamplejson.json");
//		try {
//			String content = new String(Files.readAllBytes(Paths.get(f.toURI())), "UTF-8");
//			JSONObject o = new JSONObject(content);
		flights = Parser.getInstance().parseFlights(o.getJSONArray("data"));
			
//		} catch(IOException e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
		
		FlightDao.instance.storeFlights(flights);
    }
}
