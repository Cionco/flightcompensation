package com.divirad.flightcompensation.monolith.data.api;

import java.util.ArrayList;

public class ParseEvent<T> {

	private ArrayList<T> result;
	
	private Class<T> resource;
	
	public ParseEvent(Class<T> resource) {
		this.resource = resource;
	}
	
	public ArrayList<T> getResult() {
		return result;
	}
	
	public Class<T> getResource() {
		return resource;
	}
	
	public void setResult(ArrayList<T> result) {
		this.result = result;
	}
}
