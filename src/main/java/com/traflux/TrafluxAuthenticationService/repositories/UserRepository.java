package com.traflux.TrafluxAuthenticationService.repositories;

import com.traflux.TrafluxAuthenticationService.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
    UserModel findByUserName(String userName);

    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}
