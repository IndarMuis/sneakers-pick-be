package com.sneakerspick.security;

import com.sneakerspick.Model.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class JWTAuthService {

	private Key key;

	public String generateToken(AppUser appUser) {

		Date currentDate = new Date();
		//AppUser appUser = (AppUser) authentication.getPrincipal();

		return Jwts.builder()
				.setIssuer("sneakerspick")
				.setHeaderParam("typ", "JWT")
				.setIssuedAt(currentDate)
				.setExpiration(new Date(currentDate.getTime() + SecurityConstants.EXPIRED_DATE))
				.claim("id_profil", appUser.getId())
				.claim("username", appUser.getUsername())
				.claim("scope", getRoleFromHeader(appUser.getAuthorities()))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public Claims claimsJwtToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public boolean validatedToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (JwtException exc) {
			throw new JwtException(exc.getMessage());
		} catch (Exception exc) {
			throw new RuntimeException(exc.getMessage());
		}
	}

	private List<String> getRoleFromHeader(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

	}

}
