package com.traflux.TrafluxAuthenticationService.services;

import com.traflux.TrafluxAuthenticationService.entities.UserModel;
import com.traflux.TrafluxAuthenticationService.repositories.UserRepository;
import com.traflux.TrafluxAuthenticationService.security.JwtUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserModel user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		return JwtUserDetails.create(user);
	}

	public UserDetails loadUserById(Long id) {
		UserModel user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
		return JwtUserDetails.create(user);
	}
}
