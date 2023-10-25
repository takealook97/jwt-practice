package com.jwt.practice.member.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jwt.practice.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
	@JsonProperty("email")
	// @Email(message = "Invalid email format")
	private String email;

	@JsonProperty("password")
	// @Pattern(regexp = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,16}", message = "Invalid password format")
	private String password;

	@JsonProperty("name")
	// @Pattern(regexp = "^[가-힣]{2,6}$", message = "Invalid name format")
	private String name;

	private Set<AuthorityDto> authorityDtoSet;

	public static MemberDto from(Member member) {
		if (member == null)
			return null;

		return MemberDto.builder()
			.email(member.getEmail())
			.name(member.getName())
			.authorityDtoSet(member.getAuthorities().stream()
				.map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
				.collect(Collectors.toSet()))
			.build();
	}
}
