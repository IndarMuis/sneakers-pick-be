package com.sneakerspick.service;

import com.sneakerspick.Model.entity.AppUser;
import com.sneakerspick.Model.request.LoginRequest;
import com.sneakerspick.Model.request.RegisterRequest;
import com.sneakerspick.Model.response.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AppUserService {

	AuthResponse register(RegisterRequest request);
	AuthResponse login(LoginRequest request, UserDetails userDetails);

}
