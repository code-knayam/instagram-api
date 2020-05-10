package com.knayam.instagramapi.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.knayam.instagramapi.dto.response.PostDto;
import com.knayam.instagramapi.dto.response.UserDetailsDto;
import com.knayam.instagramapi.exception.PostNotFoundException;
import com.knayam.instagramapi.exception.UserNotFoundException;
import com.knayam.instagramapi.service.PostServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	
	@Autowired
	private PostServiceImpl postService;

	private final static Logger LOGGER = LoggerFactory.getLogger(PostController.class);
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Get Post by ID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Post found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "Post not found"),
	})
	public ResponseEntity<PostDto> getPostById(
			@ApiParam(value="Post id") @PathVariable("id") String id) {

		PostDto postDto = null;		
		HttpStatus status;

		try {
			postDto = postService.getPostById(id);
			
			if(postDto != null) {
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.NOT_FOUND;
			}	
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Get Post By id failed", "Post id - " + id, e.getMessage(), e.getStackTrace());
		}
		
		return new ResponseEntity<PostDto>(postDto, new HttpHeaders(), status);
	}
	
	@GetMapping("/user/{userId}")
	@ApiOperation(value = "Get all posts made by user ")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Post found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "User not found"),
	})
	public ResponseEntity<ArrayList<PostDto>> getPostsByUserid(
			@ApiParam(value="User id") @PathVariable("userId") String userId) {
		
		ArrayList<PostDto> posts = null;
		HttpStatus status;
		
		try {
			posts = postService.getPostsByUserId(userId);
			
			status = HttpStatus.OK;
		} catch (UserNotFoundException e) {						
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Get All Posts By user id failed", "User id - " + userId, e.getMessage(), e.getStackTrace());
		}		
		
		return new ResponseEntity<ArrayList<PostDto>>(posts, new HttpHeaders(), status);
	}
	
	@GetMapping("/likes/{id}")
	@ApiOperation(value = "Get all users who liked the post")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message="Users found"),
			@ApiResponse(code = 401, message="Nto authorized"),
			@ApiResponse(code = 403, message="Forbidden resource"),
			@ApiResponse(code = 404, message="Post not found"),
	})
	public ResponseEntity<ArrayList<UserDetailsDto>> getUsersByPostLikeId(
			@ApiParam(value="Post id") @PathVariable("id") String postId) {
		
		ArrayList<UserDetailsDto> userDetailsDtos = null;
		HttpStatus status;
		
		try {
			userDetailsDtos = postService.getUsersByPostLikeId(postId);
			
			status = HttpStatus.OK;
		} catch(PostNotFoundException ex) {
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Get Users who liked post By id failed", "Post id - " + postId, ex.getMessage(), ex.getStackTrace());
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Get Users who liked post By id failed", "Post id - " + postId, e.getMessage(), e.getStackTrace());
		}
		
		return new ResponseEntity<ArrayList<UserDetailsDto>>(userDetailsDtos, new HttpHeaders(), status);
	}
	
	
	@PostMapping()
	@ApiOperation(value = "Add new post")
	public ResponseEntity<PostDto> addNewPost(
			@ApiParam(value = "User Id") @RequestHeader(value = "userId") String userId,
			@ApiParam(value = "Multipart File") @RequestParam("file") MultipartFile file,
			@ApiParam(value = "Caption") @RequestParam("caption") String caption) {
		
		PostDto post = null;
		HttpStatus status;
		
		try {
			post = postService.uploadPost(userId, file, caption);
			
			if(post != null) {
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} catch (UserNotFoundException e) {
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Add new  Post failed", e.getMessage(), e.getStackTrace());
		}
		
		return new ResponseEntity<PostDto>(post, new HttpHeaders(), status);
	}
	
	@PutMapping("/{postId}")
	@ApiOperation(value = "Update post")
	public ResponseEntity<PostDto> updatePost(
			@ApiParam(value = "User Id") @RequestHeader(value = "userId") String userId,
			@ApiParam(value = "Multipart File") @RequestParam("file") MultipartFile file,
			@ApiParam(value = "Caption") @RequestParam("caption") String caption,
			@ApiParam(value="Post id") @PathVariable("postId") String postId) {
		
		PostDto post = null;
		HttpStatus status;
		
		try {
			post = postService.updatePost(postId, userId, file, caption);
			
			if(post != null) {
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} catch (UserNotFoundException e) {
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Update Post failed", e.getMessage(), e.getStackTrace());
		}
		
		return new ResponseEntity<PostDto>(post, new HttpHeaders(), status);
	}
	
	@DeleteMapping("/{postId}")
	@ApiOperation(value = "Delete post by Id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Post found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "Post not found"),
	})
	public ResponseEntity<String> deletePostById(
			@ApiParam(value="Post id") @PathVariable("postId") String postId) {
		
		boolean isDeleted = false;
		HttpStatus status;
		
		try {
			isDeleted = postService.deletePostById(postId); 
					
			if(isDeleted) {				
				status = HttpStatus.OK;				
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} catch(PostNotFoundException e) {
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Delete post by id failed", "Post id - " + postId, e.getMessage(), e.getStackTrace());
		}
		
		return new ResponseEntity<String>(postId, new HttpHeaders(), status);
	}
	
	@PutMapping("/like/{postId}/{userId}")
	@ApiOperation(value = "Like Post")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Post found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "Post / user not found"),
	})
	public ResponseEntity<Boolean> likePostByPostIdAndUserId(
			@ApiParam(value="Post id") @PathVariable("postId") String postId,
			@ApiParam(value="User id") @PathVariable("userId") String userId) {
		boolean isPostLiked = false;
		HttpStatus status;
		
		try {
			isPostLiked = postService.likePost(postId, userId);
			status = HttpStatus.OK;
		} catch(UserNotFoundException e) {
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Like post failed", "Post id - " + postId, "User id - " + userId, e.getMessage(), e.getStackTrace());
		} catch(PostNotFoundException e) {
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Like post failed", "Post id - " + postId, "User id - " + userId, e.getMessage(), e.getStackTrace());
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			LOGGER.error("Like post failed", "Post id - " + postId, "User id - " + userId, e.getMessage(), e.getStackTrace());
		}
		
		return new ResponseEntity<Boolean>(isPostLiked, new HttpHeaders(), status);
	}
	
	@GetMapping("/user/feed/{userId}")
	@ApiOperation(value = "Get post feed for user ")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Post found"),
			@ApiResponse(code = 401, message = "Not authorized"),
			@ApiResponse(code = 403, message = "Forbidden resource"),
			@ApiResponse(code = 404, message = "User not found"),
	})
	public ResponseEntity<ArrayList<PostDto>> getPostsFeedByUserid(
			@ApiParam(value="User id") @PathVariable("userId") String userId) {
		
		ArrayList<PostDto> posts = null;
		HttpStatus status;
		
		try {
			posts = postService.getPostsFeedByUserId(userId);
			
			status = HttpStatus.OK;
		} catch (UserNotFoundException e) {						
			status = HttpStatus.NOT_FOUND;
			LOGGER.error("Get All Posts By user id failed", "User id - " + userId, e.getMessage(), e.getStackTrace());
		}		
		
		return new ResponseEntity<ArrayList<PostDto>>(posts, new HttpHeaders(), status);
	}
}
