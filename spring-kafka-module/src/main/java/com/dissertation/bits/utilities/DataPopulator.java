package com.dissertation.bits.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;

public class DataPopulator {

	public static Search createSampleSearchObject(String tenantName) {
		Search search = new Search();
		search.setTenantName(tenantName);
		search.setCost(getRandomDoubleValue(300, 600));
		search.setClicks(getRandomIntValue(80, 150));
		search.setCpc(getRandomDoubleValue(20, 60));
		search.setAvg_position(getRandomDoubleValue(2, 5));
		search.setImpressions(getRandomIntValue(20, 60));
		search.setQuote(getRandomIntValue(5, 30));
		search.setLeads(getRandomIntValue(5, 40));
		return search;
	}

	public static Display createSampleDisplayObject(String tenantName) {
		Display display = new Display();
		display.setTenantName(tenantName);
		display.setCost(getRandomDoubleValue(300, 600));
		display.setClicks(getRandomIntValue(80, 150));
		display.setCpc(getRandomDoubleValue(20, 60));
		display.setAvg_position(getRandomDoubleValue(2, 5));
		display.setImpressions(getRandomIntValue(20, 60));
		display.setQuote(getRandomIntValue(5, 30));
		List<String> rtbSourceValues = Arrays.asList("AdX", "AppNexus", "Casale", "OpenX", "Rubicorn");
		display.setRtb_source(getRandomStringValue(rtbSourceValues));
		return display;
	}

	public static Social createSampleSocialObject(String tenantName) {
		Social social = new Social();
		social.setTenantName(tenantName);
		social.setCost(getRandomDoubleValue(300, 600));
		social.setClicks(getRandomIntValue(80, 150));
		social.setCpc(getRandomDoubleValue(20, 60));
		social.setAvg_position(getRandomDoubleValue(2, 5));
		social.setImpressions(getRandomIntValue(20, 60));
		social.setCtr(getRandomDoubleValue(5, 15));
		social.setE_pcm(getRandomDoubleValue(2, 10));
		return social;
	}

	private static int getRandomIntValue(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}

	private static double getRandomDoubleValue(int min, int max) {
		Random r = new Random();
		return r.nextDouble() * ((max - min) + min);
	}

	private static String getRandomStringValue(List<String> values) {
		int randomIndex = getRandomIntValue(0, values.size() - 1);
		return values.get(randomIndex);
	}
}
