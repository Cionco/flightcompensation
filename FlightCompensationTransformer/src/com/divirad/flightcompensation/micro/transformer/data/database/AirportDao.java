package com.divirad.flightcompensation.micro.transformer.data.database;

import java.util.ArrayList;

import com.divirad.flightcompensation.monolith.data.Airport;

public class AirportDao extends Dao<Airport> {
	public static AirportDao instance = new AirportDao();
	
	private AirportDao() {
		super(Airport.class);
	}
	
	public void updateAirports(ArrayList<Airport> airports) {
		for(Airport a : airports) {
			if(get(a.iata_code) != null) {
				System.out.println("Updating " + a.iata_code);
				update(a);
			} else {
				System.out.println("Inserting " + a.iata_code);
				insert(a);
			}
		}
	}
	
	public Airport get(String iata) {
		Airport a = new Airport();
		a.iata_code = iata;
		return select(a);
	}
	
}
