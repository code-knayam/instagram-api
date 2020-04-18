package com.knayam.instagramapi.respository;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.knayam.instagramapi.domain.Follow;

public interface FollowRepository extends MongoRepository<Follow, String> {

	@Query("{followerId: ?0}")
	public ArrayList<Follow> findAllFollowersById(String id);
	
	@Query("{followeeId: ?0}")
	public ArrayList<Follow> findAllFolloweesById(String id);
}

