package com.institucion.inventario_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.institucion.inventario_api.entity.Asset;

@Repository
public interface AssetRepository
        extends JpaRepository<Asset, Long> {

    Optional<Asset> findBySerialNumber(String serialNumber);

    Optional<Asset> findTopByOrderByIdDesc();

    boolean existsBySerialNumber(String serialNumber);
}