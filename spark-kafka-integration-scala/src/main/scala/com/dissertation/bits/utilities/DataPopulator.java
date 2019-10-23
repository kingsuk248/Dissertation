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
		search.setCost(getRandomDoubleValue(10, 30));
		search.setClicks(getRandomIntValue(1, 10));
		search.setCpc(getRandomDoubleValue(1, 12));
		search.setAvg_position(getRandomDoubleValue(2, 5));
		search.setImpressions(getRandomIntValue(2, 20));
		search.setQuote(getRandomIntValue(1, 15));
		search.setLeads(getRandomIntValue(0, 8));
		return search;
	}

	public static Display createSampleDisplayObject(String tenantName) {
		Display display = new Display();
		display.setTenantName(tenantName);
		display.setCost(getRandomDoubleValue(10, 30));
		display.setClicks(getRandomIntValue(1, 10));
		display.setCpc(getRandomDoubleValue(1, 12));
		display.setAvg_position(getRandomDoubleValue(2, 5));
		display.setImpressions(getRandomIntValue(2, 20));
		display.setQuote(getRandomIntValue(1, 15));
		List<String> rtbSourceValues = Arrays.asList("AdX", "AppNexus", "Casale", "OpenX", "Rubicorn");
		display.setRtb_source(getRandomStringValue(rtbSourceValues));
		return display;
	}

	public static Social createSampleSocialObject(String tenantName) {
		Social social = new Social();
		social.setTenantName(tenantName);
		social.setCost(getRandomDoubleValue(10, 30));
		social.setClicks(getRandomIntValue(1, 10));
		social.setCpc(getRandomDoubleValue(1, 12));
		social.setAvg_position(getRandomDoubleValue(2, 5));
		social.setImpressions(getRandomIntValue(2, 20));
		social.setCtr(getRandomDoubleValue(1, 8));
		social.setE_pcm(getRandomDoubleValue(1, 8));
		return social;
	}

	private static int getRandomIntValue(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}

	private static double getRandomDoubleValue(int min, int max) {
		Random r = new Random();
		return Math.round(r.nextDouble() * ((max - min) + min));
	}

	private static String getRandomStringValue(List<String> values) {
		int randomIndex = getRandomIntValue(0, values.size() - 1);
		return values.get(randomIndex);
	}
}
