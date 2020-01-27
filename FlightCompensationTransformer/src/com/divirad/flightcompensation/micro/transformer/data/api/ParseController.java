package com.divirad.flightcompensation.micro.transformer.data.api;

import java.util.ArrayList;

import com.divirad.flightcompensation.micro.transformer.data.database.AirportDao;
import com.divirad.flightcompensation.micro.transformer.data.database.FlightDao;
import com.divirad.flightcompensation.monolith.data.Airport;
import com.divirad.flightcompensation.monolith.data.Flight;
import com.divirad.flightcompensation.monolith.data.api.ParseEvent;
import com.divirad.flightcompensation.monolith.data.api.ParseListener;

public class ParseController implements ParseListener {

	@SuppressWarnings("unchecked")
	@Override
	public void jsonParsed(ParseEvent<?> e) {
		if(e.getResource() == Flight.class)
			FlightDao.instance.storeFlights(typifyArrayList((ArrayList<Object>) e.getResult()));
		else if(e.getResource() == Airport.class)
			AirportDao.instance.updateAirports(typifyArrayList((ArrayList<Object>)e.getResult()));
	}

	@Override
	public void objectParsed(ParseEvent<?> e) {
		System.out.println("Parsed object: " + e.getResult().get(0));
	}
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> typifyArrayList(ArrayList<Object> objects) {
		ArrayList<T> result = new ArrayList<>();
		for(Object o : objects)
			result.add((T) o);
		return result;
	}

}
