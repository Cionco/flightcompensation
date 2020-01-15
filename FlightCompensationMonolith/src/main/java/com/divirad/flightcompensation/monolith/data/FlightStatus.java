package com.divirad.flightcompensation.monolith.data;

import com.divirad.flightcompensation.monolith.data.database.MysqlMarker;

@MysqlMarker.TableView(tableName = "Flight_Status", isWholeTable = true)
public final class FlightStatus {

	@MysqlMarker.PrimaryKey
	@MysqlMarker.AutomaticValue
	public int id;
	public String flight_status;
	
	public FlightStatus() {}
}
