package com.dissertation.bits.cache;

import org.apache.commons.collections4.map.LRUMap;

import com.dissertation.bits.model.Social;
import com.dissertation.bits.utilities.Constants;

/**
 * 
 * Implements Apache Commons LRUMap for updating the least recently used data
 * with the latest batch data for the Social channel
 *
 */
public class SocialInMemoryCache {
	public static LRUMap<Long, Social> socialCacheMap;
	public long lastAccessed = System.currentTimeMillis();
	
	static {
		socialCacheMap = new LRUMap<>(Constants.CACHE_SIZE);
	}
	
	public static void put(Long key, Social social) {
		synchronized (socialCacheMap) {
			socialCacheMap.put(key, social);
		}
	}
	
	public static Social get(Long key) {
		synchronized (socialCacheMap) {
			Social social = socialCacheMap.get(key);
			return social;
		}
	}
}
