package com.dissertation.bits.application;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dissertation.bits.cache.DisplayInMemoryCache;
import com.dissertation.bits.cache.SearchInMemoryCache;
import com.dissertation.bits.cache.SocialInMemoryCache;
import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;

@RestController
public class KafkaRestController {

	@GetMapping("search/parameters/{tenantId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getSearchChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {
		
		List<Search> searchChannelValuesBatch = new ArrayList<Search>();
		for (Map.Entry<Long, Search> entry : SearchInMemoryCache.searchCacheMap.entrySet()) {
			searchChannelValuesBatch.add(entry.getValue());
		}
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringSearch = convertSearchChannelToJsonObject(searchChannelValuesBatch);
		channelParametersMap.put("search", responseStringSearch);
		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}
	
	@GetMapping("display/parameters/{tenantId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getDisplayChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {
		
		List<Display> displayChannelValuesBatch = new ArrayList<Display>();
		for (Map.Entry<Long, Display> entry : DisplayInMemoryCache.displayCacheMap.entrySet()) {
			displayChannelValuesBatch.add(entry.getValue());
		}
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringDisplay = convertDisplayChannelToJsonObject(displayChannelValuesBatch);
		channelParametersMap.put("display", responseStringDisplay);
		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}
	
	@GetMapping("social/parameters/{tenantId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getSocialChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {
		
		List<Social> socialChannelValuesBatch = new ArrayList<Social>();
		for (Map.Entry<Long, Social> entry : SocialInMemoryCache.socialCacheMap.entrySet()) {
			socialChannelValuesBatch.add(entry.getValue());
		}
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringSocial = convertSocialChannelToJsonObject(socialChannelValuesBatch);
		channelParametersMap.put("social", responseStringSocial);
		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}
	
	@GetMapping("channels/parameters/{tenantId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getAllChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {

		List<Search> searchChannelValuesBatch = new ArrayList<Search>();
		for (Map.Entry<Long, Search> entry : SearchInMemoryCache.searchCacheMap.entrySet()) {
			searchChannelValuesBatch.add(entry.getValue());
		}
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringSearch = convertSearchChannelToJsonObject(searchChannelValuesBatch);
		channelParametersMap.put("search", responseStringSearch);
		
		List<Display> displayChannelValuesBatch = new ArrayList<Display>();
		for (Map.Entry<Long, Display> entry : DisplayInMemoryCache.displayCacheMap.entrySet()) {
			displayChannelValuesBatch.add(entry.getValue());
		}
		List<Map<String, Object>> responseStringDisplay = convertDisplayChannelToJsonObject(displayChannelValuesBatch);
		channelParametersMap.put("display", responseStringDisplay);
		
		List<Social> socialChannelValuesBatch = new ArrayList<Social>();
		for (Map.Entry<Long, Social> entry : SocialInMemoryCache.socialCacheMap.entrySet()) {
			socialChannelValuesBatch.add(entry.getValue());
		}
		List<Map<String, Object>> responseStringSocial = convertSocialChannelToJsonObject(socialChannelValuesBatch);
		channelParametersMap.put("social", responseStringSocial);
		
		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}
	
	private List<Map<String, Object>> convertSearchChannelToJsonObject(List<Search> searchChannelValuesBatch) {
		final ObjectMapper mapper = new ObjectMapper();
		final List<Map<String, Object>> jsonObjectList = new ArrayList<>();
		DateFormat df = null;
		ObjectWriter writer = mapper.writer(df);
		for (Search search : searchChannelValuesBatch) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(writer.writeValueAsString(search));
                Map<String, Object> jsonObjectMap = jsonObject.toMap();
                jsonObjectList.add(jsonObjectMap);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return jsonObjectList;
	}
	
	private List<Map<String, Object>> convertDisplayChannelToJsonObject(List<Display> displayChannelValuesBatch) {
		final ObjectMapper mapper = new ObjectMapper();
		final List<Map<String, Object>> jsonObjectList = new ArrayList<>();
		DateFormat df = null;
		ObjectWriter writer = mapper.writer(df);
		for (Display display : displayChannelValuesBatch) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(writer.writeValueAsString(display));
                Map<String, Object> jsonObjectMap = jsonObject.toMap();
                jsonObjectList.add(jsonObjectMap);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return jsonObjectList;
	}
	
	private List<Map<String, Object>> convertSocialChannelToJsonObject(List<Social> socialChannelValuesBatch) {
		final ObjectMapper mapper = new ObjectMapper();
		final List<Map<String, Object>> jsonObjectList = new ArrayList<>();
		DateFormat df = null;
		ObjectWriter writer = mapper.writer(df);
		for (Social social : socialChannelValuesBatch) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(writer.writeValueAsString(social));
                Map<String, Object> jsonObjectMap = jsonObject.toMap();
                jsonObjectList.add(jsonObjectMap);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return jsonObjectList;
	}
	
	@RequestMapping("/test")
	public String test() {
		return "Test";
	}
}
