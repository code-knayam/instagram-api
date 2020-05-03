package com.knayam.instagramapi.service;

import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.knayam.instagramapi.domain.Follow;
import com.knayam.instagramapi.domain.UserDetails;
import com.knayam.instagramapi.dto.request.AddUserDetailRequest;
import com.knayam.instagramapi.dto.response.UserDetailsDto;
import com.knayam.instagramapi.exception.UserNotFoundException;
import com.knayam.instagramapi.respository.FollowRepository;
import com.knayam.instagramapi.respository.UserDetailsRepository;
import com.knayam.instagramapi.utils.transformer.AddUserDetailsRequestTransformer;
import com.knayam.instagramapi.utils.transformer.UserDetailsTransformer;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private FollowRepository followRepository;
	
	@Autowired
	private UserDetailsTransformer userDetailsTransformer;
	
	@Autowired
	private AddUserDetailsRequestTransformer addUserDetailsRequestTransformer;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Override
	public UserDetailsDto findUserById(String id) {
		UserDetailsDto userDetailsDto = null;
				
		try {
			UserDetails userDetails = userDetailsRepository.findById(id).orElse(null);
			
			if(userDetails == null) {
				LOGGER.error("User not found - " + id);
				return null;
			} else {
				LOGGER.info("User found - " + id);
				userDetailsDto = userDetailsTransformer.transformTo(userDetails);
			}			
		} catch(Exception e) {
			LOGGER.error("Error finding user", "User ID - " + id, e.getMessage(), e.getStackTrace());
		}
		
		return userDetailsDto;
	}
	
	@Override
	public boolean userExists(String id) {
		boolean userExists = false;
		
		try {
			userExists = userDetailsRepository.existsById(id);
		} catch(Exception e) {
			LOGGER.error("Error finding user", "User ID - " + id, e.getMessage(), e.getStackTrace());
		}
		
		return userExists;
	}
	
	@Override
	public ArrayList<UserDetailsDto> getFollowersByUserId(String id) throws UserNotFoundException {
		if(!userDetailsRepository.existsById(id)) {
			LOGGER.error("Error finding user", "User ID - " + id);
			throw new UserNotFoundException("User not found -" + id);
		}
		
		ArrayList<UserDetailsDto> followers = new ArrayList<UserDetailsDto>();
		
		try {
			ArrayList<Follow> followersData = followRepository.findAllFollowersById(id);
			
			followersData.stream().forEach(data -> {
				String followerId = data.getFollowerId();
				UserDetailsDto follower = findUserById(followerId);
				
				if(follower != null) {
					followers.add(follower);
				}				
			});			
		} catch(Exception e) {
			LOGGER.error("Error finding followers", "User ID - " + id, e.getMessage(), e.getStackTrace());
		}
		
		return followers;
	}
	
	@Override
	public ArrayList<UserDetailsDto> getFolloweesByUserId(String id) throws UserNotFoundException {
		if(!userDetailsRepository.existsById(id)) {
			LOGGER.error("Error finding user", "User ID - " + id);
			throw new UserNotFoundException("User not found -" + id);
		}
		
		ArrayList<UserDetailsDto> followees = new ArrayList<UserDetailsDto>();
		
		try {
			ArrayList<Follow> followeesData = followRepository.findAllFolloweesById(id);
			
			followeesData.stream().forEach(data -> {
				String followeeId = data.getFolloweeId();
				UserDetailsDto followee = findUserById(followeeId);
				
				if(followee != null) {
					followees.add(followee);
				}				
			});			
		} catch(Exception e) {
			LOGGER.error("Error finding followees", "User ID - " + id, e.getMessage(), e.getStackTrace());
		}
		
		return followees;
	}
	
	@Override
	public boolean deactivateUserByUserId(String id) throws UserNotFoundException {
		if(!userDetailsRepository.existsById(id)) {
			LOGGER.error("Error finding user", "User ID - " + id);
			throw new UserNotFoundException("User not found -" + id);
		}
		
		boolean deleted = false;
		
		try {
			userDetailsRepository.deleteById(id);
			deleted = true;
		} catch(Exception e) {
			LOGGER.error("Error deleting user", "User ID - " + id, e.getMessage(), e.getStackTrace());
		}
		
		return deleted;
	}
	
	@Override
	public UserDetailsDto addNewUser(AddUserDetailRequest userDetailsRequest) {
		UserDetailsDto userDetailsDto = null;
		
		try {
			UserDetails userDetails = addUserDetailsRequestTransformer.transformTo(userDetailsRequest);
			userDetails = userDetailsRepository.save(userDetails);
			userDetailsDto = userDetailsTransformer.transformTo(userDetails);
		} catch(Exception e) {
			LOGGER.error("Add new user failed", e.getMessage(), e.getStackTrace());
		}
		
		return userDetailsDto;
	}
	
	@Override
	public boolean isUsernameUnique(String username) {
		boolean isUserNameUnique = false;
		
		try {
			isUserNameUnique = !userDetailsRepository.existsByUsername(username);
		} catch(Exception e) {
			LOGGER.error("Error finding username uniqueness", e.getMessage(), e.getStackTrace());
		}
		
		return isUserNameUnique;
	}
	
	@Override
	public boolean isUserFollowed(String userId1, String userId2) throws UserNotFoundException{

		if(!userDetailsRepository.existsById(userId1)) {
			LOGGER.error("Error finding user", "User ID - " + userId1);
			throw new UserNotFoundException("User not found -" + userId1);
		}
		
		if(!userDetailsRepository.existsById(userId2)) {
			LOGGER.error("Error finding user", "User ID - " + userId2);
			throw new UserNotFoundException("User not found -" + userId2);
		}
		
		boolean isUserFollowed = false;
		
		try {
			ArrayList<Follow> followeesData = followRepository.findAllFolloweesById(userId1);
			
			for(int index = 0; index<followeesData.size(); index++) {
				Follow d = followeesData.get(index);
				
				if(d.getFollowerId().equals(userId2)) {
					isUserFollowed = true;
					break;
				}
			}
						
		} catch(Exception e) {
			LOGGER.error("Error finding user followed", e.getMessage(), e.getStackTrace());
		}
		
		
		return isUserFollowed;
	}
}
