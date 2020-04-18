package com.knayam.instagramapi.service;

import java.util.ArrayList;
import java.util.UUID;

import com.knayam.instagramapi.dto.request.AddUserDetailRequest;
import com.knayam.instagramapi.dto.response.UserDetailsDto;

public interface UserDetailsService {

	public UserDetailsDto findUserById(String id);
	
	public boolean userExists(String id);
	
	public ArrayList<UserDetailsDto> getFollowersByUserId(String id);
	
	public ArrayList<UserDetailsDto> getFolloweesByUserId(String id);
	
	public boolean deactivateUserByUserId(String id);
	
	public UserDetailsDto addNewUser(AddUserDetailRequest userDetails);
	
	public boolean isUsernameUnique(String userName);
}
