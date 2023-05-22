package com.sneakerspick.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}

	@GetMapping("/admin")
	public String admin() {
		return "Admin";
	}

}
