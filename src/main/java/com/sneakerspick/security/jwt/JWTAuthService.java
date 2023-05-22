package com.sneakerspick.security.jwt;

import com.sneakerspick.domains.AppUser;
import com.sneakerspick.domains.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@AllArgsConstructor
public class JWTAuthService {

	private Key key;

	public String generateAccessToken(AppUser appUser) {
		LocalDateTime currentTIme = LocalDateTime.now();
		Date expirationDate = Date.from(currentTIme.plusMinutes(30).atZone(ZoneId.of("Asia/Jakarta")).toInstant());
		return Jwts.builder()
				.setIssuer("sneakerspick")
				.setHeaderParam("typ", "JWT")
				.setExpiration(expirationDate)
				.claim("id_profil", appUser.getId())
				.claim("email", appUser.getName())
				.claim("scope", appUser.getRoles().stream().map(Role::getName))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public Claims claimsAccessToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key).build()
				.parseClaimsJws(token).getBody();
	}

}
