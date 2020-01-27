package com.divirad.flightcompensation.micro.calculator.data;

import java.sql.Date;

import org.json.JSONObject;

import com.divirad.flightcompensation.monolith.data.Flight;

public class FlightDao extends Dao<Flight> {

	private FlightDao() {
		super(Flight.class);
	}

	public static FlightDao instance = new FlightDao();
	
	public Flight getFlight(String flight_number, Date date) {
		Flight f = new Flight();
		f.flight__iata = flight_number;
		f.flight_date = date;
		return get(f);
	}
}
