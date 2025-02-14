package com.traflux.TrafluxAuthenticationService.services;

import com.traflux.TrafluxAuthenticationService.entities.RefreshTokenModel;
import com.traflux.TrafluxAuthenticationService.entities.UserModel;
import com.traflux.TrafluxAuthenticationService.enums.UserRole;
import com.traflux.TrafluxAuthenticationService.helper.CheckValid;
import com.traflux.TrafluxAuthenticationService.helper.ErrorMessages;
import com.traflux.TrafluxAuthenticationService.helper.SuccessMessages;
import com.traflux.TrafluxAuthenticationService.responses.AuthResponse;
import com.traflux.TrafluxAuthenticationService.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AuthService {

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    private RefreshTokenService refreshTokenService;


    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                       UserService userService, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    public UserModel getCurrentUser(Cookie[] cookies) {
        if (cookies == null) {
            throw new RuntimeException();
        }

        String accessToken = null;

        for (Cookie cookie : cookies) {
            if ("access_token".equals(cookie.getName())) {
                accessToken = cookie.getValue();
                break;
            }
        }

        if (accessToken == null) {
            throw new RuntimeException();
        }

        Long userId = jwtTokenProvider.getUserIdFromJwt(accessToken);
        UserModel user = userService.getUserById(userId);

        if (user == null) {
            throw new RuntimeException();
        }

        return user;
    }

    public AuthResponse loginedUser(String email, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);
        UserModel user = userService.getOneUserByEmail(email);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(jwtToken);
        authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        authResponse.setMessage(SuccessMessages.getSuccessLogin());
        return authResponse;
    }

    public AuthResponse registeredUser(String email, String userName, String password) {
        AuthResponse authResponse = new AuthResponse();
        if (userService.getOneUserByEmail(email) != null) {
            throw new RuntimeException(ErrorMessages.getEmailExists());
        }
        if (userService.getOneUserByUserName(userName) != null) {
            throw new RuntimeException(ErrorMessages.getUserNameExists());
        }
        if (!CheckValid.validateEmail(email)) {
            throw new RuntimeException(ErrorMessages.getInvalidEmail());
        }
        if (!CheckValid.validateUserName(userName)) {
            throw new RuntimeException(ErrorMessages.getInvalidUserName());
        }
        if (!CheckValid.validatePassword(password)) {
            throw new RuntimeException(ErrorMessages.getInvalidPassword());
        }
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.USER);
        userService.saveOneUser(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);

        authResponse.setMessage(SuccessMessages.getSuccessRegister());
        authResponse.setAccessToken(jwtToken);
        authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        return authResponse;
    }

    public AuthResponse refreshedToken(HttpServletRequest request) {
        // Çerezleri kontrol et
        if (request.getCookies() == null) {
            throw new RuntimeException(ErrorMessages.getInvalidToken());
        }

        // Refresh token'ı al ve boş olup olmadığını kontrol et
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new RuntimeException(ErrorMessages.getInvalidToken());
        }

        Long userId;
        try {
            // Token geçerliliğini kontrol et
            userId = jwtTokenProvider.getUserIdFromRefreshJwt(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.getInvalidToken());
        }

        RefreshTokenModel token = refreshTokenService.getByUser(userId);
        if (token == null || token.getToken() == null || !token.getToken().equals(refreshToken)) {
            throw new RuntimeException(ErrorMessages.getInvalidToken());
        }

        // Token süresi kontrolü
        if (refreshTokenService.isRefreshExpired(token)) {
            throw new RuntimeException(ErrorMessages.getInvalidToken());
        }

        // Yeni JWT oluştur ve yanıtı hazırla
        UserModel user = token.getUser();
        String jwtToken = jwtTokenProvider.generateJwtTokenByUserId(user.getId());
        AuthResponse response = new AuthResponse();
        response.setMessage(SuccessMessages.getSuccessRefreshedToken());
        response.setAccessToken(jwtToken);

        return response;
    }

}
