package com.divirad.flightcompensation.micro.transformer.data.database;

import java.sql.Date;
import java.util.ArrayList;

import com.divirad.flightcompensation.monolith.data.Flight;

public final class FlightDao extends Dao<Flight>{

	public static final FlightDao instance = new FlightDao();
	
	private FlightDao() {
		super(Flight.class);
	}

	public Flight getFlight(String flight_number, Date flight_date) {
		Flight f = new Flight();
		f.flight__iata = flight_number;
		f.flight_date = flight_date;
		return select(f);
	}
	
	public ArrayList<Flight> getFlights(Date flight_date) {
		return Database.query("SELECT * FROM Flight WHERE flight_date = ?", 
				ps -> ps.setDate(1, flight_date), 
				rs -> convAllInResultSet(rs));
	}
	
	public void storeFlights(ArrayList<Flight> flights) {
		insertAll(flights);
	}
}
