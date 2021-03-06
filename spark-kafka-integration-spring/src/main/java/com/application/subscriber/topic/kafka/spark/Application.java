package com.application.subscriber.topic.kafka.spark;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.application.streaming.spark.structured.StructuredSparkStreamer;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		String operatingSystem = System.getProperty("os.name").toLowerCase();
		if (operatingSystem.contains("windows")) {
			System.setProperty("hadoop.home.dir",
					"C:\\Users\\kisarkar\\Desktop\\BITS\\4thSem\\softwares\\hadoop-3.1.2");

		} else if (operatingSystem.contains("linux") || operatingSystem.contains("ubuntu")) {
			System.setProperty("hadoop.home.dir", "/opt/hadoop-3.1.2");
		}
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		StructuredSparkStreamer structuredSparkStreamer = new StructuredSparkStreamer();
		structuredSparkStreamer.readFromKafkaTopics(args);
	}
}
