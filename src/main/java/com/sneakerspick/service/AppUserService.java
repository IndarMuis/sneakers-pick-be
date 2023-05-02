package com.sneakerspick.service;

import com.sneakerspick.Model.request.LoginRequest;
import com.sneakerspick.Model.request.RegisterRequest;
import com.sneakerspick.Model.response.AuthResponse;

public interface AppUserService {

	AuthResponse register(RegisterRequest request);
	AuthResponse login(LoginRequest request);

}
