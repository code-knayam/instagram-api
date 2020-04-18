package com.knayam.instagramapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDto {

	@Getter
	@Setter
	private String id;
	
	@Getter
	@Setter
	private String userId;
	
	@Getter
	@Setter
	private String mediaUrl;
	
	@Getter
	@Setter
	private String caption;
	
	@Getter
	@Setter
	private Integer totalLikes;
	
	@Getter
	@Setter
	private String createdOn;
	
	@Getter
	@Setter
	private String lastModified;
}
