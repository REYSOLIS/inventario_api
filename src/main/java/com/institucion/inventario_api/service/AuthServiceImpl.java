package com.institucion.inventario_api.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.institucion.inventario_api.dto.ApiResponse;
import com.institucion.inventario_api.dto.LoginRequest;
import com.institucion.inventario_api.dto.LoginResponse;
import com.institucion.inventario_api.dto.RegisterRequest;
import com.institucion.inventario_api.dto.RegisterResponse;
import com.institucion.inventario_api.entity.RoleName;
import com.institucion.inventario_api.entity.User;
import com.institucion.inventario_api.exception.BadRequestException;
import com.institucion.inventario_api.repository.UserRepository;
import com.institucion.inventario_api.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<ApiResponse<RegisterResponse>> register(RegisterRequest request) {

        if (userRepository.existsByUsername(
                request.getUsername())) {

                throw new BadRequestException(
                        "El username ya existe");
        }

        if (userRepository.existsByEmail(
                request.getEmail())) {

                throw new BadRequestException(
                        "El email ya existe");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .role(RoleName.valueOf(request.getRol()))
                .build();

        User userEntity = userRepository.save(user);

        RegisterResponse data = new RegisterResponse();

        data.setUsername(userEntity.getUsername());
        data.setEmail(userEntity.getEmail());
        data.setRole(userEntity.getRole().name());

        return ResponseEntity.ok(
            ApiResponse.<RegisterResponse>builder()
                    .status(200)
                    .message("Usuario registrado correctamente")
                    .data(data)
                    .build());

    }

    @Override
    public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()));

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token =
                jwtService.generateToken(
                        userDetails);

        LoginResponse data = new LoginResponse();

       User user = userRepository
        .findByUsername(request.getUsername())
        .orElseThrow(() ->
                new BadRequestException("Usuario no encontrado"));

        data.setToken(token);
        data.setUsername(user.getUsername());
        data.setRole(user.getRole().name());

        return ResponseEntity.ok(
            ApiResponse.<LoginResponse>builder()
                    .status(200)
                    .message("Login exitoso")
                    .data(data)
                    .build());
    }
}
