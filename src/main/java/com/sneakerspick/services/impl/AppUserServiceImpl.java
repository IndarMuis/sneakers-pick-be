package com.sneakerspick.services.impl;

import com.sneakerspick.domains.AppUser;
import com.sneakerspick.dtos.response.AppUserResponseDTO;
import com.sneakerspick.repositories.AppUserRepository;
import com.sneakerspick.services.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

	private AppUserRepository appUserRepository;

	@Override
	public AppUserResponseDTO findByEmail(String email) {

		AppUser appUser = appUserRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Email not found"));

		return AppUserResponseDTO.builder()
				.name(appUser.getName())
				.email(appUser.getEmail())
				.profileId(appUser.getId())
				.build();
	}

	@Override
	public boolean isExistingEmail(String email) {
		return appUserRepository.existsByEmail(email);
	}
}
