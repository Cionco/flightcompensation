package com.divirad.flightcompensation.monolith.data.api;

import java.util.EventListener;

public interface ParseListener extends EventListener {
	public void jsonParsed(ParseEvent<?> e);
	public void objectParsed(ParseEvent<?> e);
}
