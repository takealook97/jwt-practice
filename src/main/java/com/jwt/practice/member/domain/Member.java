package com.jwt.practice.member.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import net.minidev.json.annotate.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "member")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(name = "email", nullable = false, unique = true)
	@NotNull
	// @JsonIgnore// JSON 직렬화, 역직렬화에서 속성을 무시
	private String email;

	@Column(name = "password", nullable = false)
	@NotNull
	private String password;

	@Column(name = "name", nullable = false)
	@NotNull
	private String name;

	@Column(name = "activated", nullable = false)
	@NotNull
	private Boolean activated;

	@ManyToMany
	@JoinTable(
		name = "member_authority",
		joinColumns = {@JoinColumn(name = "member_id")},
		inverseJoinColumns = {@JoinColumn(name = "authority_name")}
	)
	private Set<Authority> authorities;
}
