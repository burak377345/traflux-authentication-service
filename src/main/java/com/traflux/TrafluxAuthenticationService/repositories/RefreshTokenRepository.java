package com.traflux.TrafluxAuthenticationService.repositories;

import com.traflux.TrafluxAuthenticationService.entities.RefreshTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, Long>{

	RefreshTokenModel findByUserId(Long userId);
	
}
