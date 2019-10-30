package com.dissertation.bits.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;

public class DataPopulator {

	public static Search getSearchObjectFromBatch(String tenantName) {
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

	public static Display getDisplayObjectFromBatch(String tenantName) {
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

	public static Social getSocialObjectFromBatch(String tenantName) {
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
	
	
	public static Search createSampleSingleSearchObject(String tenantName) {
		Search search = new Search();
		search.setTenantName(tenantName);
		search.setCost(getRandomDoubleValue(30, 60));
		search.setClicks(getRandomIntValue(8, 15));
		search.setCpc(getRandomDoubleValue(2, 6));
		search.setAvg_position(getRandomDoubleValue(2, 5));
		search.setImpressions(getRandomIntValue(2, 6));
		search.setQuote(getRandomIntValue(1, 5));
		search.setLeads(getRandomIntValue(0, 8));
		return search;
	}

	public static Display createSampleSingleDisplayObject(String tenantName) {
		Display display = new Display();
		display.setTenantName(tenantName);
		display.setCost(getRandomDoubleValue(30, 60));
		display.setClicks(getRandomIntValue(8, 15));
		display.setCpc(getRandomDoubleValue(2, 6));
		display.setAvg_position(getRandomDoubleValue(2, 5));
		display.setImpressions(getRandomIntValue(2, 6));
		display.setQuote(getRandomIntValue(1, 5));
		List<String> rtbSourceValues = Arrays.asList("AdX", "AppNexus", "Casale", "OpenX", "Rubicorn");
		display.setRtb_source(getRandomStringValue(rtbSourceValues));
		return display;
	}

	public static Social createSampleSingleSocialObject(String tenantName) {
		Social social = new Social();
		social.setTenantName(tenantName);
		social.setCost(getRandomDoubleValue(30, 60));
		social.setClicks(getRandomIntValue(8, 15));
		social.setCpc(getRandomDoubleValue(2, 6));
		social.setAvg_position(getRandomDoubleValue(2, 5));
		social.setImpressions(getRandomIntValue(2, 6));
		social.setCtr(getRandomDoubleValue(1, 5));
		social.setE_pcm(getRandomDoubleValue(1, 3));
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
