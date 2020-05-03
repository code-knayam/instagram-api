package com.knayam.instagramapi.utils.transformer;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.knayam.instagramapi.domain.UserDetails;
import com.knayam.instagramapi.dto.request.AddUserDetailRequest;

@Component
public class AddUserDetailsRequestTransformer implements Transformer<AddUserDetailRequest, UserDetails> {

	@Override
	public UserDetails transformTo(AddUserDetailRequest userDetailsRequest) {
		UserDetails details = new UserDetails();
		
		if(userDetailsRequest.getName() != null)
			details.setName(userDetailsRequest.getName());
		
		if(userDetailsRequest.getUserBio() != null)
			details.setUserBio(userDetailsRequest.getUserBio());
		
		if(userDetailsRequest.getEmail() != null)
			details.setEmail(userDetailsRequest.getEmail());
		
		if(userDetailsRequest.getUsername() != null)
			details.setUsername(userDetailsRequest.getUsername());
		
		if(userDetailsRequest.getDateOfBirth() != null)
			details.setDateOfBirth(userDetailsRequest.getDateOfBirth());
		
		if(userDetailsRequest.getImageUrl() != null)
			details.setImageUrl(userDetailsRequest.getImageUrl());
		
		details.setTimestamp(Instant.now());
		
		details.setFollowees(0);
		
		details.setFollowers(0);
		
		return details;
	}
}
