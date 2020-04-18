package com.knayam.instagramapi.domain;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "post")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post {

	@Id
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
	private boolean active;
	
	@Getter
	@Setter
	private Instant createdOn;
	
	@Getter
	@Setter
	private Instant updatedOn;
}
