package com.knayam.instagramapi.utils.transformer;

import org.springframework.stereotype.Component;

import com.knayam.instagramapi.domain.UserDetails;
import com.knayam.instagramapi.dto.response.UserSearchDto;

@Component
public class UserSearchTransformer implements Transformer<UserDetails, UserSearchDto> {
	
	public UserSearchDto transformTo(UserDetails userDetails) {
		UserSearchDto userSearchDto = new UserSearchDto();
		
		if(userDetails.getId() != null)
			userSearchDto.setId(userDetails.getId());
		
		if(userDetails.getName() != null)
			userSearchDto.setName(userDetails.getName());
		
		if(userDetails.getUsername() != null)
			userSearchDto.setUsername(userDetails.getUsername());
				
		if(userDetails.getImageUrl() != null)
			userSearchDto.setImageUrl(userDetails.getImageUrl());
		
		return userSearchDto;
	};
}
