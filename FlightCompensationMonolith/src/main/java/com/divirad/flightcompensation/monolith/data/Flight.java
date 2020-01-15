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
	
	public Timestamp scheduled_department;
	public Timestamp actaual_department;
	public Timestamp scheduled_arrival;
	public Timestamp actual_department;
	
	public String origin_airport;
	public String destination_airport;
	
	public Flight() {}
	
}
