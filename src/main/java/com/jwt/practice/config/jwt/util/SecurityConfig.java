package com.jwt.practice.config.jwt.util;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity// 기본적인 보안을 활성화 하겠다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring()
			.antMatchers(
				// "/h2-console/**",
				"/favicon.ico"
			);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()// HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
			.antMatchers("/api/hello").permitAll()// 인증 없이 접근을 허용하겠다.
			.anyRequest().authenticated();// 나머지 요청들에 대해서는 인증을 받아야한다.
	}
}
