package com.crud.latihansecurity.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.latihansecurity.models.RoleEntity;
import com.crud.latihansecurity.models.enums.RoleTypes;
@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, Long> {
    public Optional<RoleEntity> findByName(RoleTypes name);
}
