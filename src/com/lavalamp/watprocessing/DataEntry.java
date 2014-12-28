package com.lavalamp.watprocessing;

public class DataEntry {
	
	public static final String RECORD_ENTRY = "00";
	public static final String SERVER_NAME_ENTRY = "01";
	public static final String SERVER_PATH_ENTRY = "02";
	public static final String RECORD_SEPARATOR = "_';)_";
	
	private String type;
	private String server;
	private String info;
		
	public DataEntry(String inputType, String inputServer, String inputInfo) {

		type = inputType;
		server = inputServer;
		info = inputInfo;

	}
	
	public DataEntry(String inputType, String inputInfo) {
		
		type = inputType;
		info = inputInfo;
		
	}
	
	public DataEntry(String inputType) {
		
		type = inputType;
		
	}
	
	@Override
	public String toString() {
		
		//TODO good god shoot this thing and make a new one
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("< ");
		
		if (type.equals(RECORD_ENTRY)) {
			sb.append(RECORD_ENTRY);
			sb.append(RECORD_SEPARATOR);
			sb.append("RECORD_PROCESSED");
		}else if (type.equals(SERVER_NAME_ENTRY)) {
			sb.append(SERVER_NAME_ENTRY);
			sb.append(RECORD_SEPARATOR);
			sb.append(info);
		} else if (type.equals(SERVER_PATH_ENTRY)) {
			sb.append(SERVER_PATH_ENTRY);
			sb.append(RECORD_SEPARATOR);
			sb.append(server);
			sb.append(RECORD_SEPARATOR);
			sb.append(info);
		}
		
		sb.append(" >");
		
		return sb.toString();
		
	}
	
}
