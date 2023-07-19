package com.sneakerspick.security.filter;

import com.sneakerspick.domain.Role;
import com.sneakerspick.domain.User;
import com.sneakerspick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// TODO: 7/19/2023 TESTING
@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtAuthProcessingProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("START JWT AUTHENTICATION PROVIDER");

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            log.error("USERNAME NOT FOUND {}", username);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "username not found");
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            log.error("PASSWORD IS WRONG");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid username or password");
        }
        List<GrantedAuthority> grantedAuthorities = mapToGrantedAuthority(user.get().getRoles());
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private List<GrantedAuthority> mapToGrantedAuthority(Set<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (!roles.isEmpty()) {
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        }

        return authorities;
    }
}
