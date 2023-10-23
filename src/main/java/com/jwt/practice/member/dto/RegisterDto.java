package com.jwt.practice.member.dto;

import javax.validation.constraints.Email;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {
	@JsonProperty("email")
	@Email(message = "Invalid email format")
	private String email;

	@JsonProperty("password")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,16}", message = "Invalid password format")
	private String password;

	@JsonProperty("name")
	@Pattern(regexp = "^[가-힣]{2,6}$", message = "Invalid name format")
	private String name;

	@JsonProperty("birth")
	@Pattern(regexp = "^(19|20)\\d\\d(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$", message = "Invalid date of birth format")
	private String birth;

	@JsonProperty("member_image")
	private String memberImage;
}
