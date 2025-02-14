package com.traflux.TrafluxAuthenticationService.requests;

import lombok.Data;

@Data
public class RefreshRequest {

	Long userId;
	String refreshToken;
}
