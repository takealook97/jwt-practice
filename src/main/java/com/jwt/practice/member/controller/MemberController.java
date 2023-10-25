package com.jwt.practice.member.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.practice.member.dto.MemberDto;
import com.jwt.practice.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
	private final MemberService memberService;

	@PostMapping("/test-redirect")
	public void testRedirect(HttpServletResponse response) throws IOException, IOException {
		response.sendRedirect("/api/user");
	}

	@PostMapping("/register")
	public ResponseEntity<MemberDto> register(@Valid @RequestBody MemberDto memberDto) {
		System.out.println(memberDto);
		return ResponseEntity.ok(memberService.register(memberDto));
	}

	@GetMapping("/member")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<MemberDto> getMyUserInfo(HttpServletRequest request) {
		return ResponseEntity.ok(memberService.getMyMemberWithAuthorities());
	}

	@GetMapping("/member/{email}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<MemberDto> getUserInfo(@PathVariable String email) {
		return ResponseEntity.ok(memberService.getMemberWithAuthorities(email));
	}
}
