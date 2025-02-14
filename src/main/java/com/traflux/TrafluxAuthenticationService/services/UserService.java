package com.traflux.TrafluxAuthenticationService.services;

import com.traflux.TrafluxAuthenticationService.entities.UserModel;
import com.traflux.TrafluxAuthenticationService.helper.ErrorMessages;
import com.traflux.TrafluxAuthenticationService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel getOneUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public UserModel getOneUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
    public UserModel saveOneUser(UserModel newUser) {
        return userRepository.save(newUser);
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> users = userRepository.findAll();
        return users;
    }

    public UserModel getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.getUserNotFound()));
    }
}
