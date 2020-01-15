package com.divirad.flightcompensation.monolith.data;

import java.sql.Date;
import java.sql.Timestamp;

import com.divirad.flightcompensation.monolith.data.database.MysqlMarker;

@MysqlMarker.TableView(tableName = "Flight", isWholeTable = true)
public final class Flight {

	@MysqlMarker.PrimaryKey
	public Date flight_date;
	@MysqlMarker.PrimaryKey
	public String flight_number;
	
	public int flight_status;
	
	public Timestamp scheduled_departure;
	public Timestamp actual_departure;
	public Timestamp scheduled_arrival;
	public Timestamp actual_arrival;
	
	public int departure_delay;
	public int arrival_delay;
	
	public String origin_airport;
	public String destination_airport;
	
	public Flight() {}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(flight_date);
		sb.append(" ");
		sb.append(flight_number);
		return sb.toString();
	}
}
