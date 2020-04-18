package com.knayam.instagramapi.respository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.knayam.instagramapi.domain.UserDetails;

public interface UserDetailsRepository extends MongoRepository<UserDetails, String>{

	public boolean existsByUsername(String username);
}
