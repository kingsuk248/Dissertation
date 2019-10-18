package com.kafka.utilities;

import java.util.Random;

import com.kafka.model.message.Search;

public class DataPopulator {

	public static Search createSampleSearchObject(String tenantName) {
		Search search = new Search();
		search.setTenantName(tenantName);
		search.setChannelName("search");
		search.setCost(getRandomDoubleValue(300, 600));
		search.setClicks(getRandomIntValue(80, 150));
		search.setCpc(getRandomDoubleValue(20, 60));
		search.setAvg_position(getRandomDoubleValue(2, 5));
		search.setImpressions(getRandomIntValue(20, 60));
		search.setQuote(getRandomIntValue(5, 30));
		search.setLeads(getRandomIntValue(5, 40));
		return search;
	}
	
	private static int getRandomIntValue(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}
	
	private static double getRandomDoubleValue(int min, int max) {
		Random r = new Random();
		return r.nextDouble() * ((max - min) + min);
	}
}
