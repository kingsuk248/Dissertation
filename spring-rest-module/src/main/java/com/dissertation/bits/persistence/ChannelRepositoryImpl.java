package com.dissertation.bits.persistence;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;

@Repository
public class ChannelRepositoryImpl implements ChannelRepository {
	
	@Autowired
    private MongoOperations mongoOperations;
	
	@Override
	public List<Search> getSearchChannelDataBetweenPeriods(String fromDate, String toDate) {
		List<Search> searchList = new ArrayList<>();
		Query query = new Query();
		Criteria criteriaGt = Criteria.where("date").gt(convertStringToDateObject(fromDate));
		query.addCriteria(criteriaGt);
		searchList = mongoOperations.find(query, Search.class, "searchColl");
		return searchList;
	}

	@Override
	public List<Display> getDisplayChannelDataBetweenPeriods(String fromDate, String toDate) {
		List<Display> displayList = new ArrayList<>();
		Query query = new Query();
		Criteria criteriaGt = Criteria.where("date").gt(convertStringToDateObject(fromDate));
		query.addCriteria(criteriaGt);
		displayList = mongoOperations.find(query, Display.class, "displayColl");
		return displayList;
	}

	@Override
	public List<Social> getSocialChannelDataBetweenPeriods(String fromDate, String toDate) {
		List<Social> socialList = new ArrayList<>();
		Query query = new Query();
		Criteria criteriaGt = Criteria.where("date").gt(convertStringToDateObject(fromDate));
		query.addCriteria(criteriaGt);
		socialList = mongoOperations.find(query, Social.class, "socialColl");
		return socialList;
	}
	
	private Date convertStringToDateObject(final String dateStr) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (final ParseException pe) {
        }
        return date;
    }

}
