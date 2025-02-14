package com.traflux.TrafluxAuthenticationService.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.traflux.TrafluxAuthenticationService.entities.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtUserDetails implements UserDetails {

	public Long id;
	private String email;
	private String userName;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	private JwtUserDetails(Long id, String email, String userName, String password, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.authorities = authorities;
	}

	public static JwtUserDetails create(UserModel user) {
		List<GrantedAuthority> authoritiesList = new ArrayList<>();
		authoritiesList.add(new SimpleGrantedAuthority(user.getRole().name())); // Kullanıcının rolü eklendi
		return new JwtUserDetails(user.getId(), user.getEmail(), user.getUserName(), user.getPassword(), authoritiesList);
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
