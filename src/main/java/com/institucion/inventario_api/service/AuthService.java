package com.institucion.inventario_api.service;

import com.institucion.inventario_api.dto.LoginResponse;
import com.institucion.inventario_api.dto.RegisterRequest;
import com.institucion.inventario_api.dto.RegisterResponse;

import org.springframework.http.ResponseEntity;

import com.institucion.inventario_api.dto.ApiResponse;
import com.institucion.inventario_api.dto.LoginRequest;

public interface AuthService {

    ResponseEntity<ApiResponse<RegisterResponse>> register(RegisterRequest request);

    ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request);
}