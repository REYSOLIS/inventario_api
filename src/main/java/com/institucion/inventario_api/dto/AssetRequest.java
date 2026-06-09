package com.institucion.inventario_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.institucion.inventario_api.utils.enums.AssetStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetRequest {

    private String folio;
    private String serialNumber;
    private String model;
    private AssetStatus status;
    private BigDecimal purchaseValue;
    private LocalDate acquisitionDate;
    private Long categoryId;  
}