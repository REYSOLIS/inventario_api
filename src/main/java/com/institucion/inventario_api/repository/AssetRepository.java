package com.institucion.inventario_api.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.institucion.inventario_api.dto.AssetResponse;
import com.institucion.inventario_api.entity.Asset;
import com.institucion.inventario_api.utils.enums.AssetStatus;

@Repository
public interface AssetRepository
        extends JpaRepository<Asset, Long> {

    Optional<Asset> findBySerialNumber(String serialNumber);

    Optional<Asset> findByModel(String model);

    List<Asset> findByCategoryId(Long id);

    List<Asset> findByStatus(AssetStatus status);

    Optional<Asset> findByPurchaseValue(BigDecimal purchaseValue);

    Optional<Asset> findTopByOrderByIdDesc();

    boolean existsBySerialNumber(String serialNumber);

    @Query("""
        SELECT new com.institucion.inventario_api.dto.AssetResponse(
                a.folio,
                a.serialNumber,
                a.model,
                a.status,
                a.purchaseValue,
                a.acquisitionDate,
                c.id as categoryId,
                c.name
        )
        FROM Asset a
        INNER JOIN a.category c
        WHERE (:serialNumber IS NULL OR LOWER(a.serialNumber) LIKE LOWER(CONCAT('%', :serialNumber, '%')))
          AND (:model IS NULL OR LOWER(a.model) LIKE LOWER(CONCAT('%', :model, '%')))
          AND (:status IS NULL OR a.status = :status)
          AND (:categoryId IS NULL OR a.category.id = :categoryId)
          AND (:minPurchaseValue IS NULL OR a.purchaseValue >= :minPurchaseValue)
          AND (:maxPurchaseValue IS NULL OR a.purchaseValue <= :maxPurchaseValue)
        """)
        Page<AssetResponse> searchAssets(
                String serialNumber,
                String model,
                AssetStatus status,
                Long categoryId,
                BigDecimal minPurchaseValue,
                BigDecimal maxPurchaseValue,
                Pageable pageable
        );   
}