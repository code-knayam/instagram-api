package com.knayam.instagramapi.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.knayam.instagramapi.domain.Follow;
import com.knayam.instagramapi.domain.User;
import com.knayam.instagramapi.domain.UserDetails;
import com.knayam.instagramapi.dto.request.AddUserDetailRequest;
import com.knayam.instagramapi.dto.response.UserDetailsDto;
import com.knayam.instagramapi.dto.response.UserSearchDto;
import com.knayam.instagramapi.exception.UserNotFoundException;
import com.knayam.instagramapi.queries.UserDetailsQueries;
import com.knayam.instagramapi.respository.FollowRepository;
import com.knayam.instagramapi.respository.UserDetailsRepository;
import com.knayam.instagramapi.utils.transformer.AddUserDetailsRequestTransformer;
import com.knayam.instagramapi.utils.transformer.UserDetailsTransformer;
import com.knayam.instagramapi.utils.transformer.UserSearchTransformer;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private FollowRepository followRepository;
	
	@Autowired
	private UserDetailsTransformer userDetailsTransformer;
	
	@Autowired
	private UserSearchTransformer userSearchTransformer;
	
	@Autowired
	private AddUserDetailsRequestTransformer addUserDetailsRequestTransformer;
	
	@Autowired
	private UserDetailsQueries userDetailsQueries;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Override
	public UserDetailsDto findUserById(String id) throws UserNotFoundException {
		UserDetailsDto userDetailsDto = null;
				
		try {
			UserDetails userDetails = userDetailsRepository.findById(id).orElse(null);
			
			if(userDetails == null) {
				LOGGER.error("User not found - " + id);
				throw new UserNotFoundException("User not found -" + id);
			} else {
				LOGGER.info("User found - " + id);
																
				userDetailsDto = userDetailsTransformer.transformTo(userDetails);
				
				userDetailsDto.setFollowers(Integer.parseInt(followRepository.countFollowersById(id).toString()));
				userDetailsDto.setFollowees(Integer.parseInt(followRepository.countFolloweesById(id).toString()));
				
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
					follower.setIsFollowed(isUserFollowed(id, followerId));
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
					followee.setIsFollowed(isUserFollowed(id, followeeId));
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
				
				if(d.getFolloweeId().equals(userId2)) {
					isUserFollowed = true;
					break;
				}
			}
						
		} catch(Exception e) {
			LOGGER.error("Error finding user followed", e.getMessage(), e.getStackTrace());
		}
		
		
		return isUserFollowed;
	}
	
	@Override
	public Follow followUser(String userId1, String userId2) throws UserNotFoundException {
		if(!userDetailsRepository.existsById(userId1)) {
			LOGGER.error("Error finding user", "User ID - " + userId1);
			throw new UserNotFoundException("User not found -" + userId1);
		}
		
		if(!userDetailsRepository.existsById(userId2)) {
			LOGGER.error("Error finding user", "User ID - " + userId2);
			throw new UserNotFoundException("User not found -" + userId2);
		}
		
		Follow data = null;
		
		try {
			data = new Follow();
			
			data.setFolloweeId(userId2);
			data.setFollowerId(userId1);
			data.setTimestamp(Instant.now());
			
			followRepository.save(data);
									
		} catch(Exception e) {
			LOGGER.error("Error following user", e.getMessage(), e.getStackTrace());
		}
		
		
		return data;
	}
	
	@Override
	public Follow unfollowUser(String userId1, String userId2) throws UserNotFoundException {
		if(!userDetailsRepository.existsById(userId1)) {
			LOGGER.error("Error finding user", "User ID - " + userId1);
			throw new UserNotFoundException("User not found -" + userId1);
		}
		
		if(!userDetailsRepository.existsById(userId2)) {
			LOGGER.error("Error finding user", "User ID - " + userId2);
			throw new UserNotFoundException("User not found -" + userId2);
		}
			
		Follow follow = null;
		
		try {
			follow = followRepository.findByFollowerFollowee(userId1, userId2);
			
			if(follow != null) {
				followRepository.delete(follow);				
			}
									
		} catch(Exception e) {
			LOGGER.error("Error following user", e.getMessage(), e.getStackTrace());
		}
		
		
		return follow;
	}
	
	@Override
	public String getUserName(String id) throws UserNotFoundException {
		if(!userDetailsRepository.existsById(id)) {
			LOGGER.error("Error finding user", "User ID - " + id);
			throw new UserNotFoundException("User not found -" + id);
		}
		
		String userName = "";
		
		try {
			UserDetails user = userDetailsRepository.findById(id).orElse(null);
		
			if(user != null) {
				userName = user.getUsername();	
			}		
		} catch(Exception e) {
			LOGGER.error("Error finding username", e.getMessage(), e.getStackTrace());
		}
		
		return userName;
	}
	
	@Override
	public ArrayList<UserSearchDto> searchUser(String query) {
		ArrayList<UserSearchDto> users = new ArrayList<UserSearchDto>();
		
		try {
			List<UserDetails> details = userDetailsQueries.findByFreeText(query);
			
			details.stream().forEach(data -> {
				users.add(userSearchTransformer.transformTo(data));				
			});
		} catch(Exception e) {
			LOGGER.error("Error searching users", e.getMessage(), e.getStackTrace());
		}

		return users;
	}
}
