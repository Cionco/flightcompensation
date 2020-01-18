package com.divirad.flightcompensation.monolith.data.api;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.database.FlightStatusDao;

public final class Parser {

	private static Parser instance;
	
	private EventListenerList listeners = new EventListenerList();
	
	private Parser() {}
	
	public static Parser getInstance() {
		if(instance == null) instance = new Parser();
		return instance;
	}
	
	public void addParseListener(ParseListener l) {
		listeners.add(ParseListener.class, l);
	}
	
	public void removeParseListener(ParseListener l) {
		listeners.remove(ParseListener.class, l);
	}
	
	protected void fireJsonParsed(ArrayList<Flight> flights) {
		ParseEvent e = new ParseEvent();
		e.setResult(flights);
		ParseListener[] listeners = getParseListeners();
		for(ParseListener l : listeners) {
			l.jsonParsed(e);
		}
	}
	
	private ParseListener[] getParseListeners() {
		return (ParseListener[]) listeners.getListeners(ParseListener.class);
	}
	
	public ArrayList<Flight> parseFlights(JSONArray json_flights) {
		ArrayList<Flight> result = new ArrayList<>();
		
		for(Object flight : json_flights) {
			try {
				JSONObject json_flight = (JSONObject) flight;
				Flight f = new Flight();
				f.flight_date = Date.valueOf(json_flight.getString("flight_date"));
				f.flight_status = FlightStatusDao.instance.getIdByName(json_flight.getString("flight_status"));
				f.flight_number = json_flight.getJSONObject("flight").getString("iata");
				
				JSONObject departure = json_flight.getJSONObject("departure");
				JSONObject arrival = json_flight.getJSONObject("arrival");
				f.origin_airport = departure.getString("iata");
				f.scheduled_departure	= ISO8601ToTimestamp(departure.getString("scheduled"));
				//if(!(departure.get("actual") instanceof String)) System.out.println("OH BOY " + json_flight.getJSONObject("flight").getString("iata"));
				f.actual_departure		= departure.get("actual") instanceof String ? ISO8601ToTimestamp(departure.getString("actual")) : null;
				f.departure_delay = departure.get("delay") instanceof Integer ? departure.getInt("delay") : 0;
				f.destination_airport = arrival.getString("iata");
				f.scheduled_arrival		= ISO8601ToTimestamp(arrival.getString("scheduled"));
				//System.out.println(arrival.get("actual").getClass().getName());
				f.actual_arrival		= arrival.get("actual") instanceof String ? ISO8601ToTimestamp(arrival.getString("actual")) : null;
				f.arrival_delay = arrival.get("delay") instanceof Integer ? arrival.getInt("delay") : null;
				
				
				System.out.println("Parsed object: " + f);
				result.add(f);
			} catch(JSONException e) {
				//No iata -> cargo flights
				System.out.println("Error while parsing: " + e.getMessage());
			}
		}
		
		fireJsonParsed(result);		
		return result;
	}
	
	
	private Timestamp ISO8601ToTimestamp(String iso8601) {
		return Timestamp.valueOf(iso8601.replace("T", " ").substring(0, 19));
	}
}
