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
		System.out.println("dataSetRows" + dataSetRows);
		//dataSetRows.collectAsList().forEach(x -> System.out.println(x));
		for (int i = 0; i < 300; i++) {
			try {
				Thread.sleep(1000);
				System.out.println("Waiting: " + i);
			} catch (InterruptedException e) {
			}
		}
		System.out.println("============ EXECUTION Finished ===========");
	}
}
