package com.divirad.flightcompensation.micro.collector.server;

import java.sql.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.divirad.flightcompensation.monolith.data.database.MysqlMarker;;

@MysqlMarker.TableView(tableName = "JSON", isWholeTable = true)
public final class JSON {

	@MysqlMarker.PrimaryKey
	public Date date;
	@MysqlMarker.PrimaryKey
	public String type;
	
	public String json;
	public int jsons;
	
	
	public JSONObject toJson() {
		JSONObject j = new JSONObject();
		j.put("date", date.toString());
		j.put("type", type);
		j.put("original_requests", jsons);
		j.put("data", new JSONArray(json));
		return j;
	}
}
