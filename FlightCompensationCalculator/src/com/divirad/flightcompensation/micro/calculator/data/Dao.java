package com.divirad.flightcompensation.micro.calculator.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.divirad.flightcompensation.monolith.data.database.MysqlMarker;

public class Dao<T> {

	protected String resourceName;
	
	protected Class<T> cls;
	protected Constructor<T> constructor;
	
	protected Field[] allFields;
	protected Field[] primaryKeys;
	
	protected String primary_list;
	
	protected String query_get;
	
	protected String resource;
	
	public Dao(Class<T> cls) {
		this.cls = cls;
		if(!Modifier.isFinal(cls.getModifiers()))
			throw new IllegalArgumentException("Can't use class: must be final");
		if(cls.getSuperclass() != Object.class)
			throw new IllegalArgumentException("Can't use class: must not extend another class");
		
		try {
			this.constructor = cls.getConstructor();
		} catch(NoSuchMethodException e) {
			throw new IllegalArgumentException("Can't use class: No default constructor");
		}
		analyzeFields();
		makeStrings();
	}
	
	private void analyzeFields() {
		List<Field> primaryKeys = new ArrayList<>();
		List<Field> allFields = new ArrayList<>();
		for(Field field : this.cls.getDeclaredFields()) {
			if(field.getAnnotation(MysqlMarker.IgnoreField.class) != null)
				continue;
			allFields.add(field);
			if(field.getAnnotation(MysqlMarker.PrimaryKey.class) != null)
				primaryKeys.add(field);
		}
		this.primaryKeys = primaryKeys.toArray(new Field[0]);
		this.allFields = allFields.toArray(new Field[0]);
	}
	
	private void makeStrings() {
		this.resource = this.cls.getSimpleName().toLowerCase() + "s";
		
		StringJoiner primary_joiner = new StringJoiner("&", "?", "");
		for(Field field : this.primaryKeys)
			primary_joiner.add(field.getName() + "=%");
		
		this.primary_list = primary_joiner.toString();
		
		this.query_get = this.resource + this.primary_list;
	}
	
	protected ArrayList<T> convAllInResult(JSONObject o) {
		try { if(o.getInt("response_code") == 500) return null; } catch(JSONException e) {}
		JSONArray a;
		try {
			
			a = o.getJSONArray("data");
		} catch(JSONException e) {
			e.printStackTrace();
			return null;
		}
		ArrayList<T> result = new ArrayList<>();
		for(Object obj : a) {
			result.add(convResult((JSONObject) obj));
		}
		return result;
		
	}
	
	protected T convResult(JSONObject j) {
		try {
			try { if(j.getInt("response_code") == 500) return null; } catch(JSONException e) {}
			T t = this.constructor.newInstance();
			
			for(Field f : this.allFields) {
				JSONObject base = j;
				String[] path = f.getName().split("__");
				for(int i = 0; i < path.length - 1; i++) {
					base = base.getJSONObject(path[i]);
				}
				String name = path[path.length - 1];
				
				if (f.getType() == int.class || f.getType() == Integer.class)
					try { f.set(t, base.getInt(name));} catch(JSONException e) {}
				else if (f.getType() == boolean.class || f.getType() == Boolean.class)
					try { f.set(t, base.getBoolean(name));} catch(JSONException e) {}
	            else if (f.getType() == long.class || f.getType() == Long.class)
	            		try { f.set(t, base.getLong(name));} catch(JSONException e) {}
	            else if (f.getType() == double.class || f.getType() == Double.class)
	            		try { f.set(t, base.getDouble(name));} catch(JSONException e) {}
	            else if (f.getType() == String.class)
	            		f.set(t, base.getString(name));
	            else if (f.getType() == Date.class)
	            		try { f.set(t, Date.valueOf(base.getString(name)));} catch(JSONException e) {}
	            else if (f.getType() == Time.class)
	            		try { f.set(t, Time.valueOf(base.getString(name)));} catch(JSONException e) {}
	            else if (f.getType() == Timestamp.class)
	            		try { f.set(t, ISO8601ToTimestamp(base.getString(name)));} catch(JSONException e) {}
	            else
	                throw new IllegalStateException("Unknown type of field: " + f.getName() + ", " + f.getType().getName());
			}
			return t;
		} catch(InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected String setParams(String resource, T data, Field[] fields) {
		try {
			for(int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				resource = resource.replace(f.getName() + "=%", f.getName() + "=" + f.get(data));
			}
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		}
		return resource;
	}
	
	protected T get(T data) {
		return API.get(this.query_get, 
				url -> setParams(url, data, this.primaryKeys), 
				o -> convResult(o));
	}
	
	private Timestamp ISO8601ToTimestamp(String iso8601) {
		return Timestamp.valueOf(iso8601.replace("T", " ").substring(0, 19));
	}
}
