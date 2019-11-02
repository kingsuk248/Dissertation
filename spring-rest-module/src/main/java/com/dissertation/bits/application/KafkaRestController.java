package com.dissertation.bits.application;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.dissertation.bits.persistence.ChannelRepository;
import com.dissertation.bits.utilities.ChannelDataToJsonConverter;

/**
 * 
 * Controller for exposing the data from the downstream Kafka Topics via the
 * RESTful APIs
 *
 */

@RestController
@CrossOrigin
public class KafkaRestController {
	
	/**
	 * API for exposing the channel batch values
	 * @param tenantId
	 * @return ResponseEntity
	 */
	@GetMapping("channels/parameters/{tenantId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getAllChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {

		List<Search> searchChannelValuesBatch = new ArrayList<Search>();
		for (Map.Entry<Long, Search> entry : SearchInMemoryCache.searchCacheMap.entrySet()) {
			searchChannelValuesBatch.add(entry.getValue());
		}
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringSearch = ChannelDataToJsonConverter.convertSearchChannelToJsonObject(searchChannelValuesBatch);
		channelParametersMap.put("search", responseStringSearch);

		List<Display> displayChannelValuesBatch = new ArrayList<Display>();
		for (Map.Entry<Long, Display> entry : DisplayInMemoryCache.displayCacheMap.entrySet()) {
			displayChannelValuesBatch.add(entry.getValue());
		}
		List<Map<String, Object>> responseStringDisplay = ChannelDataToJsonConverter.convertDisplayChannelToJsonObject(displayChannelValuesBatch);
		channelParametersMap.put("display", responseStringDisplay);

		List<Social> socialChannelValuesBatch = new ArrayList<Social>();
		for (Map.Entry<Long, Social> entry : SocialInMemoryCache.socialCacheMap.entrySet()) {
			socialChannelValuesBatch.add(entry.getValue());
		}
		List<Map<String, Object>> responseStringSocial = ChannelDataToJsonConverter.convertSocialChannelToJsonObject(socialChannelValuesBatch);
		channelParametersMap.put("social", responseStringSocial);

		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}

	/**
	 * API for exposing the search channel batch values
	 * @param tenantId
	 * @return ResponseEntity
	 */
	@GetMapping("search/parameters/{tenantId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getSearchChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {

		List<Search> searchChannelValuesBatch = new ArrayList<Search>();
		for (Map.Entry<Long, Search> entry : SearchInMemoryCache.searchCacheMap.entrySet()) {
			searchChannelValuesBatch.add(entry.getValue());
		}
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringSearch = ChannelDataToJsonConverter.convertSearchChannelToJsonObject(searchChannelValuesBatch);
		channelParametersMap.put("search", responseStringSearch);
		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}

	/**
	 * API for exposing the display channel batch values
	 * @param tenantId
	 * @return ResponseEntity
	 */
	@GetMapping("display/parameters/{tenantId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getDisplayChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {

		List<Display> displayChannelValuesBatch = new ArrayList<Display>();
		for (Map.Entry<Long, Display> entry : DisplayInMemoryCache.displayCacheMap.entrySet()) {
			displayChannelValuesBatch.add(entry.getValue());
		}
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringDisplay = ChannelDataToJsonConverter.convertDisplayChannelToJsonObject(displayChannelValuesBatch);
		channelParametersMap.put("display", responseStringDisplay);
		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}

	/**
	 * API for exposing the social channel batch values
	 * @param tenantId
	 * @return ResponseEntity
	 */
	@GetMapping("social/parameters/{tenantId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getSocialChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {

		List<Social> socialChannelValuesBatch = new ArrayList<Social>();
		for (Map.Entry<Long, Social> entry : SocialInMemoryCache.socialCacheMap.entrySet()) {
			socialChannelValuesBatch.add(entry.getValue());
		}
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringSocial = ChannelDataToJsonConverter.convertSocialChannelToJsonObject(socialChannelValuesBatch);
		channelParametersMap.put("social", responseStringSocial);
		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}

	@RequestMapping("/test")
	public String test() {
		return "Test";
	}
}
