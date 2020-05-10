package com.knayam.instagramapi.queries;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

import com.knayam.instagramapi.domain.UserDetails;

@Service
public class UserDetailsQueries {

	private MongoTemplate mongoTemplate;
	
	public UserDetailsQueries(MongoTemplate template) {
		this.mongoTemplate = template;
	}
	
	public List<UserDetails> findByFreeText(String text) {
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(text);
		
		Query query = TextQuery.queryText(criteria)
				.sortByScore()
				.with(PageRequest.of(0, 3));
		
		return this.mongoTemplate.find(query, UserDetails.class);
	}
}
