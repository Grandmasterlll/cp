package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      JwtService jwtService, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        var user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setRoles(Set.of(Role.ROLE_USER));
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return new RegisterResponse(jwtToken);
    }

    public LoginResponse login(LoginRequest request) {
        var user = userDetailsService.loadUserByUsername(request.username());
        var jwtToken = jwtService.generateToken(user);
        
        return new LoginResponse(jwtToken, jwtToken, 
                jwtService.extractClaim(jwtToken, claims -> claims.getExpiration().getTime()));
    }

    public RefreshTokenResponse refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            String newAccessToken = jwtService.generateToken(userDetails);
            return new RefreshTokenResponse(newAccessToken);
        }

        throw new RuntimeException("Invalid refresh token");
    }

    public void logout(HttpServletRequest request) {
        // TODO: Добавить логику блокировки токена в Redis
    }

    // Request/Response DTOs using records
    public record RegisterRequest(String username, String password, String email) {}
    public record LoginRequest(String username, String password) {}
    public record RegisterResponse(String accessToken) {}
    public record LoginResponse(String accessToken, String refreshToken, long expiresIn) {}
    public record RefreshTokenResponse(String accessToken) {}
}
