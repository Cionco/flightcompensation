package com.divirad.flightcompensation.monolith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONObject;

import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.api.Parser;
import com.divirad.flightcompensation.monolith.data.database.FlightDao;



public class App {
    
	
	public static void main(String[] args) {
		ArrayList<Flight> flights = null;
		
		File f = new File("/Users/nicolaskepper/Downloads/xamplejson.json");
		try {
			String content = new String(Files.readAllBytes(Paths.get(f.toURI())), "UTF-8");
			JSONObject o = new JSONObject(content);
			flights = Parser.getInstance().parseFlights(o.getJSONArray("data"));
			
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		FlightDao.instance.storeFlights(flights);
    }
}
