package com.knayam.instagramapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {

	@Getter
	@Setter
	private String id;

	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String email;

	@Getter
	@Setter
	private String userBio;

	@Getter
	@Setter
	private String dateOfBirth;

	@Getter
	@Setter
	private String lastModified;
}
