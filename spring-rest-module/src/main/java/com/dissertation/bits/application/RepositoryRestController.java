package com.dissertation.bits.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;
import com.dissertation.bits.persistence.ChannelRepository;
import com.dissertation.bits.utilities.ChannelDataToJsonConverter;

/**
 * 
 * Controller for exposing the daily aggregated data from the database via the
 * RESTful APIs
 *
 */

@RestController
@CrossOrigin
public class RepositoryRestController {

	@Autowired
	private ChannelRepository channelRepository;

	/**
	 * API for exposing the list of daily aggregated values
	 * 
	 * @param tenantId
	 * @return ResponseEntity
	 */
	@GetMapping("channels/parameters/{tenantId}/{fromDate}/{toDate}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getAllChannelValuesBetweenPeriods(
			@PathVariable("tenantId") final String tenantId, @PathVariable("fromDate") final String fromDate,
			@PathVariable("toDate") final String toDate) {

		List<Search> searchDailyAggregatedList = channelRepository.getSearchChannelDataBetweenPeriods(fromDate, toDate);
		final Map<String, List<Map<String, Object>>> channelParametersMap = new HashMap<>();
		List<Map<String, Object>> responseStringSearch = ChannelDataToJsonConverter
				.convertSearchChannelToJsonObject(searchDailyAggregatedList);
		channelParametersMap.put("search", responseStringSearch);
		
		List<Display> displayDailyAggregatedList = channelRepository.getDisplayChannelDataBetweenPeriods(fromDate, toDate);
		List<Map<String, Object>> responseStringDisplay = ChannelDataToJsonConverter
				.convertDisplayChannelToJsonObject(displayDailyAggregatedList);
		channelParametersMap.put("display", responseStringDisplay);
		
		List<Social> socialDailyAggregatedList = channelRepository.getSocialChannelDataBetweenPeriods(fromDate, toDate);
		List<Map<String, Object>> responseStringSocial = ChannelDataToJsonConverter
				.convertSocialChannelToJsonObject(socialDailyAggregatedList);
		channelParametersMap.put("social", responseStringSocial);
		return new ResponseEntity<Map<String, List<Map<String, Object>>>>(channelParametersMap, HttpStatus.OK);
	}
}
