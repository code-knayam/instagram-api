package com.knayam.instagramapi.respository;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.knayam.instagramapi.domain.Post;

public interface PostRepository extends MongoRepository<Post, String>{

	public ArrayList<Post> findAllByUserId(String userId);
	
}
