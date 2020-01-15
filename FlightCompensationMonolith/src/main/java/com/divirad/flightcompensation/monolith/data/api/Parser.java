package com.divirad.flightcompensation.monolith.data.api;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.FlightStatusDao;

public final class Parser {

	private static Parser instance;
	
	private Parser() {}
	
	public static Parser getInstance() {
		if(instance == null) instance = new Parser();
		return instance;
	}
	
	public ArrayList<Flight> parseFlights(JSONArray json_flights) {
		ArrayList<Flight> result = new ArrayList<>();
		
		for(Object flight : json_flights) {
			JSONObject json_flight = (JSONObject) flight;
			Flight f = new Flight();
			f.flight_date = Date.valueOf(json_flight.getString("flight_date"));
			f.flight_status = FlightStatusDao.instance.getIdByName(json_flight.getString("flight_status"));
			
			JSONObject departure = json_flight.getJSONObject("departure");
			JSONObject arrival = json_flight.getJSONObject("arrival");
			f.origin_airport = departure.getString("iata");
			f.scheduled_department	= ISO8601ToTimestamp(departure.getString("scheduled"));
			f.actual_department		= ISO8601ToTimestamp(departure.getString("actual"));
			f.destination_airport = arrival.getString("iata");
			f.scheduled_arrival		= ISO8601ToTimestamp(arrival.getString("scheduled"));
			f.scheduled_arrival		= ISO8601ToTimestamp(arrival.getString("actual"));
			
			f.flight_number = json_flight.getJSONObject("flight").getString("iata");
			
			System.out.println("Parsed object");
		}
		
		return result;
	}
	
	
	private Timestamp ISO8601ToTimestamp(String iso8601) {
		return Timestamp.valueOf(iso8601.replace("T", " ").substring(0, 19));
	}
}
