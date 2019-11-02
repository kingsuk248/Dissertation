package com.dissertation.bits.persistence;

import java.util.List;

import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;

public interface ChannelRepository {
	
	public List<Search> getSearchChannelDataBetweenPeriods(String fromDate, String toDate);
	
	public List<Display> getDisplayChannelDataBetweenPeriods(String fromDate, String toDate);
	
	public List<Social> getSocialChannelDataBetweenPeriods(String fromDate, String toDate);
}
