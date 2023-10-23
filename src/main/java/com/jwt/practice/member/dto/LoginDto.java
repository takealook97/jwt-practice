package com.jwt.practice.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
	@JsonProperty("email")
	@Email(message = "Invalid email format")
	private String email;

	@JsonProperty("password")
	private String password;
}
