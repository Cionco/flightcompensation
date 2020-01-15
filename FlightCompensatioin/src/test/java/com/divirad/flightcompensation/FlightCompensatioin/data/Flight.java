package com.divirad.flightcompensation.FlightCompensatioin.data;

import java.sql.Date;

import com.divirad.flightcompensation.FlightCompensatioin.data.database.MysqlMarker;

@MysqlMarker.TableView(tableName = "Flight", isWholeTable = true)
public final class Flight {

	@MysqlMarker.PrimaryKey
	public Date flight_date;
	@MysqlMarker.PrimaryKey
	public String flight_number;
	
	public int flight_status;
	
	public Date scheduled_department;
	public Date actaual_department;
	public Date scheduled_arrival;
	public Date actual_department;
	
	public String origin_airport;
	public String destination_airport;
	
	public Flight() {}
	
}
