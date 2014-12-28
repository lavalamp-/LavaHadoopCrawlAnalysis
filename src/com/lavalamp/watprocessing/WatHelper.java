package com.lavalamp.watprocessing;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.ArrayList;

public class WatHelper {

	public static boolean validateRecordForDisco(WatArchiveRecord inputRecord) {
		
		//TODO there has to be more to validate this with than just checking for response
		//TODO add some sort of handling for responses that do not have valid URL strings
		
		return inputRecord.isResponse() && inputRecord.hasValidUrlString();
		
	}
	
	public static ArrayList<DataEntry> getDataEntriesFromRecord(WatArchiveRecord inputRecord) {
		
		ArrayList<DataEntry> toReturn = new ArrayList<DataEntry>();
		
		String serverClean = inputRecord.getShortenedServerType();
		ArrayList<String> urlParts = WatHelper.processPathIntoParts(inputRecord.getUrl().getPath());
		
		for (String curPart : urlParts) {
			toReturn.add(new DataEntry(DataEntry.SERVER_PATH_ENTRY, serverClean, curPart));
		}

		toReturn.add(new DataEntry(DataEntry.SERVER_NAME_ENTRY, serverClean));
		toReturn.add(WatHelper.getRecordEntry());
		
		return toReturn;
		
	}
	
	public static DataEntry getRecordEntry() {
		return new DataEntry(DataEntry.RECORD_ENTRY);
	}
	
	public static String getHashFromHeaders(ArrayList<WatHttpHeader> inputHeaders) {
		
		String toDigest = "";
		
		for (WatHttpHeader curHeader : inputHeaders) {
			toDigest += curHeader.toString() + ":";
		}
		
		return WatHelper.getMD5Hash(toDigest);
		
	}
	
	public static String getMD5Hash(String inputString) {
		try {
			byte[] messageBytes = inputString.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(messageBytes);
			return WatHelper.byteArrayToHex(digest);
		} catch (Exception ex) {
			return "";
		}
	}
	
	public static String byteArrayToHex(byte[] a) {
	   StringBuilder sb = new StringBuilder();
	   for(byte b: a)
	      sb.append(String.format("%02x", b&0xff));
	   return sb.toString();
	}
	
	public static String cleanPathComponent(String inputPiece) {

		@SuppressWarnings("deprecation")
		String toTest = URLDecoder.decode(inputPiece);

		String testForInt = toTest.replace("-", "").replace("_", "");

		try {
			Integer.parseInt(testForInt);
			return "[[INTEGER]]";
		} catch(NumberFormatException e) {

		}

		if (toTest.length() >= 3) {
			String testForInt2 = toTest.subSequence(1, toTest.length()).toString();
			try {
				Integer.parseInt(testForInt2);
				return "[[INTEGER]]";
			} catch(NumberFormatException e) {

			}
		}

		if (toTest.length() >= 16) {
			return "[[OVER_SIXTEEN_LENGTH]]";
		} else if (WatHelper.doesStringContainLotsOfNumbers(testForInt)) {
			return "[[MAJORITY_INTS]]";
		// Depending on the success of running this code as is, may make more sense to be more granular with separator descriptions
		// } else if (toTest.split("-").length >= 5) {
		// 	return "[[WORDS_WITH_DASH_SEPARATORS]]";
		// } else if (toTest.split("_").length >= 5) {
		// 	return "[[WORDS_WITH_UNDER_SEPARATORS]]";
		// } else if (toTest.split(",").length >= 5) {
		// 	return "[[WORDS_WITH_COMMA_SEPARATORS]]";
		// } else if (toTest.split("\\+").length >= 5) {
		// 	return "[[WORDS_WITH_PLUS_SEPARATORS]]";
		// } else if (toTest.split("\\.").length >= 5) {
		// 	return "[[WORDS_WITH_PERIOD_SEPARATORS]]";
		} else {
			return toTest;
		}

	}
	
	public static ArrayList<String> processPathIntoParts(String inputPath) {

		ArrayList<String> toReturn = new ArrayList<String>();

		// Handle edge cases
		if (inputPath.equals("/") || inputPath.equals("")) {
			return toReturn;
		}

		String[] pathPieces = inputPath.split("/");
		String curPiece;

		if (pathPieces.length >= 2) {
			for (int i=1; i < pathPieces.length; i++) {
				curPiece = WatHelper.cleanPathComponent(pathPieces[i]);
				if (i == pathPieces.length - 1) {
					toReturn.add(curPiece);
				} else {
					toReturn.add("/" + curPiece + "/");
				}
			}
		}

		return toReturn;

	}
	
	public static boolean doesStringContainLotsOfNumbers(String inputString) {

		@SuppressWarnings("unused")
		int numChars = 0;
		int numInts = 0;
		char curChar;
		
		for (int i=0; i<inputString.length(); i++) {
			curChar = inputString.charAt(i);
			if (curChar >= 48 && curChar <= 57) {
				numInts += 1;
			} else {
				numChars += 1;
			}
		}
		
		return (numInts > 0.5 * inputString.length());

	}
	
}

/*
 *
 * OLD CODE - KEEPING FOR REFERENCE
 * 
 * ArrayList<WatHttpHeader> headers = inputRecord.getSanitizedHttpHeaders();
 * String hash = WatDiscoHelper.getHashFromHeaders(headers);
 * ArrayList<WatUrlPair> urlPairs = WatUrlPair.processPathIntoPairs(inputRecord.getUrl().getPath());
		
 * Check for error conditions
 * if (hash.isEmpty()) {
 * 	toReturn.add(new DiscoDataEntry(DiscoDataEntry.DISCO_ERROR_ENTRY, "Hash for record was empty!"));
 * 	return toReturn;
 * }
		
 * for (WatUrlPair curPair : urlPairs) {
 * 	toReturn.add(new DiscoDataEntry(DiscoDataEntry.DISCO_SERVER_ENTRY, server, curPair));
 * 	toReturn.add(new DiscoDataEntry(DiscoDataEntry.DISCO_HASH_ENTRY, hash, curPair));
 * }
		
 * for (WatHttpHeader curHeader : inputRecord.getAllHttpHeaders()) {
 * 	toReturn.add(new DiscoDataEntry(DiscoDataEntry.DISCO_HEADER_ENTRY, curHeader));
 * }
		
 * toReturn.add(new DiscoDataEntry(DiscoDataEntry.DISCO_RECORD_ENTRY));
 * 
 * */
