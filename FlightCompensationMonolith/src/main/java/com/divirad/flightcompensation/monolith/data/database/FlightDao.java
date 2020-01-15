package com.divirad.flightcompensation.monolith.data.database;

import java.sql.Date;
import java.util.ArrayList;

import com.divirad.flightcompensation.monolith.data.Flight;

public class FlightDao extends Dao<Flight> {

	public static final FlightDao instance = new FlightDao();
	
	private FlightDao() {
		super(Flight.class);
	}

	public Flight getFlight(Date flight_date, String flight_number) {
		Flight f = new Flight();
		f.flight_date = flight_date;
		f.flight_number = flight_number;
		return select(f);
	}
	
	public void storeFlights(ArrayList<Flight> flights) {
		for(Flight f : flights) {
			insert(f);
		}
	}

}
