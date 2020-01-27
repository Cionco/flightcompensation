package com.divirad.flightcompensation.monolith.data;

import org.json.JSONObject;

import com.divirad.flightcompensation.monolith.data.database.MysqlMarker;

@MysqlMarker.TableView(tableName="Airport", isWholeTable=true)
public final class Airport {

	@MysqlMarker.PrimaryKey
	public String iata_code;
	public String icao_code;
	public String airport_name;
	public double latitude;
	public double longitude;
	public String timezone;
	public int gmt;
	
	public Airport() {}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(iata_code);
		sb.append(" ");
		sb.append(airport_name);
		sb.append(" gmt" + intWithSign(gmt));
		return sb.toString();
	}
	
	public JSONObject toJson() {
		JSONObject j = new JSONObject();
		j.put("iata_code", iata_code);
		j.put("icao_code", icao_code);
		j.put("airport_name", airport_name);
		j.put("latitude", latitude);
		j.put("longitude", longitude);
		j.put("timezone", timezone);
		j.put("gmt", gmt);
		return j;
	}
	
	private String intWithSign(int i) {
		return i > 0 ? "+" + i : Integer.toString(i);
	}
}
