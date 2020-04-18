package com.knayam.instagramapi.respository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.knayam.instagramapi.domain.User;

public interface UserRepository extends MongoRepository<User, String> {

}
