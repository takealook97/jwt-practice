package com.jwt.practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.jwt.practice.jwt.JwtAccessDeniedHandler;
import com.jwt.practice.jwt.JwtAuthenticationEntryPoint;
import com.jwt.practice.jwt.JwtSecurityConfig;
import com.jwt.practice.jwt.TokenProvider;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
	private final TokenProvider tokenProvider;
	private final CorsFilter corsFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	public SecurityConfig(
		TokenProvider tokenProvider,
		CorsFilter corsFilter,
		JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
		JwtAccessDeniedHandler jwtAccessDeniedHandler
	) {
		this.tokenProvider = tokenProvider;
		this.corsFilter = corsFilter;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// token을 사용하는 방식이기 때문에 csrf를 disable
			.csrf(AbstractHttpConfigurer::disable)

			.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.accessDeniedHandler(jwtAccessDeniedHandler)
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			)

			.authorizeHttpRequests(
				authorizeHttpRequests -> authorizeHttpRequests// HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
					.antMatchers("/api/hello", "/api/authenticate", "/api/register").permitAll()// 인증 없이 접근을 허용하겠다.
					// .requestMatchers(PathRequest.toH2Console()).permitAll()
					.anyRequest().authenticated()// 나머지 요청들에 대해서는 인증을 받아야한다.
			)

			// 세션을 사용하지 않기 때문에 STATELESS로 설정
			.sessionManagement(sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			// // enable h2-console
			// .headers(headers ->
			// 	headers.frameOptions(options ->
			// 		options.sameOrigin()
			// 	)
			// )

			.apply(new JwtSecurityConfig(tokenProvider));
		return http.build();
	}
}