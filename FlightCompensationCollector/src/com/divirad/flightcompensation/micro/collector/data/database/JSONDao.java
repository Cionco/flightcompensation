package com.divirad.flightcompensation.micro.collector.data.database;

import java.sql.Date;

import org.json.JSONArray;

import com.divirad.flightcompensation.micro.collector.server.JSON;
import com.divirad.flightcompensation.monolith.data.database.Dao;

public class JSONDao extends Dao<JSON>{

	public static JSONDao instance = new JSONDao();
	
	private JSONDao() {
		super(JSON.class);
	}
	
	public void store(JSONArray data, int jsons, String type, Date date) {
		JSON json = new JSON();
		json.date = date;
		json.json = data.toString();
		json.jsons = jsons;
		json.type = type;
		insert(json);
	}

	public JSON get(String type, Date date) {
		JSON j = new JSON();
		j.date = date;
		j.type = type;
		return select(j);
	}
}
