package com.application.streaming.spark.structured;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class StructuredSparkStreamer {

	public void readFromKafkaTopics(String[] args) {
		SparkSession.builder().appName("SparkApplication").master("local").getOrCreate();
		System.out.println("Applicationcontext started");
		SparkSession sparkSession = SparkSession.builder().appName("SparkKafkaApplication").master("local")
				.getOrCreate();
		System.out.println("============ APPLICATIONCONTEXT Started ===========");
		Dataset<Row> dataSetRows = sparkSession.readStream().format("kafka")
				.option("kafka.bootstrap.servers", "localhost:9092")
				.option("subscribe", "spooldir-search-topic,spooldir-display-topic,spooldir-social-topic").load();
		//dataSetRows.collectAsList().forEach(x -> System.out.println(x));
		System.out.println("============ EXECUTION Finished ===========");
	}
}
