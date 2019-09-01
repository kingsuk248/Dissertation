package com.application.subscriber.topic.kafka.spark;

import com.application.streaming.spark.structured.StructuredSparkStreamer;

public class Application {

	public static void main(String[] args) {
		String operatingSystem = System.getProperty("os.name").toLowerCase();
		if (operatingSystem.contains("windows")) {
			System.setProperty("hadoop.home.dir",
					"C:\\Users\\kisarkar\\Desktop\\BITS\\4thSem\\softwares\\hadoop-3.1.2");

		} else if (operatingSystem.contains("linux") || operatingSystem.contains("ubuntu")) {
			System.setProperty("hadoop.home.dir", "/opt/hadoop-3.1.2");
		}
		Application application = new Application();
		application.run(args);
	}

	public void run(String... args) {
		StructuredSparkStreamer structuredSparkStreamer = new StructuredSparkStreamer();
		structuredSparkStreamer.readFromKafkaTopics(args);
	}
}
