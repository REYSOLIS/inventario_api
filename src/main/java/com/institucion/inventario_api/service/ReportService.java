package com.institucion.inventario_api.service;

import com.institucion.inventario_api.dto.AssetSearchRequest;
import com.institucion.inventario_api.dto.ReportResponse;

public interface ReportService {

    ReportResponse generateReport(
            AssetSearchRequest request,
            String username
    );

}