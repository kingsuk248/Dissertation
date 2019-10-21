package com.dissertation.bits.cache;

import org.apache.commons.collections4.map.LRUMap;

import com.dissertation.bits.model.Search;

public class SearchInMemoryCache {
	public static LRUMap<Long, Search> searchCacheMap;
	public long lastAccessed = System.currentTimeMillis();
	
	static {
		searchCacheMap = new LRUMap<>(1);
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
