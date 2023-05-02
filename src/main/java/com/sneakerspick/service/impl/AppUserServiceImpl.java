package com.sneakerspick.service.impl;

import com.sneakerspick.Model.entity.AppUser;
import com.sneakerspick.Model.entity.Role;
import com.sneakerspick.Model.request.LoginRequest;
import com.sneakerspick.Model.request.RegisterRequest;
import com.sneakerspick.Model.response.AuthResponse;
import com.sneakerspick.exception.DataNotFoundException;
import com.sneakerspick.repository.AppUserRepository;
import com.sneakerspick.repository.RoleRepository;
import com.sneakerspick.security.JWTAuthService;
import com.sneakerspick.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.management.relation.RoleNotFoundException;
import java.util.Collections;

@AllArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

	private AppUserRepository appUserRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private JWTAuthService jwtAuthService;

	private AuthenticationManager authenticationManager;

	@Override
	public AuthResponse register(RegisterRequest request) {

		AppUser appUser = new AppUser();
		Role role = roleRepository.findById(1).orElseThrow(() -> new DataNotFoundException("Role not found"));

		appUser.setUsername(request.getUsername());
		appUser.setPassword(passwordEncoder.encode(request.getPassword()));
		appUser.setRoles(Collections.singletonList(role));
		appUserRepository.save(appUser);
		String jwtToken = jwtAuthService.generateToken(appUser);

		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword())
		);
		SecurityContextHolder.getContext().setAuthentication(authenticate);

		return AuthResponse.builder()
				.userId(appUser.getId())
				.username(appUser.getUsername())
				.token(jwtToken)
				.build();
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		return null;
	}
}
