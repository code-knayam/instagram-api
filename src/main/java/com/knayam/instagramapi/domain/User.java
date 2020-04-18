package com.knayam.instagramapi.domain;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "user")
public class User {
	
	@Id
	private String id;

	private String username;
	
	private String password;
	
	private boolean active;
	
	private Instant timestamp;
}
