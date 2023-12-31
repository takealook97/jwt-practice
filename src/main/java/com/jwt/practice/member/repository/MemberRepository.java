package com.jwt.practice.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.practice.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByMemberId(Long memberId);

	Optional<Member> findByEmail(String email);

	Boolean existsByEmail(String email);

	@EntityGraph(attributePaths = "authorities")// 조회 시 lazy x eager o
	Optional<Member> findOneWithAuthoritiesByEmail(String email);
}
