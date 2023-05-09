package com.sneakerspick.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private JWTAuthService jwtAuthService;
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		String token = getTokenFromHeader(request);
		if (token != null && jwtAuthService.validatedToken(token)) {
			Claims claims = jwtAuthService.claimsJwtToken(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername((String) claims.get("username"));
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());
			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		filterChain.doFilter(request, response);
	}

	private String getTokenFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader(SecurityConstants.AUTH_HEADER);
		String tokenPrefix = SecurityConstants.JWT_PREFIX;
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {
			return bearerToken.substring(tokenPrefix.length());
		}
		return null;
	}
}
