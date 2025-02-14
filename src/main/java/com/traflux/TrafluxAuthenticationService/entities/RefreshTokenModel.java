package com.traflux.TrafluxAuthenticationService.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name="refresh_token")
@Data
public class RefreshTokenModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	UserModel user;
	
	@Column(nullable = false, unique = true)
	String token;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	Date expiryDate;
}
