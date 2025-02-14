package com.traflux.TrafluxAuthenticationService.requests;

import lombok.Data;

@Data
public class UserRequest {
	String email;
	String userName;
	String password;
}
