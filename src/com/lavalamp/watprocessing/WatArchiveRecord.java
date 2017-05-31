package com.lavalamp.watprocessing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;
import org.json.JSONObject;

public class WatArchiveRecord {

	public static final int WAT_REQUEST_TYPE = 0;
	public static final int WAT_RESPONSE_TYPE = 1;
	public static final int WAT_OTHER_TYPE = 2;
	public static final int WAT_UNRECOGNIZED_MIME = 3;
	public static final String WAT_NONE_SERVER_TYPE = "NONE";
	
	@SuppressWarnings("unused")
	private ArchiveRecord record;
	private String recordUrlString;
	private String mimeType;
	private JSONObject recordBody;
	private JSONObject payloadMetadata;
	private URL recordUrl;
	private String serverType;
	private ArrayList<WatHttpHeader> httpHeaders;
	
	public WatArchiveRecord(ArchiveRecord inputRecord) throws IOException {
		
		record = inputRecord;
		ArchiveRecordHeader header = inputRecord.getHeader();
	    recordUrlString = header.getUrl();
	    if (hasValidUrlString()) {
	    	//TODO refactor this, so ugly
	    	try {
	    		recordUrl = new URL(recordUrlString);
	    	} catch (MalformedURLException ex) {
	    		
	    	}
	    }
		mimeType = header.getMimetype();
			if (containsJSON()) {
			byte[] contentBytes = IOUtils.toByteArray(inputRecord, inputRecord.available());
		    recordBody = new JSONObject(new String(contentBytes));
		    payloadMetadata = recordBody.getJSONObject("Envelope").getJSONObject("Payload-Metadata");
		}
		
	}
	
	public ArrayList<WatHttpHeader> getAllHttpHeaders() {
		
		if (httpHeaders == null) {
			
			httpHeaders = new ArrayList<WatHttpHeader>();
			ArrayList<String> headerKeys = new ArrayList<String>();
			JSONObject headerObject;
			
			if (isRequest()) {
				headerObject = payloadMetadata.getJSONObject("HTTP-Request-Metadata").getJSONObject("Headers");
			} else if (isResponse()) {
				headerObject = payloadMetadata.getJSONObject("HTTP-Response-Metadata").getJSONObject("Headers");
			} else {
				//TODO figure out a better way to handle this
				return httpHeaders;
			}
			
			@SuppressWarnings("rawtypes")
			Iterator it = headerObject.keys();
			
			while(it.hasNext()) {
				headerKeys.add(it.next().toString());
			}
			
			Collections.sort(headerKeys);
			
			for (String curHeader : headerKeys) {
				httpHeaders.add(new WatHttpHeader(curHeader, headerObject.getString(curHeader)));
			}
			
		}
		
		return httpHeaders;
		
	}
	
	public ArrayList<WatHttpHeader> getSanitizedHttpHeaders() {
		
		ArrayList<WatHttpHeader> allHeaders = getAllHttpHeaders();
		ArrayList<WatHttpHeader> sanitizedHeaders = new ArrayList<WatHttpHeader>();
		
		for (WatHttpHeader curHeader : allHeaders) {
			if (!curHeader.isIgnored()) {
				sanitizedHeaders.add(curHeader);
			}
		}
		
		return sanitizedHeaders;
		
	}
	
	public boolean hasValidUrlString() {
		try {
			@SuppressWarnings("unused")
			URL testUrl = new URL(recordUrlString);
			return true;
		} catch (MalformedURLException ex) {
			return false;
		}
	}
	
	public boolean isResponse() {
		return (getRecordType() == WAT_RESPONSE_TYPE);		
	}
	
	public boolean isRequest() { 
		return (getRecordType() == WAT_REQUEST_TYPE);
	}
	
	public String getServerType() {
		
		if (serverType == null) {
			for (WatHttpHeader curHeader : getAllHttpHeaders()) {
				if (curHeader.isServerHeader()) {
					serverType = curHeader.getValue().trim().toLowerCase();
					break;
				}
				//TODO fix this - shoddy logic
				serverType = WAT_NONE_SERVER_TYPE;
			}
		}
		
		return serverType;
		
	}

	public String getShortenedServerType() {

		String inputServerType = getServerType();

		if (inputServerType == null) {
			return "null_server";
		}

		String toAnalyze = inputServerType;

		if (toAnalyze.contains("apache")) {
			if (toAnalyze.contains("(win32)") || toAnalyze.contains("(win64)")) {
				return "apache_windows";
			} else if (toAnalyze.contains("unix") || toAnalyze.contains("centos") || toAnalyze.contains("freebsd") || toAnalyze.contains("linux") || toAnalyze.contains("ubuntu") || toAnalyze.contains("red hat") || toAnalyze.contains("debian") || toAnalyze.contains("fedora")) {
				return "apache_unix";
			} else {
				return "apache_generic";
			}
		} else if (toAnalyze.startsWith("ats")) {
			return "ats";
		} else if (toAnalyze.contains("nginx")) {
			return "nginx";
		} else if (toAnalyze.contains("gunicorn")) {
			return "gunicorn";
		} else if (toAnalyze.contains("ibm_http_server")) {
			return "ibm_http_server";
		} else if (toAnalyze.startsWith("iis") || toAnalyze.startsWith("microsoft-iis") || toAnalyze.startsWith("microsoft iis")) {
			return "iis";
		} else if (toAnalyze.startsWith("jetty")) {
			return "jetty";
		} else if (toAnalyze.startsWith("lighttpd")) {
			return "lighttpd";
		} else if (toAnalyze.startsWith("litespeed")) {
			return "litespeed";
		} else if (toAnalyze.startsWith("lotus-domino")) {
			return "lotus_domino";
		} else if (toAnalyze.startsWith("oracle-application") || toAnalyze.startsWith("oracle application") || toAnalyze.startsWith("oracle")) {
			return "oracle-application-server";
		} else if (toAnalyze.startsWith("resin")) {
			return "resin";
		} else if (toAnalyze.startsWith("sun")) {
			return "sun-web-server";
		} else if (toAnalyze.startsWith("websphere")) {
			return "websphere";
		} else if (toAnalyze.startsWith("demandware")) {
			return "demandware";
		} else if (toAnalyze.startsWith("microsoft-httpapi")) {
			return "microsoft-httpapi";
		} else if (toAnalyze.startsWith("pws")) {
			return "pws";
		} else if (toAnalyze.startsWith("4d")) {
			return "4d";
		} else if (toAnalyze.startsWith("aleph")) {
			return "aleph";
		} else if (toAnalyze.startsWith("arc-reactor")) {
			return "arc-reactor";
		} else if (toAnalyze.startsWith("cherokee")) {
			return "cherokee";
		} else if (toAnalyze.startsWith("glassfish")) {
			return "glassfish";
		} else if (toAnalyze.startsWith("ning")) {
			return "ning";
		} else if (toAnalyze.startsWith("openresty")) {
			return "openresty";
		} else if (toAnalyze.startsWith("thin")) {
			return "thin";
		} else if (toAnalyze.startsWith("zope")) {
			return "zope";
		} else if (toAnalyze.startsWith("gwiseguy")) {
			return "gwiseguy";
		} else {
			return "misc_server";
			// return toAnalyze;
		}

	}
	
	public int getRecordType() {
		if (containsJSON()) {
			if (payloadMetadata.has("HTTP-Response-Metadata")) {
				return WAT_RESPONSE_TYPE;
			} else if (payloadMetadata.has("HTTP-Request-Metadata")) {
				return WAT_REQUEST_TYPE;
			} else {
				return WAT_OTHER_TYPE;
			}
		} else {
			return WAT_UNRECOGNIZED_MIME;
		}
	}
	
	public boolean containsJSON() {
		return (mimeType.equals("application/json"));
	}
	
	public String getUrlString() {
		return recordUrlString;
	}
	
	public URL getUrl() {
		return recordUrl;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
}
