package com.institucion.inventario_api.dto;

import java.math.BigDecimal;

import com.institucion.inventario_api.utils.enums.AssetStatus;

public record AssetSearchRequest(
        String serialNumber,
        String model,
        AssetStatus status,
        Long categoryId,
        BigDecimal minPurchaseValue,
        BigDecimal maxPurchaseValue
) { 
}
