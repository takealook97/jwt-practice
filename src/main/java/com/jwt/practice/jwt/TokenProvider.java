package com.jwt.practice.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider implements InitializingBean {
	private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
	private static final String AUTHORITIES_KEY = "auth";
	private final String secret;
	private final long tokenValidityInMilliSeconds;
	private Key key;

	public TokenProvider(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
		this.secret = secret;
		this.tokenValidityInMilliSeconds = tokenValidityInSeconds * 1000;
	}

	@Override
	public void afterPropertiesSet() {// 빈생성, 의존성 주입 후 주입받은 secret값을 decode한 뒤 키 변수에 할당하기 위함
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createToken(Authentication authentication) {// Authentication 객체의 권한정보를 통해 토큰 생성
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));// 권한 가져오

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInMilliSeconds);// 토큰 expire time 설정

		return Jwts.builder()// jwt 토큰 생성 후 리턴
			.setSubject(authentication.getName())
			.claim(AUTHORITIES_KEY, authorities)
			.signWith(SignatureAlgorithm.HS512, key)
			.setExpiration(validity)
			.compact();
	}

	public Authentication getAuthentication(String token) {// 토큰에 담겨있는 정보를 통해 Authentication 객체 리턴
		Claims claims = Jwts.parser()// 토큰을 통해 Claim 생성
			.setSigningKey(key)
			.parseClaimsJws(token)
			.getBody();

		Collection<? extends GrantedAuthority> authorities =// claim에서 권한정보들을 가져온다.
			Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);// 유저 객체 생성

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String token) {// 토큰 유효성 검사 메서드
		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);// 파싱 시
			return true;// 문제 없다면 true 리턴
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			logger.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			logger.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			logger.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			logger.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}
}
