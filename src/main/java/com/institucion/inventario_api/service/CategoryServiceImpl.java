package com.institucion.inventario_api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.institucion.inventario_api.dto.ApiResponse;
import com.institucion.inventario_api.dto.CategoryListResponse;
import com.institucion.inventario_api.dto.CategoryRequest;
import com.institucion.inventario_api.dto.CategoryResponse;
import com.institucion.inventario_api.dto.LoginResponse;
import com.institucion.inventario_api.entity.Category;
import com.institucion.inventario_api.exception.BadRequestException;
import com.institucion.inventario_api.exception.ResourceNotFoundException;
import com.institucion.inventario_api.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public ApiResponse<CategoryResponse> create(CategoryRequest request) {

        if (categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException(
                    "La categoría ya existe");
        }

        Category category = Category.builder()
                .name(request.getName())
                .code(request.getCode())
                .build();

        Category savedCategory =
                categoryRepository.save(category);

        CategoryResponse data = new CategoryResponse();

        data.setId(savedCategory.getId());
        data.setCode(savedCategory.getCode());
        data.setName(savedCategory.getName());

        return 
            ApiResponse.<CategoryResponse>builder()
                    .status(200)
                    .message("registro exitoso")
                    .data(data)
                    .build();
    }

    @Override
    public CategoryListResponse findAll() {

        List<CategoryResponse> listCategories =
         categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
        
        CategoryListResponse response = new CategoryListResponse();
        response.setCategories(listCategories);

        return response;
    }

    @Override
    public CategoryResponse findById(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Categoría no encontrada"));

        return mapToResponse(category);
    }

    @Override
    public ApiResponse<CategoryResponse> update(
            Long id,
            CategoryRequest request) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Categoría no encontrada"));

        category.setName(request.getName());
        category.setCode(
                request.getCode());

        Category updatedCategory =
                categoryRepository.save(category);

        return 
            ApiResponse.<CategoryResponse>builder()
                    .status(200)
                    .message("registro exitoso")
                    .data(mapToResponse(updatedCategory))
                    .build();
    }

    @Override
    public void delete(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Categoría no encontrada"));

        category.setActive(false);
        categoryRepository.save(category);
    }

    private CategoryResponse mapToResponse(
            Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .code(
                        category.getCode())
                .build();
    }
}