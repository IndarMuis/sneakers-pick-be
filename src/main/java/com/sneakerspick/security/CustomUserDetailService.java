package com.sneakerspick.security;

import com.sneakerspick.domain.User;
import com.sneakerspick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            log.warn("Username {} unauthorized", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password is wrong");
        }

        return CustomUserDetail.
                builder()
                .id(user.get().getId())
                .name(user.get().getName())
                .email(user.get().getEmail())
                .username(user.get().getUsername())
                .password(user.get().getPassword())
                .authorities(user.get().getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()))
                .build();
    }


}
