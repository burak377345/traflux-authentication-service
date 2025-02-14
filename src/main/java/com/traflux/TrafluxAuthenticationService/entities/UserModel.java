package com.traflux.TrafluxAuthenticationService.entities;


import com.traflux.TrafluxAuthenticationService.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
