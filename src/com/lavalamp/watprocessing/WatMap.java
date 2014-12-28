package com.lavalamp.watprocessing;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
//import org.apache.log4j.Logger;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;

public class WatMap {
	
	//private static final Logger LOG = Logger.getLogger(WatMap.class);
	
	protected static enum MAPPERCOUNTER {
		RECORDS_IN,
		NO_SERVER,
		EXCEPTIONS
	}
	
	public static class WatMapper extends MapReduceBase implements Mapper<Text, ArchiveReader, Text, LongWritable> {
		
		private Text outKey = new Text();
		private LongWritable outVal = new LongWritable(1);
		
		public void map(Text key, ArchiveReader value, OutputCollector<Text, LongWritable> output, Reporter reporter) throws IOException {
			WatArchiveRecord war;
			ArrayList<DataEntry> entries;
			for (ArchiveRecord r : value) {
				war = new WatArchiveRecord(r);
				if (WatHelper.validateRecordForDisco(war)) {
					entries = WatHelper.getDataEntriesFromRecord(war);
					for (DataEntry curEntry : entries) {
						outKey.set(curEntry.toString());
						output.collect(outKey, outVal);
					}
				} else {
					outKey.set(WatHelper.getRecordEntry().toString());
					output.collect(outKey, outVal);
				}
			}
		}
		
	}

}
