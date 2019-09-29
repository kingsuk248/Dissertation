package com.application;

import java.text.DecimalFormat;
import java.util.Random;

import com.model.Search;
import com.model.Tenant;

public class Application {
	public static final int NO_OF_MESSAGES = 10;
	
	public static void main(String[] args) {
		KafkaService kafkaService = new KafkaService();
		for (int i = 0; i < NO_OF_MESSAGES; i++) {
			Tenant tenant = createNewTenantBatch();
			kafkaService.send(tenant);
		}
		System.out.println("===== DONE =====");
	}
	
	public static Tenant createNewTenantBatch() {
		Tenant tenant = new Tenant();
		tenant.setTenantName("allianz");
		tenant.setSearch(createNewSearchObject());
		return tenant;
	}
	
	public static Search createNewSearchObject() {
		Search search = new Search();
		search.setAvg_position(getRandomDoubleValue(1.0, 30.0));
		search.setClicks(getRandomIntegerValue(10, 60));
		search.setCost(getRandomDoubleValue(50.0, 120.0));
		search.setCpc(getRandomDoubleValue(.50, 5.0));
		search.setImpressions(getRandomIntegerValue(5, 15));
		search.setLeads(getRandomIntegerValue(0, 10));
		search.setQuote(getRandomDoubleValue(2.0, 16.0));
		return search;
	}

	private static double getRandomDoubleValue(double minRange, double maxRange) {
		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		double retVal = Math.random() * (maxRange - minRange) + minRange;
		String formattedValue = decimalFormat.format(retVal);
		return Double.valueOf(formattedValue);
	}

	private static int getRandomIntegerValue(int minRange, int maxRange) {
		Random random = new Random();
		return random.nextInt((maxRange - minRange) + minRange);
	}
}
