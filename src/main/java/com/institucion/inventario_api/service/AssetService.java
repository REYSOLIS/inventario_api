package com.institucion.inventario_api.service;

import java.util.List;

import com.institucion.inventario_api.dto.AssetRequest;
import com.institucion.inventario_api.dto.AssetResponse;

public interface AssetService {

    AssetResponse create(AssetRequest request);

    List<AssetResponse> findAll();

    AssetResponse findById(Long id);

    AssetResponse update(Long id, AssetRequest request);

    void delete(Long id);

}