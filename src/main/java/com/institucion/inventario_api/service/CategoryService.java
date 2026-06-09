package com.institucion.inventario_api.service;

import org.springframework.http.ResponseEntity;

import com.institucion.inventario_api.dto.ApiResponse;
import com.institucion.inventario_api.dto.CategoryListResponse;
import com.institucion.inventario_api.dto.CategoryRequest;
import com.institucion.inventario_api.dto.CategoryResponse;

public interface CategoryService {

    ApiResponse<CategoryResponse> create(CategoryRequest request);

    CategoryListResponse findAll();

    CategoryResponse findById(Long id);

    ApiResponse<CategoryResponse> update(Long id, CategoryRequest request);

    void delete(Long id);
}