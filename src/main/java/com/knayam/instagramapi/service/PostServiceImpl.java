package com.knayam.instagramapi.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.knayam.instagramapi.domain.Like;
import com.knayam.instagramapi.domain.Post;
import com.knayam.instagramapi.dto.response.PostDto;
import com.knayam.instagramapi.dto.response.UserDetailsDto;
import com.knayam.instagramapi.exception.PostNotFoundException;
import com.knayam.instagramapi.exception.UserNotFoundException;
import com.knayam.instagramapi.respository.LikeRepository;
import com.knayam.instagramapi.respository.PostRepository;
import com.knayam.instagramapi.utils.transformer.PostTransformer;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private PostTransformer postTransformer;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);
	
	@Override
	public PostDto getPostById(String id) {
		PostDto postDto = null;
		
		try {
			Post post = postRepository.findById(id).orElse(null);
			
			if(post == null) {
				LOGGER.error("Post not found - " + id);
			} else {
				postDto = postTransformer.transformTo(post);
				LOGGER.info("Post found - " + id);
			}
		} catch(Exception e) {
			LOGGER.error("Error finding Post", "Post ID - " + id, e.getMessage(), e.getStackTrace());
		}

		return postDto;
	}
	
	@Override
	public ArrayList<PostDto> getPostsByUserId(String userId) throws UserNotFoundException {
		if (!userDetailsService.userExists(userId)) {
			LOGGER.error("User not found with ID - " + userId);
			throw new UserNotFoundException("User not found.");
		}
		
		ArrayList<PostDto> postsDto = new ArrayList<>();
		
		try {
			ArrayList<Post> posts = postRepository.findAllByUserId(userId);
			
			posts.stream().forEach(post -> {
				if(post.isActive()) {
					PostDto postDto = postTransformer.transformTo(post);
					postsDto.add(postDto);					
				}
			});
			
			LOGGER.info("Post found for userid - " + userId);
			
		} catch(Exception e) {
			LOGGER.error("Error finding Posts by user", "User ID - " + userId, e.getMessage(), e.getStackTrace());
		}
		
		return postsDto;
	}
	
	@Override
	public ArrayList<UserDetailsDto> getUsersByPostLikeId(String postId) throws PostNotFoundException {
		if(!postRepository.existsById(postId)) {
			LOGGER.error("Post not found with Id - " + postId);
			throw new PostNotFoundException("Post not found with Id - " + postId);
		}
		
		ArrayList<UserDetailsDto> userDetailsDtos = new ArrayList<>();
		
		try {
			ArrayList<Like> likes = likeRepository.findLikeByPostId(postId);
			
			likes.stream().forEach(like -> {
				String userId = like.getUserId();
				
				if(userDetailsService.userExists(userId)) {
					UserDetailsDto userDetailsDto = userDetailsService.findUserById(userId);
					
					if(userDetailsDto != null) {
						userDetailsDtos.add(userDetailsDto);
					}
				}
			});
		} catch(Exception e) {
			LOGGER.error("Error finding user who liked by post id", "Post id - " + postId, e.getMessage(), e.getStackTrace());
		}
		
		return userDetailsDtos;
	}
	
	@Override
	public PostDto uploadPost(String userId, MultipartFile file, String caption) throws UserNotFoundException  {
		if (!userDetailsService.userExists(userId)) {
			LOGGER.error("User not found with ID - " + userId);
			throw new UserNotFoundException("User not found.");
		}
		
		PostDto postDto = null;

		try {
			Post newPost = new Post();
			
			newPost.setUserId(userId);
			newPost.setMediaUrl("mediaUrl");
			newPost.setCaption(caption);
			newPost.setActive(true);
			newPost.setTotalLikes(0);
			newPost.setCreatedOn(Instant.now());
			newPost.setUpdatedOn(Instant.now());
			
			newPost = postRepository.save(newPost);
			
			if(newPost != null) {
				postDto = postTransformer.transformTo(newPost);
				
				LOGGER.info("Post saved with id - " + postDto.getId());
			}
			
		} catch(Exception e) {
			LOGGER.error("Error uploading post", "User ID - " + userId, e.getMessage(), e.getStackTrace());
		}
		
		return postDto;
	}
	
	@Override
	public PostDto updatePost(String postId, String userId, MultipartFile file, String caption)
			 throws UserNotFoundException, PostNotFoundException  {
		if (!userDetailsService.userExists(userId)) {
			LOGGER.error("User not found with ID - " + userId);
			throw new UserNotFoundException("User not found.");
		}
		
		if (!postRepository.existsById(postId)) {
			LOGGER.error("Post not found with ID - " + postId);
			throw new UserNotFoundException("Post not found.");
		}
		
		PostDto postDto = null;
		
		try {
			
			Post post = postRepository.findById(postId).orElse(null);
			
			if(post != null) {
				post.setMediaUrl("mediaUrl");
				post.setCaption(caption);
				post.setUpdatedOn(Instant.now());
				
				post = postRepository.save(post);
				
				if(post != null) {
					postDto = postTransformer.transformTo(post);
					
					LOGGER.info("Post saved with id - " + postDto.getId());
				}
			}
		} catch(Exception e) {
			LOGGER.error("Error updating post", "Post ID - " + postId, "User ID - " + userId, e.getMessage(), e.getStackTrace());
		}
		
		return postDto;
	}
	
	@Override
	public boolean deletePostById(String id) throws PostNotFoundException {
		
		if(!postRepository.existsById(id)) {
			LOGGER.error("Post not found with ID - " + id);
			throw new PostNotFoundException("post not found.");
		}
		
		boolean isDeleted = false;
		
		try {
			postRepository.deleteById(id);
			
			isDeleted = true;
			
			LOGGER.info("Post deleted - " + id);
		} catch(Exception e) {
			LOGGER.error("Error finding Posts ", "Post ID - " + id, e.getMessage(), e.getStackTrace());
		}
		
		return isDeleted;
	}
	
	@Override
	public boolean likePost(String postId, String userId) 
			throws PostNotFoundException, UserNotFoundException {
		
		if(!userDetailsService.userExists(userId)) {
			LOGGER.error("user not found with ID - " + userId);
			throw new PostNotFoundException("user not found.");
		}
		
		if(!postRepository.existsById(postId)) {
			LOGGER.error("Post not found with ID - " + postId);
			throw new PostNotFoundException("post not found.");
		}
		
		boolean isPostLiked = false;
		
		try {
			Like newLike = new Like();
			
			newLike.setPostId(postId);
			newLike.setUserId(userId);
			newLike.setTimestamp(Instant.now());
			
			newLike = likeRepository.save(newLike);
			
			if(newLike !=null) {
				Post post = postRepository.findById(postId).orElse(null);
				
				if(post !=null) {
					post.setTotalLikes(post.getTotalLikes() + 1);
					post = postRepository.save(post);
					isPostLiked = true;		
				}
			}			
		} catch(Exception e) {
			LOGGER.error("Error liking post ", "Post ID - " + postId, "User ID - " + userId, e.getMessage(), e.getStackTrace());
		}
		
		return isPostLiked;
	}

}
