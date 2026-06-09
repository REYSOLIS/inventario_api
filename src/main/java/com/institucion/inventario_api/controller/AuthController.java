package com.institucion.inventario_api.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.institucion.inventario_api.dto.ApiResponse;
import com.institucion.inventario_api.dto.LoginRequest;
import com.institucion.inventario_api.dto.LoginResponse;
import com.institucion.inventario_api.dto.RegisterRequest;
import com.institucion.inventario_api.dto.RegisterResponse;
import com.institucion.inventario_api.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}