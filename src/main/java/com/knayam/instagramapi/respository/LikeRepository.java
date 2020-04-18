package com.knayam.instagramapi.respository;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.knayam.instagramapi.domain.Like;

public interface LikeRepository extends MongoRepository<Like, String> {

	@Query("{postId: ?0}")
	public ArrayList<Like> findLikeByPostId(String postId); 
}
