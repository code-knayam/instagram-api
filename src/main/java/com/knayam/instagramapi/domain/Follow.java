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

@Document(collection = "follow")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Follow {

	@Id
	private String id;
	
	@Getter
	@Setter	
	private String followerId;
	
	@Getter
	@Setter
	private String followeeId;
	
	@Getter
	@Setter
	private Instant timestamp;
}
