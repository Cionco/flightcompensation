package com.divirad.flightcompensation.monolith.data;

import java.sql.Date;
import java.sql.Timestamp;

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
}
