package com.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

@Component
public class OSConfiguration {
	
	@Value("${hadoop.home}")
	private String hadoopHome;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInWindows() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	public String getHadoopHome() {
		hadoopHome = "Test";
		return hadoopHome;
	}
}
