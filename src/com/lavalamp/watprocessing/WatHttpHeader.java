package com.lavalamp.watprocessing;

import java.util.Arrays;

public class WatHttpHeader {
	
	//TODO fill this out
	private final String[] ignoredHeaders = {
			"Date",
			"Expires",
			"Last-Modified",
			"Cache-Control",
			"Connection",
			"Host",
			"From",
			"Age",
			"Content-Length",
			"Set-Cookie"
	};
	
	private String key;
	private String value;
	
	public WatHttpHeader(String inputKey, String inputValue) {
		key = inputKey;
		value = inputValue;
	}
	
	public String getKey() {
		return key.trim();
	}
	
	public String getValue() {
		return value.trim();
	}
	
	public boolean isServerHeader() {
		return key.equalsIgnoreCase("server");
	}
	
	public boolean isPoweredByHeader() {
		return key.equalsIgnoreCase("x-powered-by");
	}
	
	public boolean isIgnored() {
		return Arrays.asList(ignoredHeaders).contains(key);
	}
	
	@Override
	public String toString() {
		return key.trim() + ":" + value.trim();
	}
	
}
