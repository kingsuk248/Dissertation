package com.kafka.cache;

import org.apache.commons.collections4.map.LRUMap;

import com.kafka.model.channel.Social;

public class SocialInMemoryCache {
	public static LRUMap<Long, Social> socialCacheMap;
	public long lastAccessed = System.currentTimeMillis();
	
	public SocialInMemoryCache() {
		socialCacheMap = new LRUMap<>(1);
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
