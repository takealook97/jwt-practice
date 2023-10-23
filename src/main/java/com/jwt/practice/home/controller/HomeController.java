package com.jwt.practice.home.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {
	@GetMapping("/hello")
	public ResponseEntity<String> home() {
		return ResponseEntity.ok("hello");
	}
}
