package com.institucion.inventario_api.dto;

public record ReportResponse(
        Integer status,
        String message,
        String fileName,
        String fileBase64
) {
}