package com.dissertation.bits.cache;

import org.apache.commons.collections4.map.LRUMap;

import com.dissertation.bits.model.Display;

public class DisplayInMemoryCache {
	public static LRUMap<Long, Display> displayCacheMap;
	public long lastAccessed = System.currentTimeMillis();
	
	static {
		displayCacheMap = new LRUMap<>(5);
	}
	
	public static void put(Long key, Display display) {
		synchronized (displayCacheMap) {
			displayCacheMap.put(key, display);
		}
	}
	
	public static Display get(Long key) {
		synchronized (displayCacheMap) {
			Display display = displayCacheMap.get(key);
			return display;
		}
	}
}
