package com.knayam.instagramapi.utils.transformer;

import org.springframework.stereotype.Component;

import com.knayam.instagramapi.domain.Post;
import com.knayam.instagramapi.dto.response.PostDto;

@Component
public class PostTransformer implements Transformer<Post, PostDto> {

	@Override
	public PostDto transformTo(Post post) {
		PostDto postDto = new PostDto();
		
		if(post.getId() != null)
			postDto.setId(post.getId());
		
		if(post.getUserId() != null)
			postDto.setUserId(post.getUserId());
		
		if(post.getMediaUrl() != null)
			postDto.setMediaUrl(post.getMediaUrl());
		
		if(post.getCaption() != null)
			postDto.setCaption(post.getCaption());
		
		if(post.getTotalLikes() != null)
			postDto.setTotalLikes(post.getTotalLikes());
		
		if(post.getCreatedOn().toString() != null)
			postDto.setCreatedOn(post.getCreatedOn().toString());
		
		if(post.getUpdatedOn().toString() != null)
			postDto.setLastModified(post.getUpdatedOn().toString());
		
		return postDto;
	}	
}
