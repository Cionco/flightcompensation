package com.divirad.flightcompensation.monolith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONObject;

import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.api.Parser;



public class App {
    
	
	public static void main(String[] args) {
		File f = new File("C:\\Users\\h4098099\\Desktop\\xamplejson.json");
		try {
			String content = new String(Files.readAllBytes(Paths.get(f.toURI())), "UTF-8");
			JSONObject o = new JSONObject(content);
			ArrayList<Flight> flights = Parser.getInstance().parseFlights(o.getJSONArray("data"));
			System.out.println(flights);
		} catch(IOException e) {
			e.printStackTrace();
		}
    }
}
