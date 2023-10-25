package com.jwt.practice.member.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.practice.jwt.JwtFilter;
import com.jwt.practice.jwt.TokenProvider;
import com.jwt.practice.jwt.dto.TokenDto;
import com.jwt.practice.member.dto.LoginDto;

@RestController
@RequestMapping("/api")
public class AuthController {
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}

	@PostMapping("/authenticate")
	public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

		UsernamePasswordAuthenticationToken authenticationToken =// authentication 토큰 객체 생성
			new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
		// authenticate 메서드가 실행될 때 LoadUserByUserName 메서드가 실행된다.
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);// 객체를 security context에 저장

		String jwt = tokenProvider.createToken(authentication);// 인증 정보를 기준으로 토큰 생성

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);// response 헤더에 넣고

		return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);// body에도 넣는다.
	}
}
