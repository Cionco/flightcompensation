package com.divirad.flightcompensation.FlightCompensatioin.data;

import java.sql.Date;

import com.divirad.flightcompensation.FlightCompensatioin.data.database.Dao;

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

}
