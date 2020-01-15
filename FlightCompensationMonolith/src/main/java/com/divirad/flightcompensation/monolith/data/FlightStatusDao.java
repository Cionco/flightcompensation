package com.divirad.flightcompensation.monolith.data;

import com.divirad.flightcompensation.monolith.data.database.Database;
import com.divirad.flightcompensation.monolith.data.database.Dao;

public class FlightStatusDao extends Dao<FlightStatus> {
	
	public static FlightStatusDao instance = new FlightStatusDao();
	
	private FlightStatusDao() {
		super(FlightStatus.class);
	}
	
	public int getIdByName(String name) {
		return Database.query("SELECT id FROM " + this.tableName + " WHERE FlightStatus = ?", 
				ps -> ps.setString(1, name),
				rs -> {
					if(rs == null || !rs.next()) return -1;
					return rs.getInt(1);
				});
	}

}
