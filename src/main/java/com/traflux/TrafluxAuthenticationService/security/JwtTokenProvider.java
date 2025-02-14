package com.traflux.TrafluxAuthenticationService.security;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.traflux.TrafluxAuthenticationService.entities.UserModel;
import com.traflux.TrafluxAuthenticationService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	@Value("${traflux.access.token.app.secret}")
	private String APP_SECRET;

	@Value("${traflux.refresh.token.app.secret}")
	private String REFRESH_APP_SECRET;

	@Value("${traflux.access.token.expires.in}")
	private long EXPIRES_IN;

	@Autowired
	UserRepository userRepository;


	public String generateJwtToken(Authentication auth) {
		JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
		Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
		String roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		return Jwts.builder()
				.setSubject(Long.toString(userDetails.getId()))
				.claim("email", userDetails.getEmail())
				.claim("name", userDetails.getUsername())
				.claim("roles", roles)
				.setIssuedAt(new Date())
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, APP_SECRET)
				.compact();
	}

	public String generateJwtTokenByUserId(Long userId) {
		Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
		UserModel userDetails = userRepository.getById(userId);
		String roles = String.valueOf(userDetails.getRole());
		return Jwts.builder()
				.setSubject(Long.toString(userId))
				.claim("email", userDetails.getEmail())
				.claim("name", userDetails.getUserName())
				.claim("roles", roles)
				.setIssuedAt(new Date())
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, APP_SECRET)
				.compact();
	}

	public Long getUserIdFromJwt(String token) {
		Claims claims = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

	public Long getUserIdFromRefreshJwt(String token) {
		Claims claims = Jwts.parser().setSigningKey(REFRESH_APP_SECRET).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

}
