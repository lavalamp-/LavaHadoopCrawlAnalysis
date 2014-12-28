package com.lavalamp.warc;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.archive.io.ArchiveReader;

/**
 * Minimal implementation of FileInputFormat for WARC files.
 * Hadoop is told that splitting these compressed files is not possible.
 *
 * @author Stephen Merity (Smerity)
 */

public class WARCFileInputFormat extends FileInputFormat<Text, ArchiveReader> {

	@Override
	public RecordReader<Text, ArchiveReader> getRecordReader(InputSplit split,
			JobConf conf, Reporter reporter) throws IOException {
		return new WARCFileRecordReader(split, conf, reporter);
	}
	
	@Override
	protected boolean isSplitable(FileSystem fs, Path filename) {
		return false;
	}
	
}
