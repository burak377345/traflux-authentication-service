package com.traflux.TrafluxAuthenticationService.services;

import com.traflux.TrafluxAuthenticationService.entities.UserModel;
import com.traflux.TrafluxAuthenticationService.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @Test
    void getCurrentUser_ShouldReturnUser_WhenAccessTokenExists() {
        // Arrange (Test verileri hazırlanıyor)
        Cookie[] cookies = { new Cookie("access_token", "valid-token") };
        Long userId = 123L;
        UserModel expectedUser = new UserModel();
        expectedUser.setId(userId);

        // Mock davranışları
        when(jwtTokenProvider.getUserIdFromJwt("valid-token")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(expectedUser);

        // Act (Test edilen metot çağrılıyor)
        UserModel actualUser = authService.getCurrentUser(cookies);

        // Assert (Sonuç doğrulanıyor)
        assertNotNull(actualUser);
        assertEquals(expectedUser.getId(), actualUser.getId());
    }

    @Test
    void getCurrentUser_ShouldThrowException_WhenCookiesAreNull() {
        // Arrange & Act & Assert
        assertThrows(RuntimeException.class, () -> authService.getCurrentUser(null));
    }

    @Test
    void getCurrentUser_ShouldThrowException_WhenAccessTokenIsMissing() {
        // Arrange
        Cookie[] cookies = { new Cookie("session_id", "some-session") }; // access_token yok

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.getCurrentUser(cookies));
    }

    @Test
    void getCurrentUser_ShouldThrowException_WhenTokenIsInvalid() {
        // Arrange
        Cookie[] cookies = { new Cookie("access_token", "invalid-token") };

        // Mock davranışı: Geçersiz token verildiğinde null veya hata döndürsün
        when(jwtTokenProvider.getUserIdFromJwt("invalid-token")).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.getCurrentUser(cookies));
    }

    @Test
    void getCurrentUser_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        Cookie[] cookies = { new Cookie("access_token", "valid-token") };
        Long userId = 123L;

        // Mock davranışı: Kullanıcı bulunamazsa null döndür
        when(jwtTokenProvider.getUserIdFromJwt("valid-token")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.getCurrentUser(cookies));
    }
}
