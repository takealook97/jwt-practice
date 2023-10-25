package com.jwt.practice.member.service;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwt.practice.exception.DuplicateMemberException;
import com.jwt.practice.exception.NotFoundMemberException;
import com.jwt.practice.member.domain.Authority;
import com.jwt.practice.member.domain.Member;
import com.jwt.practice.member.dto.MemberDto;
import com.jwt.practice.member.repository.MemberRepository;
import com.jwt.practice.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public MemberDto register(MemberDto memberDto) {
		if (memberRepository.findOneWithAuthoritiesByEmail(memberDto.getEmail()).orElse(null) != null) {
			throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
		}

		Authority authority = Authority.builder()
			.authorityName("ROLE_USER")
			.build();

		Member member = Member.builder()
			.email(memberDto.getEmail())
			.password(passwordEncoder.encode(memberDto.getPassword()))
			.name(memberDto.getName())
			.authorities(Collections.singleton(authority))
			.activated(true)
			.build();

		return MemberDto.from(memberRepository.save(member));
	}

	// 어떠한 이메일이든 해당하는 유저 정보와 권한 정보를 가져오는 메서드
	public MemberDto getMemberWithAuthorities(String email) {
		return MemberDto.from(memberRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
	}

	// 현재 security context에 저장되어있는 유저 정보와 권한 정보만을 가져오는 메서드
	public MemberDto getMyMemberWithAuthorities() {
		return MemberDto.from(
			SecurityUtil.getCurrentEmail()
				.flatMap(memberRepository::findOneWithAuthoritiesByEmail)
				.orElseThrow(() -> new NotFoundMemberException("Member not found"))
		);
	}
}
