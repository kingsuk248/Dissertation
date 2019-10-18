package com.kafka.consumer;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.model.channel.Display;
import com.kafka.model.channel.Search;
import com.kafka.model.channel.Social;
import com.kafka.utilities.DataPopulator;

@RestController
public class KafkaRestController {

	@GetMapping("search/parameters/{tenantId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getSearchChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {
		
		List<Search> searchChannelValuesBatch = new ArrayList<Search>();
		for (int i = 0; i < 10; i++) {
			searchChannelValuesBatch.add(DataPopulator.createSampleSearchObject("allianz"));
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
		for (int i = 0; i < 10; i++) {
			displayChannelValuesBatch.add(DataPopulator.createSampleDisplayObject("allianz"));
		}
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringSearch = convertDisplayChannelToJsonObject(displayChannelValuesBatch);
		channelParametersMap.put("search", responseStringSearch);
		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}
	
	@GetMapping("social/parameters/{tenantId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getSocialChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {
		
		List<Social> socialChannelValuesBatch = new ArrayList<Social>();
		for (int i = 0; i < 10; i++) {
			socialChannelValuesBatch.add(DataPopulator.createSampleSocialObject("allianz"));
		}
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringSearch = convertSocialChannelToJsonObject(socialChannelValuesBatch);
		channelParametersMap.put("search", responseStringSearch);
		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}
	
	@GetMapping("channels/parameters/{tenantId}")
	public List<ResponseEntity<Map<String, List<Map<String, Object>>>>> getAllChannelValuesBatch(
			@PathVariable("tenantId") final String tenantId) {

		String tenantName = "allianz";
		ResponseEntity<Map<String, List<Map<String, Object>>>> searchResponseEntity = getSearchChannelValuesBatch(tenantName);
		ResponseEntity<Map<String, List<Map<String, Object>>>> displayResponseEntity = getDisplayChannelValuesBatch(tenantName);
		ResponseEntity<Map<String, List<Map<String, Object>>>> socialResponseEntity = getSocialChannelValuesBatch(tenantName);
		List<ResponseEntity<Map<String, List<Map<String, Object>>>>> allChannelsResponseEntity = new ArrayList<>();
		allChannelsResponseEntity.add(searchResponseEntity);
		allChannelsResponseEntity.add(displayResponseEntity);
		allChannelsResponseEntity.add(socialResponseEntity);
		return allChannelsResponseEntity;
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
