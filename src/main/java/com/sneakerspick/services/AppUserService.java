package com.sneakerspick.services;

import com.sneakerspick.dtos.response.AppUserResponseDTO;

public interface AppUserService {

	AppUserResponseDTO findByEmail(String email);

	boolean isExistingEmail(String email);

}
