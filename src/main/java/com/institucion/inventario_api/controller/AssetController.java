package com.institucion.inventario_api.controller;

import com.institucion.inventario_api.dto.ApiResponse;
import com.institucion.inventario_api.dto.AssetListResponse;
import com.institucion.inventario_api.dto.AssetRequest;
import com.institucion.inventario_api.dto.AssetResponse;
import com.institucion.inventario_api.dto.AssetSearchRequest;
import com.institucion.inventario_api.dto.PageResponse;
import com.institucion.inventario_api.service.AssetService;
import com.institucion.inventario_api.utils.enums.AssetStatus;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<ApiResponse<AssetResponse>> createAsset(
            @Valid @RequestBody AssetRequest request) {

        return ResponseEntity.ok(assetService.create(request));
    }

    @GetMapping
    public ResponseEntity<AssetListResponse> findAll() {

        return ResponseEntity.ok(assetService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(assetService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponse> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody AssetRequest request) {

        return ResponseEntity.ok(assetService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(
            @PathVariable Long id) {

        assetService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<AssetResponse>> searchAssets(
            @RequestBody AssetSearchRequest request,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(
                assetService.searchAssets(request, pageable)
        );
    }
}