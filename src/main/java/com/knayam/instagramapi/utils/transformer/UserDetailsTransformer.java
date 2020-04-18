package com.knayam.instagramapi.utils.transformer;

import org.springframework.stereotype.Component;

import com.knayam.instagramapi.domain.UserDetails;
import com.knayam.instagramapi.dto.request.AddUserDetailRequest;
import com.knayam.instagramapi.dto.response.UserDetailsDto;

@Component
public class UserDetailsTransformer implements Transformer<UserDetails, UserDetailsDto> {

	@Override
	public UserDetailsDto transformTo(UserDetails userDetails) {
		UserDetailsDto userDetailsDto = new UserDetailsDto();
		
		if(userDetails.getId() != null)
			userDetailsDto.setId(userDetails.getId());
		
		if(userDetails.getName() != null)
			userDetailsDto.setName(userDetails.getName());
		
		if(userDetails.getUsername() != null)
			userDetailsDto.setUsername(userDetails.getUsername());
		
		if(userDetails.getUserBio() != null)
			userDetailsDto.setUserBio(userDetails.getUserBio());
		
		if(userDetails.getEmail() != null)
			userDetailsDto.setEmail(userDetails.getEmail());
		
		if(userDetails.getDateOfBirth() != null)
			userDetailsDto.setDateOfBirth(userDetails.getDateOfBirth());
		
		if(userDetails.getTimestamp().toString() != null) 
			userDetailsDto.setLastModified(userDetails.getTimestamp().toString());
		
		return userDetailsDto;
	}		
}
