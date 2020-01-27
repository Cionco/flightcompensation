package com.divirad.flightcompensation.monolith.data;

import java.sql.Date;
import java.sql.Timestamp;

import org.json.JSONObject;

import com.divirad.flightcompensation.monolith.data.database.MysqlMarker;

@MysqlMarker.TableView(tableName = "Flight", isWholeTable = true)
public final class Flight {

	@MysqlMarker.PrimaryKey
	public Date flight_date;
	@MysqlMarker.PrimaryKey
	public String flight__iata;
	
	public String flight_status;
	
	public Timestamp departure__scheduled;
	public Timestamp departure__actual;
	public Timestamp arrival__scheduled;
	public Timestamp arrival__actual;
	
	public int departure__delay;
	public Integer arrival__delay;
	
	public String departure__iata;
	public String arrival__iata;
	
	public Flight() {}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(flight_date);
		sb.append(" ");
		sb.append(flight__iata);
		return sb.toString();
	}
	
	public JSONObject toJson() {
		JSONObject j = new JSONObject();
		JSONObject flight = new JSONObject();
		JSONObject departure = new JSONObject();
		JSONObject arrival = new JSONObject();
		j.put("flight_date", flight_date);
		j.put("flight_status", flight_status);
		flight.put("iata", flight__iata);
		departure.put("scheduled", departure__scheduled);
		departure.put("actual", departure__actual);
		departure.put("delay", departure__delay);
		departure.put("iata", departure__iata);
		arrival.put("scheduled", arrival__scheduled);
		arrival.put("actual", arrival__actual);
		arrival.put("delay", arrival__delay);
		arrival.put("iata", arrival__iata);
		j.put("flight", flight);
		j.put("departure", departure);
		j.put("arrival", arrival);
		return j;
	}
}
