package com.institucion.inventario_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.institucion.inventario_api.entity.Role;
import com.institucion.inventario_api.entity.RoleName;

import java.util.Optional;

@Repository
public interface RoleRepository
        extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
