package com.divirad.flightcompensation.monolith.data.api;

public class FlightDataLoader {

	public static FlightDataLoader instance;
	
	private FlightDataLoader() {}
	
	public static FlightDataLoader getInstance() {
		if(instance == null) instance = new FlightDataLoader();
		return instance;
	}
	
	
}
