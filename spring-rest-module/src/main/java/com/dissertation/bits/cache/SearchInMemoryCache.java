package com.dissertation.bits.cache;

import org.apache.commons.collections4.map.LRUMap;

import com.dissertation.bits.model.Search;
import com.dissertation.bits.utilities.Constants;

/**
 * 
 * Uses Apache Commons LRUMap for updating the least recently used data
 * with the latest batch data for the Search channel
 *
 */
public class SearchInMemoryCache {
	public static LRUMap<Long, Search> searchCacheMap;
	public long lastAccessed = System.currentTimeMillis();

	static {
		searchCacheMap = new LRUMap<>(Constants.CACHE_SIZE);
	}

	public static void put(Long key, Search search) {
		synchronized (searchCacheMap) {
			searchCacheMap.put(key, search);
		}
	}

	public static Search get(Long key) {
		synchronized (searchCacheMap) {
			Search search = searchCacheMap.get(key);
			return search;
		}
	}
}
