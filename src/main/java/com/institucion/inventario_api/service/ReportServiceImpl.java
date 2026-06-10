package com.institucion.inventario_api.service;

import com.institucion.inventario_api.dto.AssetSearchRequest;
import com.institucion.inventario_api.dto.AssetResponse;
import com.institucion.inventario_api.dto.ReportResponse;
import com.institucion.inventario_api.repository.AssetRepository;
import com.institucion.inventario_api.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Base64;
import java.util.List;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final AssetRepository assetRepository;

    @Override
    public ReportResponse generateReport(
            AssetSearchRequest request,
            String username) {

        try {

            Page<AssetResponse> page =
            assetRepository.searchAssets(
                    request.serialNumber(),
                    request.model(),
                    request.status(),
                    request.categoryId(),
                    request.maxPurchaseValue(),
                    request.minPurchaseValue(),
                    Pageable.unpaged()
            );

            List<AssetResponse> assets = page.getContent();

            byte[] excelBytes = generateExcel(assets);

            byte[] auditBytes = generateAuditFile(
                    username,
                    assets.size()
            );

            byte[] zipBytes = generateZip(
                    excelBytes,
                    auditBytes
            );

            String fileName =
                    "inventario_" +
                    LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                    + ".zip";

            String base64 =
                    Base64.getEncoder()
                            .encodeToString(zipBytes);

            return new ReportResponse(
                    200,
                    "Reporte generado correctamente",
                    fileName,
                    base64
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al generar reporte",
                    e
            );
        }
    }

    private byte[] generateExcel(
            List<AssetResponse> assets)
            throws IOException {

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {

            Sheet sheet =
                    workbook.createSheet("Activos");

            Row header = sheet.createRow(0);

            header.createCell(0)
                    .setCellValue("Folio");

            header.createCell(1)
                    .setCellValue("Serie");

            header.createCell(2)
                    .setCellValue("Modelo");

            header.createCell(3)
                    .setCellValue("Categoría");

            header.createCell(4)
                    .setCellValue("Valor Compra");

            header.createCell(5)
                    .setCellValue("Estatus");

            int rowNum = 1;

            for (AssetResponse asset : assets) {

                Row row =
                        sheet.createRow(rowNum++);

                row.createCell(0)
                        .setCellValue(asset.getFolio());

                row.createCell(1)
                        .setCellValue(asset.getSerialNumber());

                row.createCell(2)
                        .setCellValue(asset.getModel());

                row.createCell(3)
                        .setCellValue(asset.getName());

                row.createCell(4)
                        .setCellValue(
                                asset.getPurchaseValue().doubleValue()
                        );

                row.createCell(5)
                        .setCellValue(
                                asset.getStatus().name()
                        );
            }

            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return out.toByteArray();
        }
    }

    private byte[] generateAuditFile(
            String username,
            int totalRecords) {

        String content = """
                Fecha y hora: %s
                Usuario solicitante: %s
                Total registros exportados: %s
                """
                .formatted(
                        LocalDateTime.now(),
                        username,
                        totalRecords
                );

        return content.getBytes(
                StandardCharsets.UTF_8
        );
    }

    private byte[] generateZip(
            byte[] excelBytes,
            byte[] auditBytes)
            throws IOException {

        try (
                ByteArrayOutputStream baos =
                        new ByteArrayOutputStream();

                ZipOutputStream zip =
                        new ZipOutputStream(baos)
        ) {

            ZipEntry excelEntry =
                    new ZipEntry("activos.xlsx");

            zip.putNextEntry(excelEntry);
            zip.write(excelBytes);
            zip.closeEntry();

            ZipEntry auditEntry =
                    new ZipEntry("auditoria.txt");

            zip.putNextEntry(auditEntry);
            zip.write(auditBytes);
            zip.closeEntry();

            zip.finish();

            return baos.toByteArray();
        }
    }
}