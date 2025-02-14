package com.traflux.TrafluxAuthenticationService.controllers;

import com.traflux.TrafluxAuthenticationService.entities.UserModel;
import com.traflux.TrafluxAuthenticationService.helper.ErrorMessages;
import com.traflux.TrafluxAuthenticationService.requests.UserRequest;
import com.traflux.TrafluxAuthenticationService.responses.AuthResponse;
import com.traflux.TrafluxAuthenticationService.security.JwtTokenProvider;
import com.traflux.TrafluxAuthenticationService.services.AuthService;
import com.traflux.TrafluxAuthenticationService.services.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/getme")
    public ResponseEntity<?> getLoggedInUserInfo(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            UserModel user = authService.getCurrentUser(cookies);

            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.getLoggedAccountNotFound());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.getLoggedAccountFailed());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequest loginRequest, HttpServletResponse response) {
        try {
            AuthResponse loginedUser = authService.loginedUser(loginRequest.getEmail(), loginRequest.getPassword());

            // Authorization header için Access Token ve Refresh Token ekleme
            HttpHeaders headers = new HttpHeaders();
            headers.set("Access-Token", loginedUser.getAccessToken());
            headers.set("Refresh-Token", loginedUser.getRefreshToken());

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(loginedUser.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.getInvalidCredentials());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.getLoginFailed());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest registerRequest, HttpServletResponse response) {
        try {
            AuthResponse registeredUser = authService.registeredUser(registerRequest.getEmail(), registerRequest.getUserName(), registerRequest.getPassword());

            // Authorization header için Access Token ve Refresh Token ekleme
            HttpHeaders headers = new HttpHeaders();
            headers.set("Access-Token", registeredUser.getAccessToken());
            headers.set("Refresh-Token", registeredUser.getRefreshToken());

            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(registeredUser.getMessage());
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();

            if (errorMessage.equals(ErrorMessages.getEmailExists()) ||
                    errorMessage.equals(ErrorMessages.getUserNameExists())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage); // 409 Conflict
            } else if (errorMessage.equals(ErrorMessages.getInvalidEmail()) ||
                    errorMessage.equals(ErrorMessages.getInvalidUserName()) ||
                    errorMessage.equals(ErrorMessages.getInvalidPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage); // 400 Bad Request
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage); // Genel olarak 400
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.getRegisterFailed());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        try {

            // Access Token'ı silme
            Cookie accessTokenCookie = new Cookie("access_token", "");
            accessTokenCookie.setPath("/");
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true);
            accessTokenCookie.setMaxAge(0); // Çerezi tamamen sil
            accessTokenCookie.setAttribute("SameSite", "None");


            // Refresh Token'ı silme
            Cookie refreshTokenCookie = new Cookie("refresh_token", "");
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setMaxAge(0); // Çerezi tamamen sil
            refreshTokenCookie.setAttribute("SameSite", "None");

            // Yanıta temizlenmiş çerezleri ekle
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);


            return ResponseEntity.ok("Çıkış başarılı.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Çıkış işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request) {
        try {
            // Token yenileme işlemi
            AuthResponse refreshedResponse = authService.refreshedToken(request);

            // Authorization header için Access Token ve Refresh Token ekleme
            HttpHeaders headers = new HttpHeaders();
            headers.set("Access-Token", refreshedResponse.getAccessToken());

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(refreshedResponse.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
