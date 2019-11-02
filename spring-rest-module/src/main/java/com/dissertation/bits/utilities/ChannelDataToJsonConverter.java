package com.dissertation.bits.utilities;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONObject;

import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;

public class ChannelDataToJsonConverter {
	public static List<Map<String, Object>> convertSearchChannelToJsonObject(List<Search> searchChannelValuesBatch) {
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

	public static List<Map<String, Object>> convertDisplayChannelToJsonObject(List<Display> displayChannelValuesBatch) {
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

	public static List<Map<String, Object>> convertSocialChannelToJsonObject(List<Social> socialChannelValuesBatch) {
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
}
