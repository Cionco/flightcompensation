package com.divirad.flightcompensation.monolith.data.database;

import com.divirad.flightcompensation.monolith.data.FlightStatus;

public class FlightStatusDao extends Dao<FlightStatus> {
	
	public static FlightStatusDao instance = new FlightStatusDao();
	
	private FlightStatusDao() {
		super(FlightStatus.class);
	}
	
	public int getIdByName(String name) {
		return Database.query("SELECT id FROM " + this.tableName + " WHERE flight_status = ?", 
				ps -> ps.setString(1, name),
				rs -> {
					if(rs == null || !rs.next()) return -1;
					return rs.getInt(1);
				});
	}

}
