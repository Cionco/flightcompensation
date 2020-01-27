package com.divirad.flightcompensation.micro.calculator.data;

import com.divirad.flightcompensation.monolith.data.Airport;

public class AirportDao extends Dao<Airport> {

	public static AirportDao instance = new AirportDao();
	
	private AirportDao() {
		super(Airport.class);
	}

	public Airport get(String iata) {
		Airport a = new Airport();
		a.iata_code = iata;
		return get(a);
	}
}
