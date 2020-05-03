package com.knayam.instagramapi.domain;

import java.time.Instant;
import java.util.UUID;

import javax.annotation.Generated;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "userDetails")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDetails {

	@Id
	@Getter
	@Setter
	private String id;
	
	@Getter
	@Setter
	private String username;
	
	@Getter
	@Setter
	private String name;
	
	@Getter
	@Setter
	private String imageUrl;
	
	@Getter
	@Setter
	private String email;
	
	@Getter
	@Setter
	private String userBio;
	
	@Getter
	@Setter
	private Integer followers;
	
	@Getter
	@Setter
	private Integer followees;
	
	@Getter
	@Setter
	private String dateOfBirth;
	
	@Getter
	@Setter
	private Instant timestamp;
}
