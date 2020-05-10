package com.knayam.instagramapi.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.knayam.instagramapi.dto.response.PostDto;
import com.knayam.instagramapi.dto.response.UserDetailsDto;

public interface PostService {

	public PostDto getPostById(String id);
	
	public ArrayList<PostDto> getPostsByUserId(String userId);
	
	public ArrayList<UserDetailsDto> getUsersByPostLikeId(String postId);
	
	public boolean deletePostById(String id);
	
	public PostDto uploadPost(String userId, MultipartFile file, String caption );
	
	public PostDto updatePost(String postId, String userId, MultipartFile file, String caption );
	
	public boolean likePost(String postId, String userId);
	
	public ArrayList<PostDto> getPostsFeedByUserId(String userId);

}
