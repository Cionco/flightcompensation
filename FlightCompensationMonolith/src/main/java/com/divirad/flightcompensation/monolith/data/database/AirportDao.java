package com.divirad.flightcompensation.monolith.data.database;

import java.util.ArrayList;

import com.divirad.flightcompensation.monolith.data.Airport;

import com.divirad.flightcompensation.monolith.lib.StreamThread;

public class AirportDao extends Dao<Airport> {

	public static AirportDao instance = new AirportDao();
	
	private AirportDao() {
		super(Airport.class);
	}
	
	public void updateAirports(ArrayList<Airport> airports) {
		for(Airport a : airports) {
			if(get(a.iata_code) != null) {
				StreamThread.currentThread().getOut().println("Updating " + a.iata_code);
				update(a);
			} else {
				StreamThread.currentThread().getOut().println("Inserting " + a.iata_code);
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
