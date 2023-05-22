package com.sneakerspick.services.impl;

import com.sneakerspick.domains.AppUser;
import com.sneakerspick.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private AppUserRepository appUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = appUserRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("user not found"));

		return null;
	}

	/*private GrantedAuthority convertRoleToAuthority(List<Role> roles) {
		List<SimpleGrantedAuthority> collect = roles.stream().map(r -> new SimpleGrantedAuthority(r.getName()))
				.collect(Collectors.toList());
		return collec;
	}*/
}
