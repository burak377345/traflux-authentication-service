package com.traflux.TrafluxAuthenticationService.services;


import com.traflux.TrafluxAuthenticationService.entities.RefreshTokenModel;
import com.traflux.TrafluxAuthenticationService.entities.UserModel;
import com.traflux.TrafluxAuthenticationService.repositories.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class RefreshTokenService {

	@Value("${traflux.refresh.token.app.secret}")
	String APP_SECRET;
	
	@Value("${traflux.refresh.token.expires.in}")
	Long EXPIRES_IN;


	
	private RefreshTokenRepository refreshTokenRepository;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
	}
	
	public String createRefreshToken(UserModel user) {
		// Token süresi
		Instant expiryDate = Instant.now().plusSeconds(EXPIRES_IN);

		// JWT Refresh Token oluşturma
		String refreshToken = Jwts.builder()
				.setSubject(user.getId().toString()) // Kullanıcı ID'sini 'sub' alanına koy
				.setIssuedAt(new Date()) // Token oluşturulma tarihi
				.setExpiration(Date.from(expiryDate)) // Token son kullanma tarihi
				.signWith(SignatureAlgorithm.HS512, APP_SECRET) // Güvenlik için bir secretKey ile imzala
				.compact();

		RefreshTokenModel token = refreshTokenRepository.findByUserId(user.getId());
		if(token == null) {
			token =	new RefreshTokenModel();
			token.setUser(user);
		}
		token.setToken(refreshToken);
		token.setExpiryDate(Date.from(Instant.now().plusSeconds(EXPIRES_IN)));
		refreshTokenRepository.save(token);
		return refreshToken;
	}
	
	public boolean isRefreshExpired(RefreshTokenModel token) {
		return token.getExpiryDate().before(new Date());
	}

	public RefreshTokenModel getByUser(Long userId) {
		return refreshTokenRepository.findByUserId(userId);	
	}

}
