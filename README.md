LavaHadoopCrawlAnalysis
=======================

Code used for running Hadoop jobs on Amazon EC2 to analyze Common Crawl data set. Building this code requires [Apache Ant](http://ant.apache.org/).

To use this code, do the following:

1. Clone the repository
2. Update the file at `src/com/lavalamp/watprocessing/HadoopRunner.java` to reflect the Common Crawl data you want to process and the S3 bucket where you want to store the results (`fileInputPath` and `fileOutputPath` variables)
3. Build the JAR file by running `ant compile jar` in the root project directory
4. Use the JAR file located at `dist/lib/LavaHadoop.jar` with Hadoop to analyze Common Crawl data

You should *definitely* only run this on [Amazon Elastic MapReduce](https://aws.amazon.com/emr/) unless you want it to take forever and drain your bank account.

To process the results of Hadoop jobs resulting from this code, check out the [lava-hadoop-processing](https://github.com/lavalamp-/lava-hadoop-processing) repository. If you're just interested in getting the content discovery hit lists resulting from this research, take a look at the [content-discovery-hit-lists](https://github.com/lavalamp-/content-discovery-hit-lists) repository.

More details will be available via a blog post on [lavalamp's personal blog](https://l.avala.mp/) in the near future.