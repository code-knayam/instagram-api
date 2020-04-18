package com.knayam.instagramapi.dto.request;

import lombok.Getter;
import lombok.Setter;

public class AddUserDetailRequest {

	@Getter
	@Setter
	private String username;
	
	@Getter
	@Setter
	private String name;
	
	@Getter
	@Setter
	private String email;
	
	@Getter
	@Setter
	private String imageUrl;
	
	@Getter
	@Setter
	private String userBio;
	
	@Getter
	@Setter
	private String dateOfBirth;	
}
