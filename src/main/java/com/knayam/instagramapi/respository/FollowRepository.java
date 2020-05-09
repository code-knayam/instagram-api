package com.knayam.instagramapi.respository;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.knayam.instagramapi.domain.Follow;

public interface FollowRepository extends MongoRepository<Follow, String> {

	@Query("{followeeId: ?0}")
	public ArrayList<Follow> findAllFollowersById(String id);
	
	@Query("{followerId: ?0}")
	public ArrayList<Follow> findAllFolloweesById(String id);
	
	@Query(value = "{followeeId: ?0}", count = true)
	public Long countFollowersById(String id);
	
	@Query(value = "{followerId: ?0}", count = true)
	public Long countFolloweesById(String id);
	
	@Query("{followerId: ?0, followeeId: ?1}")
	public Follow findByFollowerFollowee(String id1, String id2);
}

