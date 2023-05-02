package com.sneakerspick.service.impl;

import com.sneakerspick.Model.entity.AppUser;
import com.sneakerspick.Model.entity.Role;
import com.sneakerspick.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private AppUserRepository appUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser =
				appUserRepository.findDistinctTopByUsername(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new User(appUser.getUsername(), appUser.getPassword(),
				appUser.getAuthorities());
	}

	/*private List<SimpleGrantedAuthority> convertAuthoritiesToRole(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	}*/

}
