package com.institucion.inventario_api.controller;

import com.institucion.inventario_api.dto.AssetSearchRequest;
import com.institucion.inventario_api.dto.ReportResponse;
import com.institucion.inventario_api.service.ReportService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> generateReport(
            @RequestBody AssetSearchRequest request,
            Authentication authentication) {

        ReportResponse response =
                reportService.generateReport(
                        request,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }
}