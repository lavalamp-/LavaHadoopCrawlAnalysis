package com.lavalamp.watprocessing;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapred.lib.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.lavalamp.warc.WARCFileInputFormat;

public class HadoopRunner extends Configured implements Tool {

	public int run(String[] args) throws Exception {
		
		// String fileInputPath = "/home/ubuntu/hadoop/CC-MAIN-20140313024506-00098-ip-10-183-142-35.ec2.internal.warc.wat.gz";
		// String fileInputPath = "/home/ubuntu/hadoop/CC-MAIN-20140313024506-00099-ip-10-183-142-35.ec2.internal.warc.wat.gz";
		// String fileOutputPath = "/home/ubuntu/hadoop_output_9/";
		String fileInputPath = "s3n://aws-publicdatasets/common-crawl/crawl-data/CC-MAIN-2014-49/segments/1416400372202.67/wat/*.warc.wat.gz";
		String fileOutputPath = "s3n://lava-common-crawl/path_test_1/";
		
		JobConf conf = new JobConf(WatMap.class);
		conf.setJobName("DiscoDonny Common Crawl Processor");
		conf.setJarByClass(com.lavalamp.watprocessing.HadoopRunner.class);
		System.out.println("Job jar: " + conf.getJar());
		
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(LongWritable.class);
		
		conf.setMapperClass(WatMap.WatMapper.class);
		conf.setReducerClass(LongSumReducer.class);
		
		conf.setInputFormat(WARCFileInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(conf, new Path(fileInputPath));
		FileOutputFormat.setOutputPath(conf, new Path(fileOutputPath));
		
		JobClient.runJob(conf);
		return 0;
		
	}
	
	public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(),  new HadoopRunner(), args);
		System.exit(res);
		
	}
	
}
