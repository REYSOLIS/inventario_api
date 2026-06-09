package com.institucion.inventario_api.service;

import java.time.Year;
import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.stereotype.Service;

import com.institucion.inventario_api.dto.AssetRequest;
import com.institucion.inventario_api.dto.AssetResponse;
import com.institucion.inventario_api.entity.Asset;
import com.institucion.inventario_api.entity.Category;
import com.institucion.inventario_api.exception.BadRequestException;
import com.institucion.inventario_api.exception.ResourceNotFoundException;
import com.institucion.inventario_api.repository.AssetRepository;
import com.institucion.inventario_api.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public AssetResponse create(AssetRequest request) {

        if (request.getSerialNumber() != null
                && assetRepository.existsBySerialNumber(
                        request.getSerialNumber())) {

            throw new BadRequestException(
                    "El número de serie ya existe");
        }

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Categoría no encontrada"));

        String folio = generarFolio();                        
        String year = String.valueOf(Year.now().getValue());
        String createFolio = category.getCode() + "-" + year + "-" + folio;

        Asset asset = Asset.builder()
                .folio(createFolio)
                .serialNumber(request.getSerialNumber())
                .model(request.getModel())
                .status(request.getStatus())
                .purchaseValue(request.getPurchaseValue())
                .acquisitionDate(request.getAcquisitionDate())
                .category(category)
                .build();

        Asset savedAsset =
                assetRepository.save(asset);

        return mapToResponse(savedAsset);
    }

    @Override
    public List<AssetResponse> findAll() {

        return assetRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AssetResponse findById(Long id) {

        Asset asset = assetRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Activo no encontrado"));

        return mapToResponse(asset);
    }

    @Override
    public AssetResponse update(
            Long id,
            AssetRequest request) {

        Asset asset = assetRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Activo no encontrado"));

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Categoría no encontrada"));

                String folio = generarFolio();                        
                String year = String.valueOf(Year.now().getValue());
                String createFolio = category.getCode() + "-" + year + "-" + folio;

                asset.setFolio(createFolio);
                asset.setSerialNumber(request.getSerialNumber());
                asset.setModel(request.getModel());
                asset.setStatus(request.getStatus());
                asset.setPurchaseValue(request.getPurchaseValue());
                asset.setAcquisitionDate(request.getAcquisitionDate());
                asset.setCategory(category);

        Asset updatedAsset =
                assetRepository.save(asset);

        return mapToResponse(updatedAsset);
    }

    @Override
    public void delete(Long id) {

        Asset asset = assetRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Activo no encontrado"));

        asset.setActive(false);

        assetRepository.save(asset);
    }

    private AssetResponse mapToResponse(
            Asset asset) {

        return AssetResponse.builder()
                .folio(asset.getFolio())
                .serialNumber(asset.getSerialNumber())
                .model(asset.getModel())
                .status(asset.getStatus())
                .purchaseValue(asset.getPurchaseValue())
                .acquisitionDate(asset.getAcquisitionDate())
                .categoryId(asset.getCategory().getId())
                .build();
    }

    private String generarFolio() {
        Long consecutivo = assetRepository.findTopByOrderByIdDesc()
                .map(asset -> asset.getId() + 1L)
                .orElse(1L);

        return String.format("%03d", consecutivo);
    }
}