package com.sneakerspick.controller;

import com.sneakerspick.Model.request.LoginRequest;
import com.sneakerspick.Model.request.RegisterRequest;
import com.sneakerspick.Model.response.APIResponse;
import com.sneakerspick.Model.response.AuthResponse;
import com.sneakerspick.service.AppUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@AllArgsConstructor
@RestController
@Slf4j
public class AuthController {

	private AppUserService appUserService;
	private AuthenticationManager authenticationManager;

	@PostMapping("/v1/auth/register")
	public ResponseEntity<APIResponse<?>> register(@RequestBody RegisterRequest request) {
		AuthResponse register = appUserService.register(request);

		APIResponse<?> response = APIResponse.builder()
				.message("Register Success")
				.code(HttpStatus.CREATED.value())
				.response(register)
				.build();

		return ResponseEntity.created(URI.create("/v1/auth/register")).body(response);
	}

	@PostMapping("/v1/auth/login")
	public ResponseEntity<APIResponse<?>> login(@RequestBody LoginRequest request) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		AuthResponse loginResponse = appUserService.login(request, userDetails);
		APIResponse<?> response = APIResponse.builder()
				.message("Login Success")
				.code(HttpStatus.OK.value())
				.response(loginResponse)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
