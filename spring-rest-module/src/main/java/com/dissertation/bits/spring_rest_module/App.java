package com.dissertation.bits.spring_rest_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.dissertation.bits.cache.DisplayInMemoryCache;
import com.dissertation.bits.cache.SearchInMemoryCache;
import com.dissertation.bits.cache.SocialInMemoryCache;
import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;
import com.dissertation.bits.utilities.DataPopulator;

@SpringBootApplication
@ComponentScan(basePackages = "com.dissertation.bits")
public class App {
	public static void main(String[] args) throws Exception {

		SpringApplication.run(App.class, args);

		for (int i = 0; i < 5; i++) {
			Thread.sleep(100);
			Search search = DataPopulator.getSearchObjectFromBatch("allianz");
			long currentTime = System.currentTimeMillis();
			SearchInMemoryCache.put(currentTime, search);
		}
		for (int i = 0; i < 5; i++) {
			Thread.sleep(100);
			Display display = DataPopulator.getDisplayObjectFromBatch("allianz");
			long currentTime = System.currentTimeMillis();
			DisplayInMemoryCache.put(currentTime, display);
		}
		for (int i = 0; i < 5; i++) {
			Thread.sleep(100);
			Social social = DataPopulator.getSocialObjectFromBatch("allianz");
			long currentTime = System.currentTimeMillis();
			SocialInMemoryCache.put(currentTime, social);
		}
	}
}
