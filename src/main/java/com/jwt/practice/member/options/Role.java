package com.jwt.practice.member.options;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
	ROLE_GUEST("ROLE_GUEST"),
	ROLE_USER("ROLE_USER"),
	ROLE_ADMIN("ROLE_ADMIN");

	private final String role;
}
