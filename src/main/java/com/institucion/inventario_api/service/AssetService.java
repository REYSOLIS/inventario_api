package com.institucion.inventario_api.service;

import java.math.BigDecimal;
import java.util.List;

import com.institucion.inventario_api.dto.ApiResponse;
import com.institucion.inventario_api.dto.AssetListResponse;
import com.institucion.inventario_api.dto.AssetRequest;
import com.institucion.inventario_api.dto.AssetResponse;
import com.institucion.inventario_api.dto.AssetSearchRequest;
import com.institucion.inventario_api.dto.PageResponse;
import com.institucion.inventario_api.utils.enums.AssetStatus;
import org.springframework.data.domain.Pageable;

public interface AssetService {

    ApiResponse<AssetResponse> create(AssetRequest request);

    AssetListResponse findAll();

    AssetResponse findById(Long id);

    AssetResponse findBySerialNumber(String serialNumber);

    AssetResponse findByModel(String model);

    AssetListResponse findByCategoryId(Long id);

    AssetListResponse findByStatus(AssetStatus status);

    AssetListResponse findByPurchaseValue(BigDecimal purchaseValue);

    AssetResponse update(Long id, AssetRequest request);

    void delete(Long id);

    PageResponse<AssetResponse> searchAssets(
            AssetSearchRequest request,
            Pageable pageable
    );
}