package com.knayam.instagramapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knayam.instagramapi.domain.Follow;
import com.knayam.instagramapi.dto.request.AddUserDetailRequest;
import com.knayam.instagramapi.dto.response.UserDetailsDto;
import com.knayam.instagramapi.exception.UserNotFoundException;
import com.knayam.instagramapi.service.UserDetailsServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/{id}")
	@ApiOperation(value = "Get User Details by ID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User details found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "User not found"),
	})
	public ResponseEntity<UserDetailsDto> getUserDetails(
			@ApiParam(value="user id") @PathVariable("id") String id) {
		UserDetailsDto userDetailsDto = null;
		HttpStatus status;
		
		try {
			userDetailsDto = userDetailsService.findUserById(id);
			if(userDetailsDto != null) {
				status = HttpStatus.OK; 
			} else {
				status = HttpStatus.NOT_FOUND;
				LOGGER.error("Get Users details by id failed", "user id - " + id);
			}					
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Get Users details by id failed", "user id - " + id, e.getMessage(), e.getStackTrace());
		}

		return new ResponseEntity<UserDetailsDto>(userDetailsDto, new HttpHeaders(), status);
	}
	
	@GetMapping("/{id}/followers")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User details found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "User not found"),
	})
	public ResponseEntity<ArrayList<UserDetailsDto>> getFollowersByUserId(
			@ApiParam(value="user id") @PathVariable("id") String id) {
		
		ArrayList<UserDetailsDto> userDetailsDtos = null;
		HttpStatus status;
		
		try {
			userDetailsDtos = userDetailsService.getFollowersByUserId(id);
			status = HttpStatus.OK;
		} catch(UserNotFoundException e) {
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Get Followers by user id failed", "user id - " + id, e.getMessage(), e.getStackTrace());
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Get Followers by user id failed", "user id - " + id, e.getMessage(), e.getStackTrace());
		}		
		
		return new ResponseEntity<ArrayList<UserDetailsDto>>(userDetailsDtos, new HttpHeaders(), status);		
	}
	
	@GetMapping("/{id}/followees")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User details found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "User not found"),
	})
	public ResponseEntity<ArrayList<UserDetailsDto>> getFolloweesByUserId(
			@ApiParam(value="user id") @PathVariable("id") String id) {
		
		ArrayList<UserDetailsDto> userDetailsDtos = null;
		HttpStatus status;
		
		try {
			userDetailsDtos = userDetailsService.getFolloweesByUserId(id);
			status = HttpStatus.OK;
		} catch(UserNotFoundException e) {
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Get Followees by user id failed", "user id - " + id, e.getMessage(), e.getStackTrace());
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Get Followees by user id failed", "user id - " + id, e.getMessage(), e.getStackTrace());
		}		
		
		return new ResponseEntity<ArrayList<UserDetailsDto>>(userDetailsDtos, new HttpHeaders(), status);
	}
	
	@PutMapping("/{id}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User details found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "User not found"),
	})
	public ResponseEntity<HashMap<String, Boolean>> deactivateUserByUserId(
			@ApiParam(value="user id") @PathVariable("id") String id) {
		
		HashMap<String, Boolean> response = new HashMap<>();
		boolean deleted = false;
		HttpStatus status;
		
		try {
			deleted = userDetailsService.deactivateUserByUserId(id);			
			status = HttpStatus.OK;
		} catch(UserNotFoundException e) {
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Deactivate user by user id failed", "user id - " + id, e.getMessage(), e.getStackTrace());
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Deactivate user by user id failed", "user id - " + id, e.getMessage(), e.getStackTrace());
		}
		
		response.put("deleted", deleted);
		
		return new ResponseEntity<HashMap<String, Boolean>>(response, new HttpHeaders(), status);
	}
	
	@PostMapping
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User details found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource")
	})
	public ResponseEntity<UserDetailsDto> addNewUser(
			@ApiParam(value = "User Details") @RequestBody AddUserDetailRequest userDetails) {
		
		UserDetailsDto userDetailsDto = null;
		HttpStatus status;
		
		try {
			userDetailsDto = userDetailsService.addNewUser(userDetails);
			
			if(userDetailsDto != null) {
				status = HttpStatus.OK;				
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				LOGGER.error("Add new user failed");
			}
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Add new user failed", e.getMessage(), e.getStackTrace());
		}
		
		return new ResponseEntity<UserDetailsDto>(userDetailsDto, new HttpHeaders(), status);
	}
	
	@GetMapping("/unique/{userName}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User details found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource")
	})
	public ResponseEntity<Boolean> isUsernameUnique(
			@ApiParam(value = "User name") @PathVariable String userName) {
		HttpStatus status;
		boolean isUserNameUnique = false;
		
		try {
			isUserNameUnique = userDetailsService.isUsernameUnique(userName);
			status = HttpStatus.OK;
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Is User name unique failed", e.getMessage(), e.getStackTrace());
		}
		
		return new ResponseEntity<Boolean>(isUserNameUnique, new HttpHeaders(), status);
	}
	
	@GetMapping("/follows/{userId1}/{userId2}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User details found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "user not found")
	})
	public ResponseEntity<Boolean> isUserFollowed(
			@ApiParam(value = "User Id 1") @PathVariable("userId1") String userId1,
			@ApiParam(value = "User Id 1") @PathVariable("userId2") String userId2) {
		HttpStatus status;
		boolean isUserFollowed  = false;
		
		try {
			isUserFollowed = userDetailsService.isUserFollowed(userId1, userId2);
			status = HttpStatus.OK;
		} catch(UserNotFoundException e) { 
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("User not found", e.getMessage(), e.getStackTrace());
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Is User followed", e.getMessage(), e.getStackTrace());	
		}
		
		return new ResponseEntity<Boolean>(isUserFollowed, new HttpHeaders(), status);
	}
	
	@PostMapping("/follow/{userId1}/{userId2}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User details found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "user not found")
	})
	public ResponseEntity<Follow> followUser(
			@ApiParam(value = "User Id 1") @PathVariable("userId1") String userId1,
			@ApiParam(value = "User Id 1") @PathVariable("userId2") String userId2) {
		HttpStatus status;
		Follow followUser = null;
		
		try {
			followUser = userDetailsService.followUser(userId1, userId2);
			status = HttpStatus.OK;
		} catch(UserNotFoundException e) { 
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("User not found", e.getMessage(), e.getStackTrace());
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Follow user", e.getMessage(), e.getStackTrace());	
		}
		
		return new ResponseEntity<Follow>(followUser, new HttpHeaders(), status);
	}
}
