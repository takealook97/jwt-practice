package com.jwt.practice.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "authority")
public class Authority {
	@Id
	@Column(name = "authority_name")
	private String authorityName;
}
